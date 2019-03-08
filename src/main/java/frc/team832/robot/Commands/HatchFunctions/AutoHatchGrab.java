package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.GrouchLib.Sensors.CANifier;
import frc.team832.robot.Robot;
import frc.team832.robot.RobotMap;

import java.awt.*;

public class AutoHatchGrab extends Command {

    long startTime;

    public AutoHatchGrab(){

    }

    public void initialize(){
        Robot.currentHatchState = Robot.AutoHatchState.Grabbing;
        Robot.snowBlower.setHatchHolderPower(.75);
        startTime = System.currentTimeMillis();
    }

    public void execute(){

    }

    @Override
    protected boolean isFinished() {
        return startTime+500 <= System.currentTimeMillis() ||
                (Robot.interruptedHatchState != Robot.AutoHatchState.None && Robot.interruptedHatchState != Robot.AutoHatchState.Grabbing);
    }

    @Override
    protected void end(){
        Robot.snowBlower.setHatchHolderPower(0.0);
        Robot.currentHatchState = Robot.AutoHatchState.MovingElevator;
    }
}
