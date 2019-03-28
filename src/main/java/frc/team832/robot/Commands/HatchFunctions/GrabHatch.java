package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;

public class GrabHatch extends Command {

    public void initialize(){
        Robot.interruptedHatchState = Robot.AutoHatchState.None;
        Robot.snowBlower.setHatchHolderPower(1.0);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end(){
        Robot.snowBlower.stopHatchHolder();
    }
}
