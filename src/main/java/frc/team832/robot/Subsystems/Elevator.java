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
    }

    public void pushData() {
        SmartDashboard.putNumber("Elevator Position", _elevator.getCurrentPosition());
        SmartDashboard.putNumber("Elevator Target", _elevator.getTargetPosition());
        SmartDashboard.putNumber("Elevator Velocity", _elevator.getVelocity());
        SmartDashboard.putNumber("Elevator Inches", getCurrentInches());
    }

    public void initDefaultCommand() { }

    public void setMotionPosition(double position) {
    	setMotionPosition(position, 0.2);
	}

    public void setMotionPosition(double position, double arbFF) {
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

        public static final int BOTTOM_VALUE = RobotMap.isComp ? 35 : -685;
        public static final int TOP_VALUE = RobotMap.isComp ? 430 : -375;
		public static final int BOTTOM_SOFT = BOTTOM_VALUE + 5;
		public static final int TOP_SOFT = TOP_VALUE - 5;
        public static final int MID_VALUE = (int)OscarMath.mid(BOTTOM_VALUE, TOP_VALUE);

        public static final double POT_RANGE = (TOP_VALUE) - (BOTTOM_VALUE);
        public static final double POT_TO_INCHES = 44.0 / POT_RANGE;
        public static final double INCHES_TO_POT = 1 / POT_TO_INCHES;

        public static final int MOTION_MAGIC_VEL = 750;
        public static final int MOTION_MAGIC_ACC = 750;

        public static double PotToInches(double value) {
            return OscarMath.map(value, BOTTOM_VALUE, TOP_VALUE, 0, 30);
        }
        public static double InchesToPot(double value) { return OscarMath.map(value, 0, 30, BOTTOM_VALUE, TOP_VALUE);}

        private static final MechanismPosition[] _positions = new MechanismPosition[]{
                new MechanismPosition("StartConfig", TOP_VALUE),
                new MechanismPosition("TestMiddle", OscarMath.mid(TOP_VALUE, BOTTOM_VALUE)),
                new MechanismPosition("TestTop", TOP_VALUE),

                new MechanismPosition("Bottom", BOTTOM_VALUE),
                new MechanismPosition("Middle", MID_VALUE),
                new MechanismPosition("Top", TOP_VALUE),

                new MechanismPosition("IntakeCargo_Floor", BOTTOM_VALUE),
                new MechanismPosition("IntakeHatch_HP", BOTTOM_VALUE),//TOP_VALUE - 10
				new MechanismPosition("HatchGrab", BOTTOM_VALUE),

                new MechanismPosition("CargoShip_Hatch", TOP_VALUE),
                new MechanismPosition("CargoShip_Cargo", BOTTOM_VALUE),

                new MechanismPosition("RocketHatch_Low", BOTTOM_VALUE),
                new MechanismPosition("RocketHatch_Middle", BOTTOM_VALUE),
                new MechanismPosition("RocketHatch_High", TOP_VALUE),

                new MechanismPosition("RocketCargo_Low", BOTTOM_VALUE),
                new MechanismPosition("RocketCargo_Middle", BOTTOM_VALUE),
                new MechanismPosition("RocketCargo_High", TOP_VALUE)
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
			GrabHatch("HatchGrab"),
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
