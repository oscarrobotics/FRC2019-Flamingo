package frc.team832.robot.Subsystems;

import com.ctre.phoenix.CANifier;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismComplexPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;
import frc.team832.GrouchLib.Mechanisms.OscarSimpleMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarSmartMechanism;
import frc.team832.GrouchLib.Sensors.OscarCANifier;
import frc.team832.GrouchLib.Util.MiniPID;
import frc.team832.robot.Robot;

import static frc.team832.GrouchLib.Util.OscarMath.inRange;

public class SnowBlower extends Subsystem {

    private OscarSimpleMechanism _intake;
    private OscarSmartMechanism _hatchHoldor;
    private OscarSmartMechanism _hatchGrabbor;
    private OscarCANifier _canifier;
    private OscarCANifier.Ultrasonic _heightUltrasonic, _centeringUltrasonic;
    private MiniPID _cargoHeightController;

    public HatchGrabFloorState _currentHatchState = HatchGrabFloorState.INITIAL;

    public Action toRunAction, runningAction;

    public SnowBlower(OscarSimpleMechanism intake, OscarSmartMechanism hatchHolder, OscarCANifier canifier, OscarSmartMechanism hatchGrabber){
        _intake = intake;
        _hatchHoldor = hatchHolder;
        _canifier = canifier;
        _hatchGrabbor = hatchGrabber;

        _heightUltrasonic = _canifier.addUltrasonic(CANifier.PWMChannel.PWMChannel0, CANifier.PWMChannel.PWMChannel1);
        _centeringUltrasonic = _canifier.addUltrasonic(CANifier.PWMChannel.PWMChannel0, CANifier.PWMChannel.PWMChannel2);

        _cargoHeightController = new MiniPID(Constants.HeightController_kP, Constants.HeightController_kI, Constants.HeightController_kD, Constants.HeightController_kF);
    }

    public void setAction(Action action) {
        toRunAction = action;
    }

    public void setIdle() {
        toRunAction = Action.IDLE;
        runningAction = Action.IDLE;
    }

    public enum HatchGrabFloorState {
        INITIAL,
        LOWER_ARMS,
        RAISE_ARMS,
        OPEN_HOLDER,
        RETRACT_ARMS
    }

    @Override
    public void periodic() {
        boolean gettingCargo = true;
        boolean gettingHatch = true;
        boolean getHatchFloor = true;

        // action non-cancellation
        if (toRunAction != Action.IDLE && runningAction == Action.IDLE) {
            runningAction = toRunAction;
        }

        switch(runningAction) {
            case INTAKE_HP_HATCH:
                break;
            case INTAKE_FLOOR_HATCH:
                switch (_currentHatchState){
                    case INITIAL:
                        _hatchGrabbor.setPosition("Initial");
                        _hatchHoldor.setPosition("Closed");
                        if(Math.abs(_hatchGrabbor.getPresetPosition("").getTarget() - Constants.grabberPositions[0].getTarget())<= 20) {
                            _hatchGrabbor.setPosition("Floor");
                            newHatchState(HatchGrabFloorState.LOWER_ARMS);
                        }
                        break;
                    case LOWER_ARMS:
                        if(Math.abs(_hatchGrabbor.getPresetPosition("").getTarget() - Constants.grabberPositions[2].getTarget())<= 20) {
                            _hatchGrabbor.setPosition("Release");
                            newHatchState(HatchGrabFloorState.RAISE_ARMS);
                        }
                        break;
                    case RAISE_ARMS:
                        if(Math.abs(_hatchGrabbor.getPresetPosition("").getTarget() - Constants.grabberPositions[1].getTarget())<= 20) {
                            _hatchHoldor.setPosition("Open");
                            newHatchState(HatchGrabFloorState.OPEN_HOLDER);
                        }
                        break;
                    case OPEN_HOLDER:
                        if(Math.abs(_hatchHoldor.getPresetPosition("").getTarget() - Constants.holderPositions[1].getTarget())<= 20) {
                            _hatchGrabbor.setPosition("Initial");
                            newHatchState(HatchGrabFloorState.LOWER_ARMS);
                        }
                        break;
                    case RETRACT_ARMS:
                        getHatchFloor = false;
                        break;
                }
                break;
            case INTAKE_FLOOR_CARGO:
            case INTAKE_LOW_FLOOR_CARGO:
                // we case both together, and check the action later, as the code is 99% the same, just a different position

                // get desired position
                OscarMechanismComplexPosition cargoPosition = ComplexLift.Constants.Positions.getByIndex(
                        toRunAction == Action.INTAKE_LOW_FLOOR_CARGO ? "" : "");

                // set ComplexLift position
                Robot.complexLift.setPosition(cargoPosition);

                switch (getCargoPosition()) {
                    case UNKNOWN:
                        // do nothing
                        break;
                    case BOTTOM:
                        intakeSet(0.5);
                        break;
                    case BOTTOM_CENTERED:
                        intakeSet(0.5);
                        break;
                    case MIDDLE:
                        intakeSet(0.0);
                        gettingCargo = false;
                        break;
                    case TOP:
                        intakeSet(-0.5);
                        break;
                }
                break;
            case IDLE:
                // fancy LEDs?
                break;
        }

        if (gettingCargo) {

        }

        if(gettingHatch){
            if(getHatchCoverStatus()){
                setHatchHolderOpen(false);
                gettingHatch = false;
            }
            else{
                setHatchHolderOpen(true);
            }
        }

        if(getHatchFloor){

        }

    }

    public enum CargoPosition {
        UNKNOWN,
        BOTTOM,
        BOTTOM_CENTERED,
        MIDDLE,
        TOP
    }
    public boolean getHatchCoverStatus() {
        // do fancy limit switch checking here
        return false;
    }

    public void newHatchState(HatchGrabFloorState newState){
        _currentHatchState = newState;
    }

    public CargoPosition getCargoPosition() {
        // todo: determine how to differentiate the cargo being centered in the bottom, or being off-center in the bottom
        // is this even really necessary? we could just bring the ball to the middle and it'd be centered.
        // maybe some combination of reflectance/optical and a line break?
        // maybe even another ultrasonic going across the bottom?

        _heightUltrasonic.update();
        double cargoDist = _heightUltrasonic.getRangeInches();
        boolean cargoBottomSensor = true;

        if (cargoBottomSensor && cargoAtBottom(cargoDist)) {
            return CargoPosition.BOTTOM_CENTERED;
        }
        else if (cargoBottomSensor) {
            return CargoPosition.BOTTOM;
        }
        else if (cargoAtMiddle(cargoDist)) {
            return CargoPosition.MIDDLE;
        }
        else if (cargoAtTop(cargoDist)) {
            return CargoPosition.TOP;
        }
        else {
            return CargoPosition.UNKNOWN;
        }
    }

    public void intakeSet(double power) {
        _intake.set(power);
    }

    public void setHatchHolderOpen(boolean open){
        _hatchHoldor.setPosition(open ? "Open" : "Closed");
    }

    private boolean cargoAtBottom(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoBottom_MinInches, Constants.CargoBottom_MaxInches);
    }

    private boolean cargoAtMiddle(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoMiddle_MinInches, Constants.CargoMiddle_MaxInches);
    }

    private boolean cargoAtTop(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoTop_MinInches, Constants.CargoTop_MaxInches);
    }

    private boolean cargoCentered(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoCenter_LeftInches, Constants.CargoCenter_RightInches);
    }

    public enum Action {
        INTAKE_HP_HATCH,
        INTAKE_FLOOR_HATCH,
        INTAKE_FLOOR_CARGO,
        INTAKE_LOW_FLOOR_CARGO,
        IDLE
    }

    public static class Constants {

        public static final double HeightController_kP = 1;
        public static final double HeightController_kI = 1;
        public static final double HeightController_kD = 1;
        public static final double HeightController_kF = 1;

        public static final double CargoNominalDiameterInches = 13;

        public static final double CargoBottom_MinInches = 32.1;
        public static final double CargoBottom_MaxInches = 24;
        public static final double CargoMiddle_MinInches = 24.1;
        public static final double CargoMiddle_MaxInches = 16;
        public static final double CargoTop_MinInches = 16.1;
        public static final double CargoTop_MaxInches = 8;

        public static final double CargoCenter_LeftInches = 8;
        public static final double CargoCenter_RightInches = 12;

        public static final OscarMechanismPosition[] holderPositions = new OscarMechanismPosition[]{
                //TODO: put actual numbers here
                new OscarMechanismPosition("Open", 100),
                new OscarMechanismPosition("Closed", 200)
        };

        public static final OscarMechanismPosition[] grabberPositions = new OscarMechanismPosition[]{
                //TODO: put actual numbers here
                new OscarMechanismPosition("Initial", 100),
                new OscarMechanismPosition("Release", 200),
                new OscarMechanismPosition("Floor", 700)
        };
    }

    @Override
    protected void initDefaultCommand() { }
}
