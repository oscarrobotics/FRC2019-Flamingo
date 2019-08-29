package frc.team832.robot.commands;

import edu.wpi.first.wpilibj.frc2.command.SendableCommandBase;
import frc.team832.robot.subsystems.Intake;

public class CargoUp extends SendableCommandBase {
	public CargoUp() {
		addRequirements(Intake.getInstance());
	}

	public void initialize() {
		Intake.cargoUp(1.0);
	}

	@Override
	public void execute() {}

	@Override
	public void end(boolean interrupted) {
		Intake.stopCargo();
	}
}
