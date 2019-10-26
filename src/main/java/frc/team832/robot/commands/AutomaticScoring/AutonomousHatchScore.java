package frc.team832.robot.commands.AutomaticScoring;

import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.*;
import frc.team832.robot.subsystems.*;

public class AutonomousHatchScore extends SequentialCommandGroup {

	private final Drivetrain drivetrain;
	private final Elevator elevator;
	private final Fourbar fourbar;
	private final Intake intake;
	private final SuperStructure superStructure;
	private final SuperStructure.SuperStructurePosition position;
	private final Trajectory trajectory;

	public AutonomousHatchScore (Trajectory trajectory, SuperStructure.SuperStructurePosition superStructurePosition, Drivetrain drivetrain, SuperStructure superStructure, Elevator elevator, Fourbar fourbar, Intake intake) {
		this.drivetrain = drivetrain;
		this.elevator = elevator;
		this.fourbar = fourbar;
		this.intake = intake;
		this.superStructure = superStructure;
		this.position = superStructurePosition;
		this.trajectory = trajectory;

		addCommands(new MoveToHatchScorePosition(position, superStructure, elevator, fourbar),
				new RamseteCommand(trajectory, drivetrain::getLatestPose2d, new RamseteController(2, 0.7), drivetrain.driveKinematics, drivetrain::consumeWheelSpeeds, drivetrain),
				new StartEndCommand(() -> intake.runHatch(Intake.HatchDirection.OUT), intake::stopHatch, intake),
				new WaitCommand(1),
				new HatchStop(intake));
	}

}