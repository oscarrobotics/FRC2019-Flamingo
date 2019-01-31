package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.SnowBlower;

public class IntakeControl extends InstantCommand {

	SnowBlower.Action _action;

	public IntakeControl(SnowBlower.Action _action) {
		requires(Robot.snowBlower);
	}

	/**
	 * The initialize method is called just before the first time
	 * this Command is run after being started.
	 */
	@Override
	protected void initialize() {
		Robot.snowBlower.setAction(_action);
	}
}
