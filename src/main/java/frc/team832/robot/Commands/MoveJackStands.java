package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;

public class MoveJackStands extends Command {

    private String _frontIndex, _backIndex;

    public MoveJackStands(String frontIndex, String backIndex){
        requires(Robot.jackStands);
        _frontIndex = frontIndex;
        _backIndex = backIndex;
    }

    public void initialize(){
        Robot.jackStands.setPosition(_backIndex);
    }

    @Override
    protected boolean isFinished() {
        return Robot.jackStands.getAtTarget();
    }
}
