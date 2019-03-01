package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;

import java.awt.*;


public class ReleaseHatch extends Command {


    public ReleaseHatch(){
    }

    public void initialize(){
        Robot.snowBlower.setHatchHolderPosition("Closed");
    }

    public void execute(){

    }

    @Override
    protected boolean isFinished() {
        return Math.abs(Robot.snowBlower.getHoldorTargetPosition() - Robot.snowBlower.getHoldorCurrentPosition()) <=20;
    }

    @Override
    protected void end(){
        Robot.snowBlower.setLED(Color.BLUE);
    }
}
