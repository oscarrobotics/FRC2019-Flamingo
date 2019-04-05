package frc.team832.robot.Subsystems;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Mechanisms.GeniusMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismMotionProfile;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPositionList;
import frc.team832.GrouchLib.Util.OscarMath;
import frc.team832.robot.Robot;
import frc.team832.robot.RobotMap;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Fourbar extends Subsystem {

	private GeniusMechanism _top;
	private MotionProfileStatus topStatus = new MotionProfileStatus();
	private int targetPos = 0;

	public Fourbar (GeniusMechanism top) {
		_top = top;
	}

	public double getTopTargetPosition () {
		return _top.getTargetPosition();
	}

	public double getTopCurrentPosition () {
		return _top.getCurrentPosition();
	}

	public boolean atTargetPosition () {
		return (Math.abs(getTopCurrentPosition() - getTopTargetPosition()) < 20);
	}

	public void setTopLimits (int lowerLimit, int upperLimit) {
		_top.setLowerLimit(lowerLimit);
		_top.setUpperLimit(upperLimit);
	}

	public void setTopPIDF (double kP, double kI, double kD, double kF) {
		_top.setPIDF(kP, kI, kD, kF);
	}

	@Override
	public void periodic () {
		if (!isSafe()) {
			_top.getMotor().setMotionMagcArbFF(getMinSafePos(), armFF());
		} else {
			_top.getMotor().setMotionMagcArbFF(targetPos, armFF());
		}

		if (isMovingDown()){
			_top.getMotor().configMotionMagic(Constants.MOTION_MAGIC_VEL / 2,Constants.MOTION_MAGIC_ACC / 2);
		} else {
			_top.getMotor().configMotionMagic(Constants.MOTION_MAGIC_VEL,Constants.MOTION_MAGIC_ACC);
		}
	}

	public void pushData () {
		SmartDashboard.putNumber("Top Fourbar", getTopCurrentPosition());
		SmartDashboard.putNumber("Fourbar error: ", _top.getMotor().getClosedLoopError());
		SmartDashboard.putNumber("Bottom Adj", Constants.convertUpperToLower(getTopCurrentPosition()));
		SmartDashboard.putNumber("ArmDeg", armDeg());
	}

	public void stop () {
		_top.stop();
	}

	public void setPosition (String index) {
		MechanismPosition upperPos = Constants.Positions.getByIndex(index);
		_top.setPosition(upperPos);
	}

	public void setPosition (double pos) {
		MechanismPosition upperPos = new MechanismPosition("ManualControl", pos);
		_top.setPosition(upperPos);
	}

	public void testAdjustment (double adjVal) {
		_top.setPosition(new MechanismPosition("AdjControl", getTopTargetPosition() + adjVal));
	}

	public void setMotionPosition (double position, double arbFF) {
		//mid = 2725      -65.5 min to 68.5 max
		targetPos = (int) position;
	}

	public double armDeg () {
		return OscarMath.map(getTopCurrentPosition(), 0, Constants.TOP_MAX_VAL, -65.5, 61);
	}

	public double armFF () {
		final double gravFF = .10;
		return gravFF * Math.cos(Math.toRadians(armDeg()));
	}

	@Override
	public void initDefaultCommand () {
	}

	public void bufferTrajectories (MechanismMotionProfile topTraj, MechanismMotionProfile botTraj) {
		_top.bufferTrajectory(topTraj);
	}

	public MotionProfileStatus getTopMpStatus () {
		return topStatus;
	}

	public void setMPControl (SetValueMotionProfile v) {
		_top.setMotionProfile(v.value);
	}

	public void bufferAndSendMP () {
		_top.bufferAndSendMP();
	}

	public boolean isMPFinished () {
		return _top.isMPFinished();
	}

	//fourbar3000 elevatormin
	public int getMinSafePos () {
		//2700cos(x/210)
		int offset;

		double fourbarMinAngle = RobotMap.isComp ? (65.5 * (Math.cos((Robot.elevator.getTargetPosition() - 35)/(248))) - 65.5) : (65.5 * (Math.cos((Robot.elevator.getTargetPosition() + 685)/(216.456))) - 65.5);
		SmartDashboard.putNumber("Safe Degrees", fourbarMinAngle);
		if (Robot.fourbar.getTopCurrentPosition() < 1000){
			offset = 700;
		} else if (Robot.fourbar.getTopCurrentPosition() < 1500){
			offset = 400;
		} else if (Robot.fourbar.getTopCurrentPosition() < 2000){
			offset = 0;
		} else {
			offset = -100;
		}

		//RobotMap.isComp ? (-(-0.0146 * Math.pow(Robot.elevator.getTargetPosition(), 2)) - (16.5 * Robot.elevator.getTargetPosition() - 6000)) / 2 + 100 : (-0.015 * Math.pow(Robot.elevator.getTargetPosition(), 2)) - (25.0 * Robot.elevator.getTargetPosition()) - 6950;//5800 ish

		double fourbarMinPos = OscarMath.map(fourbarMinAngle, -65.5, 0, 0, Constants.TOP_MID_VAL) + offset;
		SmartDashboard.putNumber("Min Safe Val: ", fourbarMinPos);
		return (int) fourbarMinPos;
	}

	public boolean isMovingDown(){
		return getTopCurrentPosition() > getTopTargetPosition();
	}

	public boolean isSafe () {
		boolean isSafe;
		int fourbarMinPos = getMinSafePos();
		SmartDashboard.putNumber("Fourbar Safe Min: ", fourbarMinPos);

		isSafe = !(targetPos < fourbarMinPos);
		SmartDashboard.putBoolean("Is Safe: ", isSafe);

		return isSafe;
	}

	@SuppressWarnings({"unused", "WeakerAccess"})
	public static class Constants {

		public static final double TOP_MIN_VAL = 0;
		public static final double TOP_MID_VAL = 2600;
		public static final double TOP_MAX_VAL = 5000;
		public static final double ARMLENGTH = 30.75;
		public static final double UPPERPOTTOANGLE = .262;
		public static final double UPPERPOTOFFSET = 112.66;
		public static final double HEIGHTOFFSET = 2;

		public static final int MOTION_MAGIC_VEL = 1500;
		public static final int MOTION_MAGIC_ACC = 1500;

		public static final double MAXINCHES = 27;
		public static final double MININCHES = -29;

		private static MechanismPosition[] _positions = new MechanismPosition[]{
				new MechanismPosition("StartConfig", TOP_MIN_VAL),

				new MechanismPosition("Bottom", TOP_MIN_VAL),
				new MechanismPosition("Middle", TOP_MID_VAL),
				new MechanismPosition("Top", TOP_MAX_VAL),

				new MechanismPosition("IntakeHatch_HP", TOP_MID_VAL),
				new MechanismPosition("IntakeCargo_Floor", TOP_MID_VAL),

				new MechanismPosition("CargoShip_Hatch", TOP_MID_VAL),
				new MechanismPosition("CargoShip_Cargo", 3500),

				new MechanismPosition("RocketHatch_Low", TOP_MID_VAL),
				new MechanismPosition("RocketHatch_Middle", TOP_MAX_VAL),
				new MechanismPosition("RocketHatch_High", TOP_MAX_VAL),

				new MechanismPosition("RocketCargo_Low", TOP_MID_VAL),
				new MechanismPosition("RocketCargo_Middle", 4600),
				new MechanismPosition("RocketCargo_High", TOP_MAX_VAL - 100),
		};

		@SuppressWarnings("unused")
		public enum FourbarPosition {
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
			RocketCargo_Low("RocketCargo_Low"),
			RocketCargo_Middle("RocketCargo_Middle"),
			RocketCargo_High("RocketCargo_High");

			String _index;

			FourbarPosition (String index) {
				_index = index;
			}

			public String getIndex () {
				return _index;
			}
		}

		public static final MechanismPositionList Positions = new MechanismPositionList(_positions);

		public static double inchToPotTick (double inches) {
			return (Math.toDegrees(Math.asin(inches / ARMLENGTH)) + UPPERPOTOFFSET) / UPPERPOTTOANGLE;
		}

		public static double potTickToInchTop (double potTick) {
			return Math.sin(Math.toRadians((potTick * UPPERPOTTOANGLE) - UPPERPOTOFFSET)) * ARMLENGTH;
		}

		public static double convertUpperToLower (double upperVal) {
			return (-1.39 * upperVal) + 1044;
		}

		public static double convertCompUpperToLower (double upperVal) {
			return (-.452 * upperVal) + 332.38;
		}
	}
}
