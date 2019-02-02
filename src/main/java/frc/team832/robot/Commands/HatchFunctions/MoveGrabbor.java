package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;

public class MoveGrabbor extends Command {

    String _index;

    public MoveGrabbor(String index){
        _index = index;
    }

    public void initialize(){
        Robot.snowBlower.setGrabborPosition(_index);
    }

    public void execute(){

    }

    @Override
    protected boolean isFinished() {
        return Math.abs(Robot.snowBlower.getGrabborTargetPosition(_index) - Robot.snowBlower.getGrabborCurrentPosition()) <=20;
    }
}
