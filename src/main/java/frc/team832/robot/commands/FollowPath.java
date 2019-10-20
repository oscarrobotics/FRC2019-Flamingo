package frc.team832.robot.commands;

import jaci.pathfinder.Trajectory;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.subsystems.Drivetrain;

public class FollowPath extends CommandBase {
    private Drivetrain drivetrain;
    private Trajectory leftTraj, rightTraj;

    public FollowPath(Drivetrain drivetrain, Trajectory leftTraj, Trajectory rightTraj) {
        super();
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);

        this.leftTraj = leftTraj;
        this.rightTraj = rightTraj;
    }

    @Override
    public void initialize() {
        drivetrain.preparePathFollower(leftTraj, rightTraj);
    }

    @Override
    public void end(boolean isInterrupted) {
        drivetrain.stopPathing();
    }

    @Override
    public boolean isFinished() {
        return !drivetrain.isPathing();
    }

}
