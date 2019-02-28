package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.OI;
import frc.team832.robot.Robot;

public class MoveJackStands extends Command {

    private String _index;

    public MoveJackStands(String index){
        requires(Robot.jackStands);
        _index = index;
    }

    public void initialize(){

        if(OI.driverPad.getBumper(GenericHID.Hand.kLeft)){
            Robot.jackStands.setPosition(_index);
        } else {
            end();
        }
    }

    @Override
    protected boolean isFinished() {
        return Robot.jackStands.getAtTarget();
    }
}
