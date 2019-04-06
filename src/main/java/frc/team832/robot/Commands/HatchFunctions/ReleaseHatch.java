package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Elevator;
import frc.team832.robot.Subsystems.SnowBlower;

import java.awt.*;


public class ReleaseHatch extends Command {
    boolean isStalled;
    public ReleaseHatch(){ }

    public void initialize() {
        isStalled =  Robot.snowBlower.hasHatch();
        Robot.snowBlower.setHatchHolderPower(-1.0);
        if (!isStalled) {
            Robot.snowBlower.setLEDs(SnowBlower.LEDMode.HATCH_INTAKE);
        }
    }

    public void execute(){
        isStalled =  Robot.snowBlower.hasHatch();
    }

    @Override
    protected boolean isFinished() {
        return isStalled;
    }

    @Override
    protected void end(){
        if (isStalled) {
            Robot.snowBlower.setLEDs(SnowBlower.LEDMode.HATCH_ACQUIRED);
        }
    }
}
