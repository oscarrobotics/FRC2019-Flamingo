package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team832.robot.subsystems.Intake;

public class HatchOut extends InstantCommand {
	private final Intake subsystem;

	public HatchOut(Intake subsystem) {
		this.subsystem = subsystem;
		addRequirements(subsystem);
	}

	public void initialize() {
		subsystem.hatchOut(1.0);
	}

	@Override
	public void end(boolean interrupted) {
		subsystem.stopHatch();
	}
}
