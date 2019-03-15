package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team832.robot.Robot;


public class InterruptAcquire extends InstantCommand {
    public InterruptAcquire() {
        requires(Robot.snowBlower);
        requires(Robot.drivetrain);
        requires(Robot.elevator);
    }

    @Override
    protected void initialize() {
        Robot.interruptedHatchState = Robot.currentHatchState;
        Robot.currentHatchState = Robot.AutoHatchState.None;
    }
    @Override
    protected boolean isFinished(){
        return true;
    }
}
