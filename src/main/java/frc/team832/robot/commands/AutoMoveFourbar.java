package frc.team832.robot.commands;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team832.robot.subsystems.Fourbar;

public class AutoMoveFourbar extends InstantCommand {
	private final Fourbar fourbar;
	private final Fourbar.FourbarPosition target;

	public AutoMoveFourbar(Fourbar subsystem, Fourbar.FourbarPosition position) {
		this.fourbar = subsystem;
		this.target = position;
		addRequirements(fourbar);
	}

	@Override
	public void initialize() {
		fourbar.setPosition(target);
	}

	@Override
		public void end(boolean interrupted) {
	}

}
