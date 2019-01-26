package frc.team832.robot.Subsystems;

import com.ctre.phoenix.CANifier;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.OscarMechanismPosition;
import frc.team832.GrouchLib.Mechanisms.OscarSimpleMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarSmartMechanism;
import frc.team832.GrouchLib.Sensors.OscarCANifier;

import static frc.team832.GrouchLib.Util.OscarMath.inRange;

public class SnowBlower extends Subsystem {

    private OscarSimpleMechanism _intake;
    private OscarSmartMechanism _hatchHoldor;
    private OscarSmartMechanism _hatchGrabbor;
    private OscarCANifier _canifier;
    private OscarCANifier.Ultrasonic _heightUltrasonic;
    public HatchGrabFloorState _currentHatchState = HatchGrabFloorState.INITIAL;

    public SnowBlower(OscarSimpleMechanism intake, OscarSmartMechanism hatchHolder, OscarCANifier canifier, OscarSmartMechanism hatchGrabber){
        _intake = intake;
        _hatchHoldor = hatchHolder;
        _canifier = canifier;
        _hatchGrabbor = hatchGrabber;

        _heightUltrasonic = _canifier.addUltrasonic(CANifier.PWMChannel.PWMChannel0, CANifier.PWMChannel.PWMChannel1);
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

        if (gettingCargo) {
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
            switch (_currentHatchState){
                case INITIAL:
                    _hatchGrabbor.setPosition("Initial");
                    _hatchHoldor.setPosition("Closed");
                    if(Math.abs(_hatchGrabbor.getPosition() - Constants.grabberPositions[0].getTarget())<= 20) {
                        _hatchGrabbor.setPosition("Floor");
                        newHatchState(HatchGrabFloorState.LOWER_ARMS);
                    }
                    break;
                case LOWER_ARMS:
                    if(Math.abs(_hatchGrabbor.getPosition() - Constants.grabberPositions[2].getTarget())<= 20) {
                        _hatchGrabbor.setPosition("Release");
                        newHatchState(HatchGrabFloorState.RAISE_ARMS);
                    }
                    break;
                case RAISE_ARMS:
                    if(Math.abs(_hatchGrabbor.getPosition() - Constants.grabberPositions[1].getTarget())<= 20) {
                        _hatchHoldor.setPosition("Open");
                        newHatchState(HatchGrabFloorState.OPEN_HOLDER);
                    }
                    break;
                case OPEN_HOLDER:
                    if(Math.abs(_hatchHoldor.getPosition() - Constants.holderPositions[1].getTarget())<= 20) {
                        _hatchGrabbor.setPosition("Initial");
                        newHatchState(HatchGrabFloorState.LOWER_ARMS);
                    }
                    break;
                case RETRACT_ARMS:
                    getHatchFloor = false;
                    break;
            }
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
        if(open)
            _hatchHoldor.setPosition("Open");
        else {
            _hatchHoldor.setPosition("Closed");
        }
    }


    private boolean cargoAtBottom(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoBottomMinInches, Constants.CargoBottomMaxInches);
    }

    private boolean cargoAtMiddle(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoMiddleMinInches, Constants.CargoMiddleMaxInches);
    }

    private boolean cargoAtTop(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoTopMinInches, Constants.CargoTopMaxInches);
    }

    public static class Constants {

        public static final double CargoBottomMinInches = 32.1;
        public static final double CargoBottomMaxInches = 24;
        public static final double CargoMiddleMinInches = 24.1;
        public static final double CargoMiddleMaxInches = 16;
        public static final double CargoTopMinInches = 16.1;
        public static final double CargoTopMaxInches = 8;

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
