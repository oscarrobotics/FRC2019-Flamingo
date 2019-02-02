package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.TheBigOne;

public class MoveTheBigOne extends InstantCommand {

	TheBigOne.Action _action;

	public MoveTheBigOne(TheBigOne.Action _action) {
		requires(Robot.snowBlower);
	}

	/**
	 * The initialize method is called just before the first time
	 * this Command is run after being started.
	 */
	@Override
	protected void initialize() {
		Robot.theBigOne.setAction(_action);
	}
}
