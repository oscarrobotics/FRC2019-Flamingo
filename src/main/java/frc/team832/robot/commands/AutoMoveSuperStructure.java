package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team832.robot.subsystems.Elevator;
import frc.team832.robot.subsystems.Fourbar;
import frc.team832.robot.subsystems.SuperStructure;

public class AutoMoveSuperStructure extends InstantCommand {
	private final SuperStructure superStructure;
	private final SuperStructure.SuperStructurePosition target;

	public AutoMoveSuperStructure(SuperStructure.SuperStructurePosition position, SuperStructure subsystem, Fourbar fourbar, Elevator elevator) {
		this.superStructure = subsystem;
		this.target = position;
		addRequirements(superStructure, fourbar, elevator);
	}

	@Override
	public void initialize() {
		superStructure.setPosition(target);
	}

	@Override
	public void end(boolean interrupted) {
	}
}
