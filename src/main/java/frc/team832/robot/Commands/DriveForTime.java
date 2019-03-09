package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;

public class DriveForTime extends Command {

    private int _vel, _time;
    private long startTime;

    public DriveForTime(int vel, int timeInMs) {
        requires(Robot.drivetrain);
        _time = timeInMs;
        _vel = vel;
    }

    @Override
    protected void initialize() {
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void execute() {
        Robot.drivetrain.setVelocity(_vel);
        System.out.println("DriveForTime ex");
    }

    @Override
    protected boolean isFinished() {
        return (startTime+_time) <= System.currentTimeMillis();
    }

    @Override
    protected void end() {
        Robot.drivetrain.setVelocity(0);
    }

    @Override
    protected void interrupted() {
        super.interrupted();
    }
}
