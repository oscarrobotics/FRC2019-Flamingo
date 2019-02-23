package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPositionList;
import frc.team832.GrouchLib.Mechanisms.SimpleMechanism;
import frc.team832.GrouchLib.Mechanisms.SmartMechanism;
import frc.team832.GrouchLib.Motors.SimplySmartMotor;
import frc.team832.GrouchLib.Sensors.OscarCANifier;
import frc.team832.GrouchLib.Util.MiniPID;

import static frc.team832.GrouchLib.Util.OscarMath.inRange;

public class SnowBlower extends Subsystem {

    private SimpleMechanism _intake;
    private SimplySmartMotor _hatchHoldor;
    private SmartMechanism _hatchGrabbor;
    private OscarCANifier _canifier;
    private OscarCANifier.Ultrasonic _heightUltrasonic, _centeringUltrasonic;
    private MiniPID _cargoHeightController, _holderPID;

    private double holdorTarget;

    private CargoPosition _cargoPosition;

    private boolean _open;

    private boolean _holdBall = false;

    public SnowBlower(SimpleMechanism intake, SimplySmartMotor hatchHolder, OscarCANifier canifier, SmartMechanism hatchGrabber){
        _intake = intake;
        _hatchHoldor = hatchHolder;
        _canifier = canifier;
        _hatchGrabbor = hatchGrabber;

        com.ctre.phoenix.CANifier.PWMChannel triggerChannel = com.ctre.phoenix.CANifier.PWMChannel.PWMChannel0;

        _heightUltrasonic = _canifier.getUltrasonic(Constants.UltrasonicTriggerChannel, com.ctre.phoenix.CANifier.PWMChannel.PWMChannel1);
        _centeringUltrasonic = _canifier.getUltrasonic(com.ctre.phoenix.CANifier.PWMChannel.PWMChannel0, com.ctre.phoenix.CANifier.PWMChannel.PWMChannel2);

        _holderPID = new MiniPID(1, 0,0);
        _cargoHeightController = new MiniPID(Constants.HeightController_kP, Constants.HeightController_kI, Constants.HeightController_kD, Constants.HeightController_kF);
    }

    public void setGrabborPosition(String index){
        _hatchGrabbor.setPosition(index);
    }

    public double getGrabborTargetPosition(String index){
        return _hatchGrabbor.getPresetPosition(index).getTarget();
    }

    public double getGrabborCurrentPosition(){
        return _hatchGrabbor.getCurrentPosition();
    }

    public double getHoldorTargetPosition(){
        return holdorTarget;
    }

    public double getHoldorCurrentPosition(){
        return _canifier.getQuadPosition();
    }

    @Override
    public void periodic() {
        _cargoPosition = updateCargoPosition();
        if(_holdBall){
            _intake.set(ballPIDPow());
        }

    }

    public enum CargoPosition {
        UNKNOWN,
        BOTTOM,
        MIDDLE,
        TOP
    }

    public boolean getHatchCoverStatus() {
        // do fancy limit switch checking here
        return false;
    }

    public double getCargoHeight(){
        _heightUltrasonic.update();
        return _heightUltrasonic.getRangeInches();
    }

    public void setBallStatus(boolean holdBall){
        _holdBall = holdBall;
    }


    public double ballPIDPow(){
       if(getCargoHeight() > Constants.CargoBottom_MinInches){
           return _cargoHeightController.getOutput(getCargoHeight(), Constants.CargoMiddle_MinInches - Constants.CargoMiddle_MaxInches);
       }else{
           return 0.0;
       }

    }


    public CargoPosition getCargoPosition() {
        return _cargoPosition;
    }

    private CargoPosition updateCargoPosition() {
        _heightUltrasonic.update();
        double cargoDist = _heightUltrasonic.getRangeInches();
        boolean cargoBottomSensor = false; // TODO: check height and bottom ultrasonic

        if (cargoBottomSensor) {
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

    public void setHatchHolderPosition(String index){
        holdorTarget = Constants.HolderPositions.getByIndex(index).getTarget();
        _holderPID.setSetpoint(holdorTarget);
        _hatchHoldor.setPosition(_holderPID.getOutput(_canifier.getQuadPosition()));

        _open = !(index == "Closed");
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

    private boolean cargoEntered(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoEnter_LeftInches, Constants.CargoEnter_RightInches);
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

        public static final double CargoEnter_LeftInches = 8;
        public static final double CargoEnter_RightInches = 12;

        public static final com.ctre.phoenix.CANifier.PWMChannel UltrasonicTriggerChannel = com.ctre.phoenix.CANifier.PWMChannel.PWMChannel0;

        public static final MechanismPosition[] holderPositions = new MechanismPosition[]{
                //TODO: put actual numbers here
                new MechanismPosition("Open", 100),
                new MechanismPosition("Closed", 200)
        };

        public static final MechanismPositionList HolderPositions = new MechanismPositionList(holderPositions);

        public static final MechanismPosition[] grabberPositions = new MechanismPosition[]{
                //TODO: put actual numbers here
                new MechanismPosition("Initial", 100),
                new MechanismPosition("Release", 200),
                new MechanismPosition("Floor", 700)
        };
    }

    @Override
    protected void initDefaultCommand() { }
}
