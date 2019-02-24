package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;

public class MoveCargo extends Command {

    public MoveCargo() {
        requires(Robot.snowBlower);
    }

    @Override
    protected void initialize(){
        Robot.snowBlower.intakeSet(.5);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
