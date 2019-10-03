package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team832.robot.RobotContainer;
import frc.team832.robot.subsystems.Jackstand;

public class JackstandAutoLvl2 extends CommandGroupBase {
	public JackstandAutoLvl2() {
		addCommands(new MoveJackstands(RobotContainer.jackstand, Jackstand.JackstandPosition.LVL2_UP),
					//new InstantCommand(RobotContainer.jackstand.),  Placeholder for driving with jackstand
					new MoveJackstands(RobotContainer.jackstand, Jackstand.JackstandPosition.LVL2_FRONT_ON),
					//new InstantCommand(RobotContainer.jackstand.),  Placeholder for driving with jackstand
					new MoveJackstands(RobotContainer.jackstand, Jackstand.JackstandPosition.RETRACTED)
				);
	}

	@Override
	public void addCommands (Command... commands) {

	}
}