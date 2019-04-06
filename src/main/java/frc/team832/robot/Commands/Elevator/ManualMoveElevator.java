package frc.team832.robot.Commands.Elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.GrouchLib.Util.OscarMath;
import frc.team832.robot.OI;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Elevator;


public class ManualMoveElevator extends Command {

	public ManualMoveElevator () {
		requires(Robot.elevator);
	}

	protected void execute () {
		double input = OI.operatorBox.getY();
		double realInput = OscarMath.clipMap(input, -1, 1, 0, 1);
		double pos = OscarMath.map(realInput, 0, 1, Elevator.Constants.BOTTOM_VALUE, Elevator.Constants.TOP_VALUE);
		Robot.elevator.setMotionPosition(pos);
	}

	@Override
	protected boolean isFinished () {
		return false;
	}
}
