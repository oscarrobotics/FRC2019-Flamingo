package frc.team832.robot.Commands.AutoJackStand;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.OI;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.JackStands;

public class MoveSingleJackStand extends Command {

    private String _index;
    private JackStands.JackStand _stand;

    public MoveSingleJackStand(JackStands.JackStand stand, String index){
        requires(Robot.jackStands);
        _index = index;
        _stand = stand;
    }

    public void initialize(){
       switch (_stand) {
            case FRONT:
                Robot.jackStands.setFrontPosition(_index);
                break;
            case BACK:
                Robot.jackStands.setBackPosition(_index);
                break;
        }

    }

    @Override
    protected boolean isFinished() {
        return Robot.jackStands.getAtTarget(_stand);
    }
}
