package frc.team832.robot.Commands.FourBar;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.OI;
import frc.team832.robot.Robot;

public class MoveFourbar extends Command {

	boolean isSafe = false;
	final double CONVERSION = 10;

	public MoveFourbar(){
		requires(Robot.fourbar);
	}

	public void initialize(){
		double fourbarPos = Math.abs(Robot.fourbar.getTopCurrentPosition()), elevatorPos = Robot.elevator.getCurrentPosition() * CONVERSION;
		if (fourbarPos * elevatorPos > 2) {
			isSafe = false;
		} else {
			isSafe = true;
		}
	}

	public void execute(){
		if (isSafe) {
			Robot.fourbar.setMotionPosition(OI.operatorBox.getX());
		}

	}

	@Override
	protected boolean isFinished() {
		return false;
	}

}
