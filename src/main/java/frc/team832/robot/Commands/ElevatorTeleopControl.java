package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Elevator;

public class ElevatorTeleopControl extends Command {

    private String _index;
    private double _pos;

    public ElevatorTeleopControl(String index) {
        requires(Robot.elevator);
        _index = index;
        _pos = Elevator.Constants.Positions.getByIndex(index).getTarget();
    }

    public ElevatorTeleopControl(double pos) {
        requires(Robot.elevator);
        _pos = pos;
    }

    @Override
    protected void initialize() {
        Robot.elevator.setPosition(_pos);
    }

    @Override
    protected void execute() {
        //TODO: add buttons and positions
    }

    @Override
    protected boolean isFinished() {
        return Robot.elevator.atTargetPosition();
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
        super.interrupted();
    }
}
