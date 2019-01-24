package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.SnowBlower;

import static frc.team832.robot.Robot.snowBlower;

public class MoveCargo extends Command {

    private SnowBlower.CargoPosition _targetPosition, _currentPosition;

    public MoveCargo(SnowBlower.CargoPosition targetPosition){
        super("MoveCargo_" + targetPosition.toString(), 0, snowBlower);
        _targetPosition = targetPosition;
    }

    @Override
    protected void initialize() {
        _currentPosition = snowBlower.getCargoPosition();
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
