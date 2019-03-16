package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;

import java.awt.*;


public class ReleaseHatch extends Command {


    public ReleaseHatch(){
    }

    public void initialize(){
        Robot.snowBlower.setHatchHolderPower(-.75);
    }

    public void execute(){
        Robot.snowBlower.setHatchHolderPower(-.75);
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
