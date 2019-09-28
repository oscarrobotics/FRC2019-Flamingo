package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.motorcontrol.CANVictor;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;

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
		hatchIntake = new CANVictor(13);
		cargoIntake = new CANVictor(12);
		if (!(hatchIntake.getInputVoltage() > 0)) successful = false;
		if (!(cargoIntake.getInputVoltage() > 0)) successful = false;

		NeutralMode allIdleMode = NeutralMode.kCoast;
		hatchIntake.setNeutralMode(allIdleMode);
		cargoIntake.setNeutralMode(allIdleMode);

		return successful;
	}

	public void cargoUp(double power) {
		cargoIntake.set(power);
	}

	public void cargoDown(double power) {
		cargoIntake.set(power);
	}

	public void stopCargo() {
		cargoIntake.set(0);
	}

	public void hatchIn(double power) {
		hatchIntake.set(power);
	}

	public void hatchOut(double power) {
		hatchIntake.set(power);
	}

	public void stopHatch() {
		hatchIntake.set(0);
	}
}