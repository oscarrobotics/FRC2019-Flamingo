package frc.team832.robot.commands.AutomaticScoring;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team832.robot.subsystems.Intake;

public class HatchStop extends InstantCommand {

	private final Intake subsystem;

	public HatchStop(Intake subsys){
		subsystem = subsys;
		addRequirements(subsystem);
	}

	public void initialize(){
		subsystem.stopHatch();
	}
}
