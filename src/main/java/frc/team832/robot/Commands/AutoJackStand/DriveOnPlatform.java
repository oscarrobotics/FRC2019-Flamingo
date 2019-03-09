package frc.team832.robot.Commands.AutoJackStand;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;


public class DriveOnPlatform extends Command {

    public DriveOnPlatform() {
        requires(Robot.jackStands);
    }

    @Override
    protected void initialize() {
        Robot.jackStands.setDrivePow(.9);
    }

    @Override
    protected void execute() { }

    @Override
    protected boolean isFinished() {
        //figure out reflectance sensor logic
        return false;
    }

    @Override
    protected void end() {
        Robot.jackStands.setDrivePow(0.0);
    }

    @Override
    protected void interrupted() {
        super.interrupted();
    }
}
