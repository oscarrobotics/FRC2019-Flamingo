package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.SnowBlower;

public class MoveCargo extends InstantCommand {

    private double _pow;

    public MoveCargo(double pow) {
        requires(Robot.snowBlower);
        _pow = pow;
    }

    @Override
    protected void initialize(){
        Robot.snowBlower.intakeSet(_pow);
    }

    public void execute(){
        if (_pow < 0)
            Robot.snowBlower.setLEDs(SnowBlower.LEDMode.BALL_INTAKE);
        else
            Robot.snowBlower.setLEDs(SnowBlower.LEDMode.BALL_OUTTAKE);
    }

    public void end(){
        Robot.snowBlower.setLEDs(SnowBlower.LEDMode.OFF);
    }
}
