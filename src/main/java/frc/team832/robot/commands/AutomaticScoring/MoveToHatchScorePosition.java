package frc.team832.robot.commands.AutomaticScoring;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.ScorePosition;
import frc.team832.robot.subsystems.*;
import frc.team832.robot.subsystems.Fourbar.*;
import frc.team832.robot.subsystems.Elevator.*;


public class MoveToHatchScorePosition extends CommandBase {

	private final Elevator elevator;
	private final Fourbar fourbar;
	private final ScorePosition position;
	private FourbarPosition fourbarPos;
	private ElevatorPosition elevatorPos;

	public MoveToHatchScorePosition(Elevator elevator_, Fourbar fourbar_, ScorePosition position){
		elevator = elevator_;
		fourbar = fourbar_;
		this.position = position;
		addRequirements(elevator);
		addRequirements(fourbar);
	}

	@Override
	public void initialize () {
		switch (position) {
			case High:
				elevatorPos = ElevatorPosition.ROCKETHATCH_HIGH;
				fourbarPos = FourbarPosition.ROCKETHATCH_HIGH;
				break;
			case Middle:
				elevatorPos = ElevatorPosition.ROCKETHATCH_MID;
				fourbarPos = FourbarPosition.ROCKETHATCH_MID;
				break;
			case Low:
				elevatorPos = ElevatorPosition.ROCKETHATCH_LOW;
				fourbarPos = FourbarPosition.ROCKETHATCH_LOW;
				break;
		}

		fourbar.setTarget(fourbarPos);
		elevator.setTarget(elevatorPos);
	}

	@Override
	public void execute () {

	}

	@Override
	public boolean isFinished () {
		return elevator.atTarget() && fourbar.atTarget();
	}

	@Override
	public void end (boolean interrupted) {

	}

	//TODO: add some garbage here that moves the arm to the correct position based on the button pressed
}
