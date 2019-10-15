package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.subsystems.Elevator;

public class ManualMoveElevator extends CommandBase {
    private final Elevator elevator;

    public ManualMoveElevator (Elevator elevator) {
        this.elevator = elevator;
        addRequirements(this.elevator);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        elevator.moveManual();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {

    }
}
