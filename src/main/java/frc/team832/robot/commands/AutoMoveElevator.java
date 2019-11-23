package frc.team832.robot.commands;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team832.robot.subsystems.Elevator;

public class AutoMoveElevator extends InstantCommand {
	private final Elevator elevator;
	private final Elevator.ElevatorPosition target;

	public AutoMoveElevator(Elevator subsystem, Elevator.ElevatorPosition position) {
		this.elevator = subsystem;
		this.target = position;
		addRequirements(elevator);
	}

	@Override
	public void initialize() {
		elevator.setPosition(target);
	}

	@Override
	public void end(boolean interrupted) {
	}

}
