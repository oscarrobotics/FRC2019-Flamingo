package frc.team832.robot.commands.AutomaticScoring;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team832.robot.ScorePosition;
import frc.team832.robot.commands.HatchOut;
import frc.team832.robot.subsystems.Drivetrain;
import frc.team832.robot.subsystems.Elevator;
import frc.team832.robot.subsystems.Fourbar;
import frc.team832.robot.subsystems.Intake;

public class AutomaticHatchScoring extends SequentialCommandGroup {

	private final Drivetrain drivetrain;
	private final Elevator elevator;
	private final Fourbar fourbar;
	private final Intake intake;

	public AutomaticHatchScoring(Drivetrain drivetrain, Elevator elevator, Fourbar fourbar, Intake intake, ScorePosition position) {
		this.drivetrain = drivetrain;
		this.elevator = elevator;
		this.fourbar = fourbar;
		this.intake = intake;

		addCommands(new MoveToHatchScorePosition(elevator, fourbar, position),
				new DriveToTarget(),
				new HatchOut(intake),
				new WaitCommand(1),
				new HatchStop(intake));
	}

}