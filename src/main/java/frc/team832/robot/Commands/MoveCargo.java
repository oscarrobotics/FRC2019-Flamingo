package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;

public class MoveCargo extends Command {

    private double _pow;

    public MoveCargo(double pow) {
        requires(Robot.snowBlower);
        _pow = pow;
    }

    @Override
    protected void initialize(){
        Robot.snowBlower.intakeSet(_pow);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
