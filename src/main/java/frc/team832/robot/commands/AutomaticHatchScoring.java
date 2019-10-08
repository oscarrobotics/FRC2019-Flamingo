package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team832.robot.RobotContainer;
import frc.team832.robot.commands.AutomaticScoring.DriveToScore;
import frc.team832.robot.commands.AutomaticScoring.HatchStop;
import frc.team832.robot.commands.AutomaticScoring.MoveToHatchScorePosition;

public class AutomaticHatchScoring extends SequentialCommandGroup {

	public AutomaticHatchScoring(){
		addCommands(new MoveToHatchScorePosition(RobotContainer.intake),
					new DriveToScore(),
					new HatchOut(RobotContainer.intake),
					new WaitCommand(1),
					new HatchStop(RobotContainer.intake));
	}

}
