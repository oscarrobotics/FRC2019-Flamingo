package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;


public class ReleaseHatch extends Command {


    public ReleaseHatch(){
    }

    public void initialize(){
        Robot.snowBlower.setHatchHolderOpen(false);
    }

    public void execute(){

    }

    @Override
    protected boolean isFinished() {
        return Math.abs(Robot.snowBlower.getHoldorTargetPosition() - Robot.snowBlower.getHoldorCurrentPosition()) <=20;
    }
}
