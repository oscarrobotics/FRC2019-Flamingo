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

    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end(){

    }
}
