package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team832.robot.subsystems.Jackstand;
import frc.team832.robot.subsystems.Jackstand.JackstandPosition;

public class MoveJackstands extends InstantCommand {
	private final Jackstand subsystem;
	private final JackstandPosition position;


	public MoveJackstands(JackstandPosition position, Jackstand subsystem) {
		this.subsystem = subsystem;
		this.position = position;
		addRequirements(subsystem);
	}

	@Override
	public void initialize() {
		subsystem.setPosition(position);
	}

	@Override
	public void end(boolean interrupted) {
	}
}