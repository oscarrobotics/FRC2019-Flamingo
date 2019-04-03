package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.OI;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Elevator;
import frc.team832.robot.Subsystems.Fourbar;


public class MoveComplexLiftWithAdjust extends Command {

	private double _fourbarPos, _elevatorPos;

	public MoveComplexLiftWithAdjust (String fourbarPos, String elevatorPos) {
		requires(Robot.fourbar);
		requires(Robot.elevator);

		_fourbarPos = Fourbar.Constants.Positions.getByIndex(fourbarPos).getTarget();
		_elevatorPos = Elevator.Constants.Positions.getByIndex(elevatorPos).getTarget();
	}

	@Override
	protected void initialize () {
		Robot.elevator.setMotionPosition(_elevatorPos);
		Robot.fourbar.setMotionPosition(_fourbarPos, Robot.fourbar.armFF());
	}

	@Override
	protected void execute () {
		double elevatorAdj;
		double fourbarAdj;
		if (OI.singleSwitch.get()) {
			fourbarAdj = Fourbar.Constants.TOP_MAX_VAL / 10 * (OI.operatorBox.getX());
			elevatorAdj = Elevator.Constants.POT_RANGE / 10 /* 75*/ * (OI.operatorBox.getY());
		} else {
			elevatorAdj = 0;
			fourbarAdj = 0;
		}
		Robot.elevator.setMotionPosition(_elevatorPos + elevatorAdj);
		Robot.fourbar.setMotionPosition(_fourbarPos + fourbarAdj, Robot.fourbar.armFF());
	}

	@Override
	protected boolean isFinished () {
		return Robot.elevator.atTargetPosition() && Robot.fourbar.atTargetPosition();
	}

	@Override
	protected void end () {
	}

	@Override
	protected void interrupted () {
		super.interrupted();
	}
}
