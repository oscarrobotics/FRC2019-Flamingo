package frc.team832.robot.Commands.TheBigOne;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.OI;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Elevator;
import frc.team832.robot.Subsystems.Fourbar;

import static frc.team832.robot.Robot.elevator;
import static frc.team832.robot.Robot.fourbar;

public class DefaultPosition extends Command {
	private OI.ThreeSwitchPos _mode;
	private boolean _isDefaultInvert;
	public DefaultPosition(boolean isDefaultInvert){
		requires(Robot.complexLift);
		_isDefaultInvert = isDefaultInvert;
	}

	public void initialize(){
		_mode = Robot.ThreeSwitchPos;
	}

	public void execute(){
		double elevatorPos = Robot.elevator.getTargetPosition(), fourbarPos = Robot.fourbar.getTopTargetPosition();
		switch (_mode){
			case UP:
				if (_isDefaultInvert) {
					fourbarPos = Fourbar.Constants.TOP_MIN_VAL;
					elevatorPos = Elevator.Constants.POT_TOP_VALUE;
				} else {
					fourbarPos = Fourbar.Constants.TOP_MAX_VAL;
					elevatorPos = Elevator.Constants.POT_BOTTOM_VALUE;
				}
				break;
			case DOWN:
				break;
			case OFF:
				break;
		}
		fourbar.setMotionPosition(fourbarPos, fourbar.armFF());
		elevator.setMotionPosition(elevatorPos);
	}

	@Override
	protected boolean isFinished () {
		return fourbar.atTargetPosition() && elevator.atTargetPosition();
	}
}
