package frc.team832.robot.commands;

import edu.wpi.first.wpilibj.frc2.command.SendableCommandBase;
import frc.team832.robot.subsystems.Intake;

public class HatchIn extends SendableCommandBase {
	public HatchIn() {
		addRequirements(Intake.getInstance());
	}

	public void initialize() {
		Intake.hatchIn(-1.0);
	}

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interrupted) {
		Intake.stopHatch(0);
	}
}
