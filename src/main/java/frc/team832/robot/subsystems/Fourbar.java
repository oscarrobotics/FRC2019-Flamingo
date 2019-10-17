package frc.team832.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.GrouchLib.driverstation.dashboard.DashboardManager;
import frc.team832.GrouchLib.driverstation.dashboard.DashboardUpdatable;
import frc.team832.GrouchLib.driverstation.dashboard.DashboardWidget;
import frc.team832.GrouchLib.motorcontrol.CANTalon;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;
import frc.team832.GrouchLib.util.OscarMath;
import frc.team832.robot.Constants;
import frc.team832.robot.RobotContainer;

import static frc.team832.robot.RobotContainer.elevator;

public class Fourbar extends SubsystemBase implements DashboardUpdatable {
	private static final double CHEESY_NUMBER = 800/Math.PI;

	private CANTalon fourbarTop, fourbarBottom;

	private NetworkTableEntry dashboard_AppliedPower;
	private NetworkTableEntry dashboard_DesiredPos;
	private NetworkTableEntry dashboard_RawPos;
	private NetworkTableEntry dashboard_minSafeAngle;
	private NetworkTableEntry dashboard_minSafePos;
	private NetworkTableEntry dashboard_isSafe;
	private NetworkTableEntry dashboard_currentDegrees;

	private double safety_minFourbarAngle;
	private double safety_minFourbarPos;

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
//			_top.getMotor().setMotionMagcArbFF(checkMinSafePos(), armFF());
//		} else {
//			_top.getMotor().setMotionMagcArbFF(targetPos, armFF());
//		}
//
//		if (isMovingDown()){
//			_top.getMotor().configMotionMagic(Constants.MOTION_MAGIC_VEL / 2,Constants.MOTION_MAGIC_ACC / 2);
//		} else {
//			_top.getMotor().configMotionMagic(Constants.MOTION_MAGIC_VEL,Constants.MOTION_MAGIC_ACC);
//		}

		if (!isSafe(false)) {
			fourbarTop.setPosition(safety_minFourbarPos);
		}
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

		fourbarTop.resetSensor();

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
		dashboard_isSafe = DashboardManager.addTabItem(this, "Is Safe", false, DashboardWidget.BooleanBox);
		dashboard_currentDegrees = DashboardManager.addTabItem(this, "Arm Deg", 0.0);

		fourbarTop.setForwardSoftLimit((int)Constants.FOURBAR_SOFT_MAX);
		fourbarTop.setReverseSoftLimit((int)Constants.FOURBAR_SOFT_MIN);

		return successful;
	}

	private boolean isSafe(boolean isTelemetry) {
		boolean isSafe;
		checkMinSafePos(isTelemetry);
		isSafe = !(fourbarTop.getTargetPosition() < safety_minFourbarPos);
		return isSafe;
	}

	public double getRawPosition() {
		return fourbarTop.getSensorPosition();
	}

	private void checkMinSafePos(boolean isTelemetry) {
		final double intersectionOffset = 100;

		double elevatorTarget = isTelemetry ? elevator.getSliderTarget() : elevator.getTarget();
		double unadjustedAngle = Math.cos( (elevatorTarget - Elevator.ElevatorPosition.BOTTOM.value) / CHEESY_NUMBER);

		safety_minFourbarAngle = ( Math.abs(Constants.FOURBAR_MIN_ANGLE) * unadjustedAngle) + Constants.FOURBAR_MIN_ANGLE;

		safety_minFourbarPos = OscarMath.map(safety_minFourbarAngle, Constants.FOURBAR_MIN_ANGLE, 0, 0, FourbarPosition.MIDDLE.value) + intersectionOffset;
	}

	public void moveManual() {
		double targetPos = isSafe(false) ? getSliderTarget(getSlider()) : safety_minFourbarPos;
		fourbarTop.setPosition(targetPos);
	}

	public double getSlider() {
		return RobotContainer.stratComInterface.getLeftSlider();
	}

	public double getSliderTarget(double slider) {
		return OscarMath.map(slider, -1.0, 1.0, FourbarPosition.MANUAL_BOTTOM.value, FourbarPosition.TOP.value);
	}

	public void setPosition(FourbarPosition position) {
		double targetPos = isSafe(false) ? position.value : safety_minFourbarPos;
		fourbarTop.setPosition(targetPos);
	}

	public boolean atTarget() {
		return Math.abs(fourbarTop.getTargetPosition() - fourbarTop.getSensorPosition()) <= 75;
	}

	public double degreesToPosition(double degrees) {
		return OscarMath.map(degrees, Constants.FOURBAR_MIN_ANGLE, Constants.FOURBAR_MAX_ANGLE, FourbarPosition.BOTTOM.value, FourbarPosition.TOP.value);
	}

	public double positionToDegrees(double position) {
		return OscarMath.map(position, FourbarPosition.BOTTOM.value, FourbarPosition.TOP.value, Constants.FOURBAR_MIN_ANGLE, Constants.FOURBAR_MAX_ANGLE);
	}

	@Override
	public String getDashboardTabName () {
		return m_name;
	}

	@Override
	public void updateDashboardData() {
		dashboard_isSafe.setBoolean(isSafe(true));
		dashboard_RawPos.setDouble(getRawPosition());
		dashboard_currentDegrees.setDouble(positionToDegrees(getRawPosition()));
		dashboard_DesiredPos.setDouble(getSliderTarget(getSlider()));
		dashboard_minSafePos.setDouble(safety_minFourbarPos);
		dashboard_minSafeAngle.setDouble(safety_minFourbarAngle);
		dashboard_AppliedPower.setDouble(fourbarTop.get());
	}

	public void zeroEncoder() {
		fourbarTop.resetSensor();
	}

	public enum FourbarPosition {
		BOTTOM(0),
		MANUAL_BOTTOM(BOTTOM.value + 100),
		MIDDLE(2600),
		TOP(4760),
//		TOP(5000), // Before gas shocks
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
