package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.subsystems.Intake;

public class CargoDown extends CommandBase {
	private final Intake subsystem;

	public CargoDown(Intake subsystem) {
		this.subsystem = subsystem;
		addRequirements(subsystem);
	}

	public void initialize() {
		subsystem.cargoDown(1.0);
	}

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interrupted) {
		subsystem.stopCargo();
	}
}
