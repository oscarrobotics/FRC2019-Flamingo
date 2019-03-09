package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;

public class MoveComplexLift extends Command {

    private String _fourbarPos, _elevatorPos;

    public MoveComplexLift(String fourbarPos, String elevatorPos) {
        requires(Robot.fourbar);
        requires(Robot.elevator);

        _fourbarPos = fourbarPos;
        _elevatorPos = elevatorPos;
    }

    @Override
    protected void initialize() {
        Robot.elevator.setPosition(_elevatorPos);
        Robot.fourbar.setPosition(_fourbarPos);
    }

    @Override
    protected void execute() { }

    @Override
    protected boolean isFinished() {
        return Robot.fourbar.atTargetPosition() && Robot.elevator.atTargetPosition();
    }

    @Override
    protected void end() { }

    @Override
    protected void interrupted() {
        super.interrupted();
    }
}
