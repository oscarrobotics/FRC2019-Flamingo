package frc.team832.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.lib.driverstation.dashboard.DashboardManager;
import frc.team832.lib.driverstation.dashboard.DashboardUpdatable;
import frc.team832.lib.driverstation.dashboard.DashboardWidget;
import frc.team832.lib.util.OscarMath;
import frc.team832.robot.Constants;
import frc.team832.robot.RobotContainer;
import frc.team832.robot.subsystems.Elevator.ElevatorPosition;
import frc.team832.robot.subsystems.Fourbar.FourbarPosition;

public class SuperStructure extends SubsystemBase implements DashboardUpdatable {

	private static final double CHEESY_NUMBER = 800/Math.PI;

	private final Fourbar fourbar;
	private final Elevator elevator;

	private NetworkTableEntry dashboard_minSafeAngle, dashboard_minSafePos, dashboard_isSafe;

	private NetworkTableEntry dashboard_fourbarTarget, dashboard_elevatorTarget;

	private double safety_minSafeAngle;
	private int safety_minSafePos;
	private boolean safety_isSafe;

	public SuperStructure(Fourbar fourbar, Elevator elevator) {
		super();
		this.fourbar = fourbar;
		this.elevator = elevator;
		DashboardManager.addTab(this);
		DashboardManager.addTabSubsystem(this, this);
	}

	@Override
	public void periodic() {
		manageFourbarSafety();
	}

	public boolean initialize(boolean prereqPassed) {
		boolean success = false;
		if (prereqPassed) success = true;

		dashboard_minSafeAngle = DashboardManager.addTabItem(this, "Min Safe Arm Angle", 0.0);
		dashboard_minSafePos = DashboardManager.addTabItem(this, "Min Safe Arm Pos", 0.0);
		dashboard_isSafe = DashboardManager.addTabItem(this, "Is Arm Safe", false, DashboardWidget.BooleanBox);

		dashboard_fourbarTarget = DashboardManager.addTabItem(this, "Arm Target", 0.0);
		dashboard_elevatorTarget = DashboardManager.addTabItem(this, "Elevator Target", 0.0);

		return success;
	}

	public void setPosition(SuperStructurePosition position) {
		elevator.setPosition(position.elevatorPosition);
		fourbar.setPosition(position.fourbarPosition);
	}

	private int getClimbHeight() {
		return OscarMath.map((int)RobotContainer.jackstand.getFrontPos(), Jackstand.JackstandPosition.EXTENDED.frontValue, Jackstand.JackstandPosition.RETRACTED.frontValue, FourbarPosition.MIDDLE.value, 4000);
	}

	public void handleFourbarClimbCorrection() {
		moveFourbarManual(getClimbHeight());
	}

	public boolean atTarget() {
		return elevator.atTarget() && fourbar.atTarget();
	}

	private int fourbarSliderTarget() {
		double slider = RobotContainer.stratComInterface.getLeftSlider();
		return (int) OscarMath.map(slider, -1.0, 1.0, FourbarPosition.MANUAL_BOTTOM.value, FourbarPosition.TOP.value);
	}

	private int elevatorSliderTarget() {
		double slider = RobotContainer.stratComInterface.getRightSlider();
		return (int) OscarMath.map(slider, -1.0, 1.0, ElevatorPosition.BOTTOM.value, ElevatorPosition.TOP.value);
	}

	private int getIntersectionOffset() {
		int baseOffset = 0;
		double offsetMultiplier = -2.5;
		if (fourbar.getTarget() <= 300)
			return baseOffset;
		else
			return (int) (safety_minSafeAngle * offsetMultiplier) + baseOffset;
	}

	public void moveManual() {
		moveManual(fourbarSliderTarget(), elevatorSliderTarget());
	}

	private void moveManual(int fourbarPosition, int elevatorPosition) {
		if (!safety_isSafe) {
			fourbarPosition = safety_minSafePos + getIntersectionOffset();
		}
		fourbar.moveManual(fourbarPosition);
		elevator.moveManual(elevatorPosition);
	}

	private void moveFourbarManual(int fourbarPosition) {
		if (!safety_isSafe) {
			fourbarPosition = safety_minSafePos + getIntersectionOffset();
		}
		fourbar.moveManual(fourbarPosition);
	}

	private void checkFourbarSafety(double fourbarTarget, boolean isEnabled) {
		boolean rawPos_isSafe = !isEnabled || !(fourbar.getRawPosition() + 1 < safety_minSafePos);
		boolean targetPos_isSafe = fourbarTarget > safety_minSafePos;
		safety_isSafe = (rawPos_isSafe && targetPos_isSafe);
	}

	private void checkFourbarMinSafeAngle(double elevatorTarget) {
		double unadjustedAngle = Math.cos( (elevatorTarget - Elevator.ElevatorPosition.BOTTOM.value) / CHEESY_NUMBER);
		safety_minSafeAngle = ( Math.abs(Constants.FOURBAR_MIN_ANGLE) * unadjustedAngle) + Constants.FOURBAR_MIN_ANGLE;
		safety_minSafePos = (int) fourbar.degreesToPosition(safety_minSafeAngle);
	}

	private void manageFourbarSafety() {
		if (DriverStation.getInstance().isEnabled()) {
			checkFourbarMinSafeAngle(elevator.getTarget());
			checkFourbarSafety(fourbar.getTarget(), true);
		} else {
			checkFourbarMinSafeAngle(elevatorSliderTarget());
			checkFourbarSafety(fourbarSliderTarget(), false);
		}
	}

	@Override
	public String getDashboardTabName() {
		return m_name;
	}

	@Override
	public void updateDashboardData() {
		if (DriverStation.getInstance().isDisabled()) {
			manageFourbarSafety();
			dashboard_fourbarTarget.setDouble(fourbarSliderTarget());
			dashboard_elevatorTarget.setDouble(elevatorSliderTarget());
		} else {
			dashboard_fourbarTarget.setDouble(fourbar.getTarget());
			dashboard_elevatorTarget.setDouble(elevator.getTarget());
		}

		dashboard_isSafe.setBoolean(safety_isSafe);
		dashboard_minSafeAngle.setDouble(safety_minSafeAngle);
		dashboard_minSafePos.setDouble(safety_minSafePos);
	}

	@SuppressWarnings("unused")
    public enum SuperStructurePosition {
		INTAKEHATCH(ElevatorPosition.INTAKEHATCH, FourbarPosition.MIDDLE),
		INTAKECARGO(ElevatorPosition.BOTTOM, FourbarPosition.INTAKECARGO),
		CARGOSHIP_HATCH(ElevatorPosition.TOP, FourbarPosition.CARGOSHIP_HATCH),
		CARGOSHIP_CARGO(ElevatorPosition.BOTTOM, FourbarPosition.CARGOSHIP_CARGO),
		ROCKETHATCH_LOW(ElevatorPosition.TOP, FourbarPosition.ROCKETHATCH_LOW),
		ROCKETHATCH_MID(ElevatorPosition.BOTTOM, FourbarPosition.ROCKETHATCH_MID),
		ROCKETHATCH_HIGH(ElevatorPosition.TOP, FourbarPosition.ROCKETHATCH_HIGH),
		ROCKETCARGO_LOW(ElevatorPosition.BOTTOM, FourbarPosition.ROCKETCARGO_LOW),
		ROCKETCARGO_MIDDLE(ElevatorPosition.BOTTOM, FourbarPosition.ROCKETCARGO_MID),
		ROCKETCARGO_HIGH(ElevatorPosition.TOP, FourbarPosition.ROCKETCARGO_HIGH),
		STORAGE_DEFENCE(ElevatorPosition.TOP, FourbarPosition.BOTTOM),
		STORAGE_OFFENSE(ElevatorPosition.BOTTOM, FourbarPosition.TOP);

		public final ElevatorPosition elevatorPosition;
		public final FourbarPosition fourbarPosition;

		SuperStructurePosition(ElevatorPosition elevatorPosition, FourbarPosition fourbarPosition) {
			this.elevatorPosition = elevatorPosition;
			this.fourbarPosition = fourbarPosition;
		}
	}
}
