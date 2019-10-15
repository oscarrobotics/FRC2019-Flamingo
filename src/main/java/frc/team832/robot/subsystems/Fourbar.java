package frc.team832.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.driverstation.dashboard.DashboardManager;
import frc.team832.GrouchLib.driverstation.dashboard.DashboardUpdatable;
import frc.team832.GrouchLib.motorcontrol.CANTalon;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;
import frc.team832.GrouchLib.util.OscarMath;
import frc.team832.robot.Constants;
import frc.team832.robot.RobotContainer;

import static frc.team832.robot.RobotContainer.elevator;
import static frc.team832.robot.RobotContainer.fourbar;

public class Fourbar extends SubsystemBase implements DashboardUpdatable {
	private CANTalon fourbarTop, fourbarBottom;


	private NetworkTableEntry dashboard_AppliedPower;
	private NetworkTableEntry dashboard_DesiredPos;
	private NetworkTableEntry dashboard_RawPos;
	private NetworkTableEntry dashboard_minSafeAngle;
	private NetworkTableEntry dashboard_minSafePos;

	private double safety_minFourbarAngle;
	private double safety_minFourbarPos;
	private boolean safety_isSafe;

	public Fourbar () {
		super();
		setName("Fourbar Subsys");
		DashboardManager.addTab(this);
		DashboardManager.addTabSubsystem(this, this);
	}

	@Override
	public void periodic () {
//		safety_isSafe = checkSafety();
//		if (!isSafe()) {
//			_top.getMotor().setMotionMagcArbFF(getMinSafePos(), armFF());
//		} else {
//			_top.getMotor().setMotionMagcArbFF(targetPos, armFF());
//		}
//
//		if (isMovingDown()){
//			_top.getMotor().configMotionMagic(Constants.MOTION_MAGIC_VEL / 2,Constants.MOTION_MAGIC_ACC / 2);
//		} else {
//			_top.getMotor().configMotionMagic(Constants.MOTION_MAGIC_VEL,Constants.MOTION_MAGIC_ACC);
//		}
		getMinSafePos();
	}

	public boolean initialize() {
		boolean successful = true;
		fourbarTop = new CANTalon(Constants.FOURBARTOP_CAN_ID);
		fourbarBottom = new CANTalon(Constants.FOURBARBOTTOM_CAN_ID);

		if (!(fourbarTop.getInputVoltage() > 0)) successful = false;
		if (!(fourbarBottom.getInputVoltage() > 0)) successful = false;

		fourbarTop.resetSettings();
		fourbarBottom.resetSettings();

		fourbarTop.setInverted(false);
		fourbarTop.setSensorPhase(true);

		fourbarTop.setkP(Constants.armPIDF[0]);
		fourbarTop.setkF(Constants.armPIDF[3]);

		NeutralMode allIdleMode = NeutralMode.kBrake;
		fourbarTop.setNeutralMode(allIdleMode);
		fourbarBottom.setNeutralMode(allIdleMode);

		fourbarBottom.follow(fourbarTop);
		fourbarBottom.setInverted(true);//does one need to be inverted?

		dashboard_AppliedPower = DashboardManager.addTabItem(this, "Applied Power", 0.0);
		dashboard_DesiredPos = DashboardManager.addTabItem(this, "Desired Pos", 0.0);
		dashboard_RawPos = DashboardManager.addTabItem(this, "Raw Pos", 0.0);
		dashboard_minSafeAngle = DashboardManager.addTabItem(this, "Min Safe Angle", 0.0);
		dashboard_minSafePos = DashboardManager.addTabItem(this, "Min Safe Pos", 0.0);

		fourbarTop.setForwardSoftLimit((int)Constants.FOURBAR_SOFT_MAX);
		fourbarTop.setReverseSoftLimit((int)Constants.FOURBAR_SOFT_MIN);

		return successful;
	}

	public boolean isSafe() {
		boolean isSafe;
		int fourbarMinPos = getMinSafePos();
		SmartDashboard.putNumber("Fourbar Safe Min: ", fourbarMinPos);

		isSafe = !(fourbarTop.getTargetPosition() < fourbarMinPos);
		SmartDashboard.putBoolean("Is Safe: ", isSafe);

		return isSafe;
	}

	public double getRawPosition() {
		return fourbarTop.getSensorPosition();
	}

	public int getMinSafePos() {
		final double magicPi = 248;
		double offset = 0;
		var elevatorBottomDiff = elevator.getTarget() - Elevator.ElevatorPosition.BOTTOM.value;
		var aNumber = Math.cos(elevatorBottomDiff / magicPi);
		safety_minFourbarAngle = (-Constants.FOURBAR_MIN_ANGLE * aNumber + Constants.FOURBAR_MIN_ANGLE);

//		if (fourbarTop.getTopCurrentPosition() < 1000){
//			offset = 700;
//		} else if (Robot.fourbar.getTopCurrentPosition() < 1500){
//			offset = 400;
//		}


		//(-(-0.0146 * Math.pow(Robot.elevator.getTargetPosition(), 2)) - (16.5 * Robot.elevator.getTargetPosition() - 6000)) / 2 + 100;

		safety_minFourbarPos = OscarMath.map(safety_minFourbarAngle, Constants.FOURBAR_MIN_ANGLE, 0, 0, Elevator.ElevatorPosition.MIDDLE.value) + offset;
		return (int) safety_minFourbarPos;
	}

	public void moveManual() {
		var mappedSlider = getSliderTarget(getSlider());
		fourbarTop.setPosition(mappedSlider);
	}

	public double getSlider() {
		return RobotContainer.stratComInterface.getRightSlider();
	}

	public double getSliderTarget (double slider) {
		return OscarMath.map(slider, -1.0, 1.0, FourbarPosition.MANUAL_BOTTOM.value, FourbarPosition.TOP.value);
	}

	public void setPosition(FourbarPosition position) {
		fourbarTop.setPosition(position.value);
	}

	public boolean atTarget() {
		return Math.abs(fourbarTop.getTargetPosition() - fourbarTop.getSensorPosition()) <= 75;
	}

	public double armDeg() {
		return OscarMath.map(fourbarTop.getSensorPosition(), FourbarPosition.BOTTOM.value, FourbarPosition.TOP.value, Constants.FOURBAR_MIN_ANGLE, Constants.FOURBAR_MAX_ANGLE);
	}

	public double armFF() {
		final double gravFF = .10;
		return gravFF * Math.cos(Math.toRadians(armDeg()));
	}

	@Override
	public String getDashboardTabName () {
		return m_name;
	}

	@Override
	public void updateDashboardData() {
		dashboard_RawPos.setDouble(getRawPosition());
		dashboard_minSafeAngle.setDouble(safety_minFourbarAngle);
		dashboard_minSafePos.setDouble(safety_minFourbarPos);
		dashboard_DesiredPos.setDouble(getSliderTarget(getSlider()));
		dashboard_AppliedPower.setDouble(fourbarTop.get());
	}

	public void zeroEncoder() {
		fourbarTop.resetSensor();
	}

	public enum FourbarPosition {
		BOTTOM(0),
		MANUAL_BOTTOM(BOTTOM.value + 100),
		MIDDLE(2600),
		TOP(5000),
		INTAKEHATCH(MIDDLE.value),
		INTAKECARGO(3100),
		CARGOSHIP_HATCH(MIDDLE.value),
		CARGOSHIP_CARGO(3600),
		ROCKETHATCH_LOW(MIDDLE.value),
		ROCKETHATCH_MID(TOP.value),
		ROCKETHATCH_HIGH(TOP.value),
		ROCKETCARGO_LOW(MIDDLE.value),
		ROCKETCARGO_MID(4500),
		ROCKETCARGO_HIGH(4500);

		public final int value;

		FourbarPosition(int value) {
			this.value = value;
		}
	}
}
