package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.SnowBlower;

public class IntakeCargo extends Command {

    private double _pow;

    public IntakeCargo(double pow) {
        requires(Robot.snowBlower);
        _pow = pow;
    }

    @Override
    protected void initialize(){
        Robot.snowBlower.intakeSet(_pow);
        Robot.snowBlower.setLEDs(SnowBlower.LEDMode.BALL_INTAKE);

    }

    @Override
    protected boolean isFinished () {
        return false;
    }

    public void end(){
        Robot.snowBlower.stopCargoIntake();
        Robot.snowBlower.setLEDs(SnowBlower.LEDMode.STATIC, SnowBlower.Constants.Colors.DEFAULT);
    }
}
