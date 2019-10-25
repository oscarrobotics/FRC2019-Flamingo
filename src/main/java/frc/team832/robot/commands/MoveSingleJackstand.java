package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team832.robot.subsystems.Jackstand;

public class MoveSingleJackstand extends InstantCommand {
	private Jackstand subsystem;
	private Jackstand.JackstandType jackstandType;
	private Jackstand.FrontJackPosition frontPosition;
	private Jackstand.BackJackPosition backPosition;

	public MoveSingleJackstand(Jackstand.FrontJackPosition jackstandPosition, Jackstand subsystem) {
		this.subsystem = subsystem;
		this.jackstandType = Jackstand.JackstandType.FRONT;
		this.frontPosition = jackstandPosition;
	}

	public MoveSingleJackstand(Jackstand.BackJackPosition jackstandPosition, Jackstand subsystem) {
		this.subsystem = subsystem;
		this.jackstandType = Jackstand.JackstandType.BACK;
		this.backPosition = jackstandPosition;
	}

	@Override
	public void initialize() {
		if (jackstandType == Jackstand.JackstandType.FRONT) {
			subsystem.setFrontJack(frontPosition);
		} else if (jackstandType == Jackstand.JackstandType.BACK) {
			subsystem.setBackJack(backPosition);
		}
	}

	@Override
	public void end(boolean interuppted){

	}

}
