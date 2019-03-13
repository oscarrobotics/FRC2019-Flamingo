package frc.team832.robot.Commands.Elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.OI;
import frc.team832.robot.Robot;


public class ManualMoveElevator extends Command {
	public ManualMoveElevator () {
		requires(Robot.elevator);
	}


	protected void initialize () {
		Robot.elevator.setMotionPosition(OI.operatorBox.getY());
	}

	protected void execute () {
		Robot.elevator.setMotionPosition(OI.operatorBox.getY());
	}

	@Override
	protected boolean isFinished () {
		return false;

	}

}
