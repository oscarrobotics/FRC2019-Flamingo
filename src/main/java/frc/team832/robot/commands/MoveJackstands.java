package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.subsystems.Jackstand;
import frc.team832.robot.subsystems.Jackstand.JackstandPosition;

public class MoveJackstands extends CommandBase {
	private final Jackstand subsystem;
	private final JackstandPosition position;


	public MoveJackstands(Jackstand subsystem, JackstandPosition position) {
		this.subsystem = subsystem;
		this.position = position;
		addRequirements(subsystem);
	}

	public void initialize() {
		subsystem.setPosition(position);
	}

	@Override
	public void execute() {
	}

	public boolean isFinished(){
		return subsystem.backAtTarget(100) && subsystem.frontAtTarget(100);
	}

	@Override
	public void end(boolean interrupted) {
	}
}