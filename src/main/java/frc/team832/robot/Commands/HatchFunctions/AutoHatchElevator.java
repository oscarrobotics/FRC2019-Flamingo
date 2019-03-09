package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Elevator;


public class AutoHatchElevator extends Command {

    private String _index;
    private double _pos;
    private long startTime;

    public AutoHatchElevator(String index) {
        requires(Robot.elevator);
        _index = index;
        _pos = Elevator.Constants.Positions.getByIndex(index).getTarget();
    }

    public AutoHatchElevator(double pos) {
        requires(Robot.elevator);
        _pos = pos;
    }

    @Override
    protected void initialize() {
        startTime = System.currentTimeMillis();
        Robot.elevator.setPosition(_pos);
        Robot.currentHatchState = Robot.AutoHatchState.MovingElevator;
    }

    @Override
    protected void execute() {
        //TODO: add buttons and positions
    }

    @Override
    protected boolean isFinished() {
        return Robot.elevator.atTargetPosition() ||
                (Robot.interruptedHatchState != Robot.AutoHatchState.None && Robot.interruptedHatchState != Robot.AutoHatchState.MovingElevator);
    }

    @Override
    protected void end() {
        if(Robot.interruptedHatchState == Robot.AutoHatchState.MovingElevator){
            Robot.interruptedHatchState = Robot.AutoHatchState.None;
        }
    }

    @Override
    protected void interrupted() {
        super.interrupted();
    }
}
