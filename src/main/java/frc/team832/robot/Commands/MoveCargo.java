package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team832.robot.Robot;

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
}
