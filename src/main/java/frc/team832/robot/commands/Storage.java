package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team832.robot.RobotContainer;
import frc.team832.robot.subsystems.Elevator;
import frc.team832.robot.subsystems.Fourbar;
import frc.team832.robot.subsystems.SuperStructure;

public class Storage extends InstantCommand {
	private final SuperStructure superStructure;
	private final RobotContainer.ThreeSwitchPos switchPos;

	public Storage(RobotContainer.ThreeSwitchPos threeSwitch, SuperStructure subsystem, Fourbar fourbar, Elevator elevator) {
		this.superStructure = subsystem;
		this.switchPos = threeSwitch;
		addRequirements(superStructure, fourbar, elevator);
	}

	@Override
	public void initialize() {
		if (switchPos == RobotContainer.ThreeSwitchPos.UP)
			superStructure.setPosition(SuperStructure.SuperStructurePosition.STORAGE_DEFENCE);
		else if (switchPos == RobotContainer.ThreeSwitchPos.DOWN)
			superStructure.setPosition(SuperStructure.SuperStructurePosition.STORAGE_OFFENSE);
	}

	@Override
	public void end(boolean interrupted) {
	}
}
