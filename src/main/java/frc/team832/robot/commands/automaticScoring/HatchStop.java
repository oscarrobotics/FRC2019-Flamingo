package frc.team832.robot.commands.automaticScoring;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team832.robot.subsystems.Intake;

public class HatchStop extends InstantCommand {

	private final Intake intake;

	public HatchStop(Intake intake){
		this.intake = intake;
		addRequirements(intake);
	}

	public void initialize(){
		intake.stopHatch();
	}
}
