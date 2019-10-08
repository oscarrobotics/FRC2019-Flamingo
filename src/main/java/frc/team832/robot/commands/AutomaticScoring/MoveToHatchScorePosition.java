package frc.team832.robot.commands.AutomaticScoring;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.subsystems.Intake;

public class MoveToHatchScorePosition extends CommandBase {

	private final Intake subsystem;


	public MoveToHatchScorePosition(Intake subsystem_){
		subsystem = subsystem_;
		addRequirements(subsystem);
	}

	//TODO: add some garbage here that moves the arm to the correct position based on the button pressed
}
