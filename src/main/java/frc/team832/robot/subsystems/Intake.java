package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol.vendor.CANVictor;
import frc.team832.robot.Constants;

public class Intake extends SubsystemBase {

	private CANVictor hatchIntake, cargoIntake;

	public Intake() {
		super();
		SmartDashboard.putData("Intake Subsys", this);
	}

	@Override
	public void periodic() {}

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
	}

	public void stopCargo() {
		cargoIntake.set(0.0);
	}

	public void runHatch(HatchDirection direction) {
		hatchIntake.set(direction.power);
	}

	public void stopHatch() {
		hatchIntake.set(0.0);
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
}