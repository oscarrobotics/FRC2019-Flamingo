package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.OI;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Elevator;
import frc.team832.robot.Subsystems.Fourbar;


public class MoveComplexLiftWithAdjust extends Command {

    private double _fourbarPos, _elevatorPos;
    private double fourbarAdj, elevatorAdj;

    public MoveComplexLiftWithAdjust (String fourbarPos, String elevatorPos) {
        requires(Robot.fourbar);
        requires(Robot.elevator);

        _fourbarPos = Fourbar.Constants.Positions.getByIndex(fourbarPos).getTarget();
        _elevatorPos = Elevator.Constants.Positions.getByIndex(elevatorPos).getTarget();
    }

    @Override
    protected void initialize() {
        Robot.elevator.setMotionPosition(_elevatorPos);
        Robot.fourbar.setMotionPosition(_fourbarPos, Robot.fourbar.armFF());
    }

    @Override
    protected void execute() {
        if(OI.singleSwitch.get()) {
            fourbarAdj = 500 * (OI.operatorBox.getX());
            elevatorAdj = 75 * (OI.operatorBox.getY());
        }else{
            elevatorAdj = 0;
            fourbarAdj = 0;
        }
        Robot.elevator.setMotionPosition(_elevatorPos + elevatorAdj);
        Robot.fourbar.setMotionPosition(_fourbarPos + fourbarAdj, Robot.fourbar.armFF());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() { }

    @Override
    protected void interrupted() {
        super.interrupted();
    }
}
