package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;

public class TeleopControlFourbar extends Command {

    private String _index;

    public TeleopControlFourbar(String index) {
        requires(Robot.fourbar);
        _index = index;
    }

    @Override
    protected void initialize() {
        Robot.fourbar.setPosition(_index);
    }

    @Override
    protected void execute() { }

    @Override
    protected boolean isFinished() {
        return Robot.fourbar.atTargetPosition();
    }

    @Override
    protected void end() { }

    @Override
    protected void interrupted() {
        super.interrupted();
    }
}
