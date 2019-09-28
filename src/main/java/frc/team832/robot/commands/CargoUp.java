package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.subsystems.Intake;

public class CargoUp extends CommandBase {
	private final Intake subsystem;

	public CargoUp(Intake subsystem) {
		this.subsystem = subsystem;
		addRequirements(subsystem);
	}

	public void initialize() {
		subsystem.cargoUp(-1.0);
	}

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interrupted) {
		subsystem.stopCargo();
	}
}
