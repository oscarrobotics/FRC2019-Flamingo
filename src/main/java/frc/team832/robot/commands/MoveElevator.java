package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.subsystems.Elevator;

public class MoveElevator extends CommandBase {
    private final Elevator elevator;

    public MoveElevator(Elevator subsystem) {
        this.elevator = subsystem;
        addRequirements(elevator);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        elevator.setTarget(elevator.getSliderTarget(elevator.getSlider()));
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {

    }
}
