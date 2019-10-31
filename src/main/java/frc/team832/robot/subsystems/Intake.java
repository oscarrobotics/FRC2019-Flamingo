package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.lib.driverstation.dashboard.DashboardManager;
import frc.team832.lib.driverstation.dashboard.DashboardUpdatable;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol.vendor.CANVictor;
import frc.team832.lib.sensors.CANifier;
import frc.team832.lib.util.OscarMath;
import frc.team832.robot.Constants;
import frc.team832.robot.LEDs;

import static frc.team832.robot.RobotContainer.pdp;

public class Intake extends SubsystemBase implements DashboardUpdatable {

	private CANVictor hatchIntake, cargoIntake;
	private int stallCounter = 0;
	private StallState hatchStallState = StallState.NOT_STALLED;
	private boolean hasStalled;
	private boolean hasHatch;

	public Intake() {
		super();
		DashboardManager.addTab(this);
		DashboardManager.addTabSubsystem(this, this);
		SmartDashboard.putData("Intake Subsys", this);
	}

	@Override
	public void periodic() {
		hatchStallState = isMotorStall(11, 12.0, 0.5);
		hasHatch = hatchStallState == StallState.STALLED || hatchStallState == StallState.LEAVING_STALL;
	}

	public boolean initialize() {
		boolean successful = true;
		hatchIntake = new CANVictor(Constants.HATCHINTAKE_CAN_ID);
		cargoIntake = new CANVictor(Constants.CARGOINTAKE_CAN_ID);
		if (!(hatchIntake.getInputVoltage() > 0)) successful = false;
		if (!(cargoIntake.getInputVoltage() > 0)) successful = false;

		hatchIntake.setInverted(true);
		cargoIntake.setInverted(true);

		NeutralMode allIdleMode = NeutralMode.kCoast;
		hatchIntake.setNeutralMode(allIdleMode);
		cargoIntake.setNeutralMode(allIdleMode);

		return successful;
	}

	public void runCargo(CargoDirection direction) {
		cargoIntake.set(direction.power);
		if (direction == CargoDirection.DOWN)
			LEDs.setLEDs(LEDs.LEDMode.BALL_OUTTAKE);
		else
			LEDs.setLEDs(LEDs.LEDMode.BALL_INTAKE);
	}

	public void stopCargo() {
		cargoIntake.set(0.0);
		if (LEDs.getLEDMode() == LEDs.LEDMode.BALL_INTAKE)
			LEDs.setLEDs(LEDs.LEDMode.BALL_HOLD);
		else
			LEDs.setLEDs(LEDs.LEDMode.DEFAULT);
	}

	public void runHatch(HatchDirection direction) {
		hatchIntake.set(direction.power);
		if (hatchStallState != StallState.STALLED) {
			if (direction == HatchDirection.OUT)
				LEDs.setLEDs(LEDs.LEDMode.HATCH_RELEASE);
			else
				LEDs.setLEDs(LEDs.LEDMode.HATCH_INTAKE);
		}
	}

	public void stopHatch() {
		hatchIntake.set(0.0);
		if (LEDs.getLEDMode() == LEDs.LEDMode.HATCH_ACQUIRED)
			LEDs.setLEDs(LEDs.LEDMode.HATCH_HOLD);
		else
			LEDs.setLEDs(LEDs.LEDMode.DEFAULT);
	}

	@Override
	public String getDashboardTabName () {
		return m_name;
	}

	@Override
	public void updateDashboardData () {

	}

	public enum CargoDirection {
		UP(0.7),
		DOWN(-0.5);

		public final double power;

		CargoDirection(double power) {
			this.power = power;
		}
	}

	public enum HatchDirection {
		IN(1.0),
		OUT(-1.0);

		public final double power;

		HatchDirection(double power) {
			this.power = power;
		}
	}

	public enum StallState {
		STALLED,
		LEAVING_STALL,
		NOT_STALLED
	}

	public void resetStall(){
		stallCounter = 0;
		hatchStallState = StallState.NOT_STALLED;
	}

	public StallState isMotorStall (int PDPSlot, double stallCurrent, double stallSec) {
		int slowdownMultiplier = 8;
		int  stallLoops = (int)(stallSec * 20);
		stallLoops *= slowdownMultiplier;
		double motorCurrent = pdp.getChannelCurrent(PDPSlot);

		SmartDashboard.putNumber("Stall Count", stallCounter);
		SmartDashboard.putNumber("Stall Loops", stallLoops);
		if (motorCurrent >= stallCurrent) {
			stallCounter += slowdownMultiplier;
		} else if (motorCurrent < stallCurrent) {
			stallCounter--;
		}
		stallCounter = OscarMath.clip(stallCounter, 0, stallLoops + 1);
		if (stallCounter >= stallLoops) {
			hasStalled = true;
			LEDs.setLEDs(LEDs.LEDMode.HATCH_ACQUIRED);
			return StallState.STALLED;
		}
		else if (stallCounter == 0) {
			hasStalled = false;
			return StallState.NOT_STALLED;
		}
		else if (hasStalled & stallCounter < stallLoops / 4) {
			return StallState.LEAVING_STALL;
		}
		return hatchStallState;
	}
}