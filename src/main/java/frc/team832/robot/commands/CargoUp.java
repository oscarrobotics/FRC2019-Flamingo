package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.subsystems.Intake;

public class CargoUp extends CommandBase {
	private final Intake intake;

	public CargoUp(Intake intake) {
		this.intake = intake;
		addRequirements(intake);
	}

	public void initialize() {
		intake.cargoUp(-1.0);
	}

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interrupted) {
		intake.stopCargo();
	}
}
