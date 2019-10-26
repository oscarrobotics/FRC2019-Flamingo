package frc.team832.robot.auto;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team832.robot.Paths;

public class AutonomousComposer {

	public final StartPosition startPosition;
	public final PrimaryAction primaryAction;
	public final SecondaryAction secondaryAction;
	public final TertiaryAction tertiaryAction;

	public AutonomousComposer(StartPosition startPosition, PrimaryAction primaryAction, SecondaryAction secondaryAction, TertiaryAction tertiaryAction) {
		this(startPosition, primaryAction, secondaryAction, tertiaryAction, 0);
	}

	public AutonomousComposer(StartPosition startPosition, PrimaryAction primaryAction, SecondaryAction secondaryAction, TertiaryAction tertiaryAction, int startDelayMs) {
		this.startPosition = startPosition;
		this.primaryAction = primaryAction;
		this.secondaryAction = secondaryAction;
		this.tertiaryAction = tertiaryAction;
	}

	public SequentialCommandGroup composeCommandGroup() {
		Pose2d startPose;
		switch (startPosition) {
			case LEFT_HAB:
				startPose = Paths.LEFT_HAB_START_POSE;
				break;
			case CENTER_HAB:
				startPose = Paths.CENTER_HAB_START_POSE;
				break;
			case RIGHT_HAB:
				startPose = Paths.RIGHT_HAB_START_POSE;
				break;
		}
		return null;
	}

}
