package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.SnowBlower;

import java.awt.*;


public class ReleaseHatch extends Command {


    public ReleaseHatch(){
    }

    public void initialize(){
        Robot.snowBlower.setHatchHolderPower(-.75);
    }

    public void execute(){
        if (Robot.snowBlower.hasHatch()) {
            Robot.snowBlower.stopHatchHolder();
            Robot.snowBlower.setLEDs(SnowBlower.LEDMode.HATCH_ACQUIRED);
        } else {
            Robot.snowBlower.setHatchHolderPower(-1.0);
            Robot.snowBlower.setLEDs(SnowBlower.LEDMode.HATCH_RELEASE);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end(){
        Robot.snowBlower.stopHatchHolder();
        Robot.snowBlower.setLEDs(SnowBlower.LEDMode.OFF);
    }
}
