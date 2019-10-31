package frc.team832.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.lib.driverstation.dashboard.DashboardManager;
import frc.team832.lib.driverstation.dashboard.DashboardUpdatable;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol.vendor.CANTalon;
import frc.team832.lib.util.OscarMath;
import frc.team832.robot.Constants;
import frc.team832.robot.Robot;
import frc.team832.robot.RobotContainer;

import static frc.team832.robot.RobotContainer.jackstand;
import static frc.team832.robot.RobotContainer.superStructure;

public class Fourbar extends SubsystemBase implements DashboardUpdatable {
	private static final double CHEESY_NUMBER = 800/Math.PI;

	private CANTalon fourbarTop, fourbarBottom;

	private NetworkTableEntry dashboard_AppliedPower;
	private NetworkTableEntry dashboard_DesiredPos;
	private NetworkTableEntry dashboard_RawPos;
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
//		if (jackstand.isExtending())
//			superStructure.handleFourbarClimbCorrection();
	}

	public boolean initialize() {
		boolean successful = true;
		fourbarTop = new CANTalon(Constants.FOURBARTOP_CAN_ID);
		fourbarBottom = new CANTalon(Constants.FOURBARBOTTOM_CAN_ID);

		if (!(fourbarTop.getInputVoltage() > 0)) successful = false;
		if (!(fourbarBottom.getInputVoltage() > 0)) successful = false;

		fourbarTop.setSensorType(FeedbackDevice.CTRE_MagEncoder_Relative);
		fourbarBottom.setSensorType(FeedbackDevice.CTRE_MagEncoder_Relative);

		fourbarTop.resetSettings();
		fourbarBottom.resetSettings();

		fourbarTop.setInverted(false);
		fourbarTop.setSensorPhase(true);

		fourbarTop.rezeroSensor();

		fourbarTop.setkP(Constants.ARM_PIDF[0]);
		fourbarTop.setkF(Constants.ARM_PIDF[3]);

		NeutralMode allIdleMode = NeutralMode.kCoast;
		fourbarTop.setNeutralMode(allIdleMode);
		fourbarBottom.setNeutralMode(allIdleMode);

		fourbarBottom.follow(fourbarTop);
		fourbarBottom.setInverted(true);

		dashboard_AppliedPower = DashboardManager.addTabItem(this, "Applied Power", 0.0);
		dashboard_DesiredPos = DashboardManager.addTabItem(this, "Desired Pos", 0.0);
		dashboard_RawPos = DashboardManager.addTabItem(this, "Raw Pos", 0.0);
		dashboard_currentDegrees = DashboardManager.addTabItem(this, "Arm Deg", 0.0);

		fourbarTop.setForwardSoftLimit((int)Constants.FOURBAR_SOFT_MAX);
		fourbarTop.setReverseSoftLimit((int)Constants.FOURBAR_SOFT_MIN);

		fourbarTop.configMotionMagic(Constants.FOURBAR_VELOCITY, Constants.FOURBAR_ACCELERATION);

		setPosition(FourbarPosition.STARTING_CONFIG);

		return successful;
	}

	public void setIdleMode(NeutralMode idleMode) {
		fourbarTop.setNeutralMode(idleMode);
		fourbarBottom.setNeutralMode(idleMode);
	}

	public double getRawPosition() {
		return fourbarTop.getSensorPosition();
	}

	public double getTarget() { return fourbarTop.getClosedLoopTarget(); }

	public boolean isMoving(int deadzone) {
		return fourbarBottom.getClosedLoopError() > deadzone;
	}



	public void moveManual(int targetPos) {
		targetPos = OscarMath.clip(targetPos, FourbarPosition.BOTTOM.value, FourbarPosition.TOP.value);
		fourbarTop.setMotionMagic(targetPos);
	}

	public double getSlider() {
		return RobotContainer.stratComInterface.getLeftSlider();
	}

	public double getSliderTarget(double slider) {
		return OscarMath.map(slider, -1.0, 1.0, FourbarPosition.MANUAL_BOTTOM.value, FourbarPosition.TOP.value);
	}

	public void setPosition(FourbarPosition position) {
//		double targetPos = isSafe(false) ? position.value : safety_minFourbarPos;
		fourbarTop.setMotionMagic(position.value);
	}

	public boolean atTarget() {
		return Math.abs(fourbarTop.getClosedLoopTarget() - fourbarTop.getSensorPosition()) <= 75;
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
//		dashboard_isSafe.setBoolean(isSafe(true));
		dashboard_RawPos.setDouble(getRawPosition());
		dashboard_currentDegrees.setDouble(positionToDegrees(getRawPosition()));
		dashboard_DesiredPos.setDouble(getSliderTarget(getSlider()));
//		dashboard_minSafePos.setDouble(safety_minFourbarPos);
//		dashboard_minSafeAngle.setDouble(safety_minFourbarAngle);
		dashboard_AppliedPower.setDouble(fourbarTop.get());
	}

	public void zeroEncoder() {
		fourbarTop.rezeroSensor();
	}

	public enum FourbarPosition {
		BOTTOM(0),
		MANUAL_BOTTOM(BOTTOM.value + 25),
		MIDDLE(2500),
		TOP(4800),
		STARTING_CONFIG(BOTTOM.value + 200),
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
