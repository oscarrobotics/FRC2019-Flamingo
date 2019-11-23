package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team832.lib.driverstation.controllers.StratComInterface;
import frc.team832.robot.LEDs;
import frc.team832.robot.subsystems.Elevator;
import frc.team832.robot.subsystems.Fourbar;
import frc.team832.robot.subsystems.SuperStructure;

public class Storage extends InstantCommand {
	private final SuperStructure superStructure;
	private final StratComInterface.ThreeSwitchPos switchPos;

	public Storage(StratComInterface.ThreeSwitchPos threeSwitch, SuperStructure subsystem, Fourbar fourbar, Elevator elevator) {
		this.superStructure = subsystem;
		this.switchPos = threeSwitch;
		addRequirements(superStructure, fourbar, elevator);
	}

	@Override
	public void initialize() {
		if (switchPos == StratComInterface.ThreeSwitchPos.SWITCH_UP)
			superStructure.setPosition(SuperStructure.SuperStructurePosition.STORAGE_DEFENCE);
		else if (switchPos == StratComInterface.ThreeSwitchPos.SWITCH_DOWN)
			superStructure.setPosition(SuperStructure.SuperStructurePosition.STORAGE_OFFENSE);

		LEDs.setLEDs(LEDs.LEDMode.DEFAULT);
	}

	@Override
	public void end(boolean interrupted) {
	}
}
