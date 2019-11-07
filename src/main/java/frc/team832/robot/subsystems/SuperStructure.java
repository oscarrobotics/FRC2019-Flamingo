package frc.team832.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.lib.driverstation.dashboard.DashboardManager;
import frc.team832.lib.driverstation.dashboard.DashboardUpdatable;
import frc.team832.lib.driverstation.dashboard.DashboardWidget;
import frc.team832.lib.util.OscarMath;
import frc.team832.robot.Constants;
import frc.team832.robot.LEDs;
import frc.team832.robot.RobotContainer;
import frc.team832.robot.subsystems.Elevator.ElevatorPosition;
import frc.team832.robot.subsystems.Fourbar.FourbarPosition;

public class SuperStructure extends SubsystemBase implements DashboardUpdatable {

	private static final double CHEESY_NUMBER = 800/Math.PI;

	private final Fourbar fourbar;
	private final Elevator elevator;

	private NetworkTableEntry dashboard_minSafeAngle, dashboard_minSafePos, dashboard_isSafe, dashboard_isPosSafe, dashboard_isTargetSafe;

	private NetworkTableEntry dashboard_fourbarTarget, dashboard_elevatorTarget;

	private double safety_minSafeAngle;
	private int safety_minSafePos;
	private boolean safety_isSafe;
	private boolean safety_isPosSafe;
	private boolean safety_isTargetSafe;

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
		if (isMoving()){
			LEDs.setLEDs(LEDs.LEDMode.ARM_MOVING);
		} else if (LEDs.getLEDMode() == LEDs.LEDMode.ARM_MOVING){
			LEDs.setLEDs(LEDs.LEDMode.DEFAULT);
		}

		if (elevator.isMovingDown()) {
			elevator.setMotionMagicSlow();
		} else {
			elevator.setMotionMagicNormal();
		}


	}

	public boolean initialize(boolean prereqPassed) {
		boolean success = false;
		if (prereqPassed) success = true;

		dashboard_minSafeAngle = DashboardManager.addTabItem(this, "Min Safe Arm Angle", 0.0);
		dashboard_minSafePos = DashboardManager.addTabItem(this, "Min Safe Arm Pos", 0.0);
		dashboard_isPosSafe = DashboardManager.addTabItem(this, "Is Arm Pos Safe", false, DashboardWidget.BooleanBox);
		dashboard_isTargetSafe = DashboardManager.addTabItem(this, "Is Arm Target Safe", false, DashboardWidget.BooleanBox);
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

	public boolean isMoving() {
		return elevator.isMoving(30) || fourbar.isMoving(200);
	}

	public void handleFourbarClimbCorrection() {
		moveFourbarManual(getClimbHeight());
	}

	public boolean atTarget() {
		return elevator.atTarget() && fourbar.atTarget();
	}

	private int fourbarSliderTarget() {
		double slider = RobotContainer.stratComInterface.getLeftSlider();
		return (int) OscarMath.map(slider, -1.0, 1.0, safety_minSafePos, FourbarPosition.TOP.value);
	}

	private int elevatorSliderTarget() {
		double slider = RobotContainer.stratComInterface.getRightSlider();
		return (int) OscarMath.map(slider, -1.0, 1.0, ElevatorPosition.BOTTOM.value, ElevatorPosition.TOP.value);
	}

	private int getIntersectionOffset() {
		int baseOffset = 0;
		double offsetMultiplier;
		if (elevator.isMoving(10)) {
			if (elevator.isMovingDown())
				offsetMultiplier = -5;
			else
				offsetMultiplier = -2;
		} else {
			offsetMultiplier = -2.25;
		}

		if (fourbar.getTarget() <= 200)
			return baseOffset;
		else
			return (int) (safety_minSafeAngle * offsetMultiplier) + baseOffset;
	}

	public boolean getIsSafe() {
		return safety_isSafe;
	}

	public double getMinSafePos() {
		return safety_minSafePos;
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
		safety_isPosSafe = !isEnabled || !(fourbar.getPosition() + 50 < safety_minSafePos);
		safety_isTargetSafe = fourbarTarget + 10 > safety_minSafePos;
		safety_isSafe = (safety_isTargetSafe);
	}



	private void checkFourbarMinSafeAngle(double elevatorTarget) {
		double unadjustedAngle = Math.cos( (elevatorTarget - Elevator.ElevatorPosition.BOTTOM.value) / CHEESY_NUMBER);
		safety_minSafeAngle = ( Math.abs(Constants.FOURBAR_MIN_ANGLE) * unadjustedAngle) + Constants.FOURBAR_MIN_ANGLE;
		safety_minSafePos = (int) fourbar.degreesToPosition(safety_minSafeAngle);
	}

	private void manageFourbarSafety() {
		if (DriverStation.getInstance().isEnabled()) {
			checkFourbarMinSafeAngle(elevator.getPosition());
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

		dashboard_isPosSafe.setBoolean(safety_isPosSafe);
		dashboard_isTargetSafe.setBoolean(safety_isTargetSafe);
		dashboard_isSafe.setBoolean(safety_isSafe);
		dashboard_minSafeAngle.setDouble(safety_minSafeAngle);
		dashboard_minSafePos.setDouble(safety_minSafePos);
	}

	@SuppressWarnings("unused")
    public enum SuperStructurePosition {
		INTAKEHATCH(ElevatorPosition.INTAKEHATCH, FourbarPosition.INTAKEHATCH),
		INTAKECARGO(ElevatorPosition.INTAKECARGO, FourbarPosition.INTAKECARGO),
		CARGOSHIP_HATCH(ElevatorPosition.CARGOSHIP_HATCH, FourbarPosition.CARGOSHIP_HATCH),
		CARGOSHIP_CARGO(ElevatorPosition.CARGOSHIP_CARGO, FourbarPosition.CARGOSHIP_CARGO),
		ROCKETHATCH_LOW(ElevatorPosition.ROCKETHATCH_LOW, FourbarPosition.ROCKETHATCH_LOW),
		ROCKETHATCH_MID(ElevatorPosition.ROCKETHATCH_MID, FourbarPosition.ROCKETHATCH_MID),
		ROCKETHATCH_HIGH(ElevatorPosition.ROCKETHATCH_HIGH, FourbarPosition.ROCKETHATCH_HIGH),
		ROCKETCARGO_LOW(ElevatorPosition.ROCKETCARGO_LOW, FourbarPosition.ROCKETCARGO_LOW),
		ROCKETCARGO_MID(ElevatorPosition.ROCKETCARGO_MID, FourbarPosition.ROCKETCARGO_MID),
		ROCKETCARGO_HIGH(ElevatorPosition.ROCKETCARGO_HIGH, FourbarPosition.ROCKETCARGO_HIGH),
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
