package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import frc.team832.robot.OI;

public class ManualToggle extends ConditionalCommand {

	public ManualToggle (Command onTrue, Command onFalse) {
		super(onTrue, onFalse);
	}

	@Override
	protected boolean condition () {
		return OI.mToggle.get();
	}
}
