package frc.team832.robot.commands.AutomaticScoring;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.ScorePosition;
import frc.team832.robot.subsystems.*;
import frc.team832.robot.subsystems.Fourbar.*;
import frc.team832.robot.subsystems.Elevator.*;


public class MoveToHatchScorePosition extends CommandBase {

	private final Elevator elevator;
	private final Fourbar fourbar;
	private final SuperStructure superStructure;
	private final SuperStructure.SuperStructurePosition position;


	public MoveToHatchScorePosition(SuperStructure.SuperStructurePosition position, SuperStructure superStructure_, Elevator elevator_, Fourbar fourbar_) {
		this.elevator = elevator_;
		this.fourbar = fourbar_;
		this.superStructure = superStructure_;
		this.position = position;
		addRequirements(elevator);
		addRequirements(fourbar);
	}

	@Override
	public void initialize () {
		superStructure.setPosition(position);
	}

	@Override
	public void execute () {

	}

	@Override
	public boolean isFinished () {
		return superStructure.atTarget();
	}

	@Override
	public void end (boolean interrupted) {

	}

	//TODO: add some garbage here that moves the arm to the correct position based on the button pressed
}
