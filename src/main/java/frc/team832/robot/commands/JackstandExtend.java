package frc.team832.robot.commands;

import edu.wpi.first.wpilibj.frc2.command.SendableCommandBase;
import frc.team832.robot.subsystems.Jackstand;

public class JackstandExtend extends SendableCommandBase {
	public JackstandExtend() {
		addRequirements(Jackstand.getInstance());
	}

	public void initialize() {
		Jackstand.jackstandExtend();
	}

	@Override
	public void execute() {

	}

	public boolean isFinished(){
		return Jackstand.backAtTarget() && Jackstand.frontAtTarget();
	}

	@Override
	public void end(boolean interrupted) {
	}

}
