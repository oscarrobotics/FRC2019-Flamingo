package frc.team832.robot.commands.automaticDriving;

import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.team832.robot.Constants;
import frc.team832.robot.subsystems.Drivetrain;

public class DriveOnPath extends RamseteCommand {
    public DriveOnPath(Trajectory trajectory, Drivetrain drivetrain) {
        super(trajectory, drivetrain::getLatestPose2d, new RamseteController(2, 0.7), Constants.DRIVE_KINEMATICS, drivetrain::consumeWheelSpeeds, drivetrain);
    }
}
