package frc.team832.robot.Subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Mechanisms.GeniusMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismMotionProfile;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPositionList;
import frc.team832.GrouchLib.Util.OscarMath;
import frc.team832.robot.RobotMap;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Elevator extends Subsystem {

    private GeniusMechanism _elevator;

    private MotionProfileStatus elevatorStatus = new MotionProfileStatus();

    public Elevator(GeniusMechanism elevator){
        _elevator = elevator;
    }

    public double getTargetPosition(){

        return _elevator.getTargetPosition();
    }

    public double

    getCurrentPosition(){
        return _elevator.getCurrentPosition();
    }

    public boolean atTargetPosition() {
        return (Math.abs(getCurrentPosition() - getTargetPosition()) < 20);
    }

    public double getCurrentInches() { return Constants.PotToInches(getCurrentPosition()); }

    public void setPosition(String index) {
        _elevator.setPosition(index);
    }

    public void setPosition(double pos){
        _elevator.setPosition(new MechanismPosition("ManualControl", pos));
    }

    @Override
    public void periodic() {
        if (isMovingDown()){
            _elevator.getMotor().configMotionMagic(Constants.MOTION_MAGIC_VEL / 2, Constants.MOTION_MAGIC_ACC / 2);
        } else {
            _elevator.getMotor().configMotionMagic(Constants.MOTION_MAGIC_VEL, Constants.MOTION_MAGIC_ACC);
        }
    }

    public void pushData() {
        SmartDashboard.putNumber("Elevator Position", _elevator.getCurrentPosition());
        SmartDashboard.putNumber("Elevator Target", _elevator.getTargetPosition());
        SmartDashboard.putNumber("Elevator Velocity", _elevator.getVelocity());
        SmartDashboard.putNumber("Elevator Inches", getCurrentInches());
    }

    public void initDefaultCommand() { }

    public void setMotionPosition(double position){
        double arbFF = .2;
        _elevator.getMotor().setMotionMagcArbFF(position, arbFF);
    }

    public void bufferTrajectory(MechanismMotionProfile profile) {
        _elevator.bufferTrajectory(profile);
    }

    public ErrorCode getMPStatus(){
        return _elevator.getMotionProfileStatus(elevatorStatus);
    }

    public void setMPControl(SetValueMotionProfile v) {
        _elevator.setMotionProfile(v.value);
    }

    public void bufferAndSendMP() {
        _elevator.bufferAndSendMP();
    }

    public boolean isMPFinished() {
        return _elevator.isMPFinished();
    }

    public void testAdjustment(int adjVal) {
        _elevator.setPosition(new MechanismPosition("AdjControl", getTargetPosition()+adjVal));
    }

    public boolean isMovingDown(){
        if (getTargetPosition() < getCurrentPosition()){
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class Constants {

        public static final int POT_BOTTOM_VALUE = RobotMap.isComp ? 35 : -685;
        public static final int POT_MID_VALUE = RobotMap.isComp ? 195 : -545;
        public static final int POT_TOP_VALUE = RobotMap.isComp ? 430 : -375;

        public static final double POT_RANGE = (POT_TOP_VALUE) - (POT_BOTTOM_VALUE);
        public static final double POT_TO_INCHES = 44.0 / POT_RANGE;
        public static final double INCHES_TO_POT = 1 / POT_TO_INCHES;

        public static final int MOTION_MAGIC_VEL = 1000;
        public static final int MOTION_MAGIC_ACC = 1500;

        public static double PotToInches(double value) {
            return OscarMath.map(value, POT_BOTTOM_VALUE, POT_TOP_VALUE, 0, 30);
        }
        public static double InchesToPot(double value) { return OscarMath.map(value, 0, 30, POT_BOTTOM_VALUE, POT_TOP_VALUE);}

        private static final MechanismPosition[] _positions = new MechanismPosition[]{
                new MechanismPosition("StartConfig", POT_TOP_VALUE),
                new MechanismPosition("TestMiddle", OscarMath.mid(POT_TOP_VALUE, POT_BOTTOM_VALUE)),
                new MechanismPosition("TestTop", POT_TOP_VALUE),

                new MechanismPosition("Bottom", POT_BOTTOM_VALUE),
                new MechanismPosition("Middle", POT_MID_VALUE),
                new MechanismPosition("Top", POT_TOP_VALUE),

                new MechanismPosition("IntakeCargo_Floor", POT_BOTTOM_VALUE),
                new MechanismPosition("IntakeHatch_HP",POT_BOTTOM_VALUE),//POT_TOP_VALUE - 10

                new MechanismPosition("CargoShip_Hatch", POT_TOP_VALUE),
                new MechanismPosition("CargoShip_Cargo", POT_BOTTOM_VALUE),

                new MechanismPosition("RocketHatch_Low", POT_BOTTOM_VALUE),
                new MechanismPosition("RocketHatch_Middle", POT_BOTTOM_VALUE),
                new MechanismPosition("RocketHatch_High", POT_TOP_VALUE),

                new MechanismPosition("RocketCargo_Low", POT_BOTTOM_VALUE),
                new MechanismPosition("RocketCargo_Middle",POT_BOTTOM_VALUE),
                new MechanismPosition("RocketCargo_High", POT_TOP_VALUE)
        };

        @SuppressWarnings("unused")
        public enum ElevatorPosition {
            StartConfig("StartConfig"),
            StorageConfig("StorageConfig"),
            Bottom("Bottom"),
            Middle("Middle"),
            Top("Top"),
            IntakeCargo_Floor("IntakeCargo_Floor"),
            IntakeHatch_HP("IntakeHatch_HP"),
            CargoShip_Hatch("CargoShip_Hatch"),
            CargoShip_Cargo("CargoShip_Cargo"),
            RocketHatch_Low("RocketHatch_Low"),
            RocketHatch_Middle("RocketHatch_Middle"),
            RocketHatch_High("RocketHatch_High"),
            RocketCargo_Low ("RocketCargo_Low"),
            RocketCargo_Middle("RocketCargo_Middle"),
            RocketCargo_High("RocketCargo_High");

            String _index;

            ElevatorPosition(String index) { _index = index; }

            public String getIndex() { return _index; }
        }


        public static final MechanismPositionList Positions = new MechanismPositionList(_positions);
    }
}
