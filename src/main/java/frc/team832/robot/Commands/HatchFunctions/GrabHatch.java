package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.SnowBlower;

public class GrabHatch extends Command {

    public void initialize(){
        Robot.snowBlower.resetStall();
        Robot.interruptedHatchState = Robot.AutoHatchState.None;
        Robot.snowBlower.setHatchHolderPower(1.0);
    }

    public void execute(){
        Robot.snowBlower.setLEDs(SnowBlower.LEDMode.HATCH_RELEASE);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end(){
        Robot.snowBlower.stopHatchHolder();
        Robot.snowBlower.setLEDs(SnowBlower.LEDMode.STATIC, SnowBlower.Constants.Colors.DEFAULT);
    }
}
