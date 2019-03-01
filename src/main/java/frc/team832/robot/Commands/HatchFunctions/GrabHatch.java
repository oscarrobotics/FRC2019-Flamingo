package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.GrouchLib.Sensors.CANifier;
import frc.team832.robot.Robot;
import frc.team832.robot.RobotMap;

import java.awt.*;

public class GrabHatch extends Command {

    public GrabHatch(){

    }

    public void initialize(){
        Robot.snowBlower.setHatchHolderPosition("Open");
    }

    public void execute(){

    }

    @Override
    protected boolean isFinished() {
        return Math.abs(Robot.snowBlower.getHoldorTargetPosition() - Robot.snowBlower.getHoldorCurrentPosition()) <=20;
    }

    @Override
    protected void end(){
        Robot.snowBlower.setLED(Color.MAGENTA);
    }
}
