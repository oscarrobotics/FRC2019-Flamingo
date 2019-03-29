package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.SnowBlower;

public class GrabHatch extends Command {

    public void initialize(){

        Robot.interruptedHatchState = Robot.AutoHatchState.None;
        Robot.snowBlower.setHatchHolderPower(1.0);
    }

    public void execute(){

    }

    @Override
    protected boolean isFinished() {
        if (Robot.snowBlower.isMotorStall(6,15.0,1))
            return true;
        return false;
    }

    @Override
    protected void end(){
        Robot.snowBlower.stopHatchHolder();
    }
}
