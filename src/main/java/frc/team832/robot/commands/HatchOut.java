package frc.team832.robot.commands;

import edu.wpi.first.wpilibj.frc2.command.SendableCommandBase;
import frc.team832.robot.subsystems.Intake;

public class HatchOut extends SendableCommandBase {
	public HatchOut() {
		addRequirements(Intake.getInstance());
	}

	public void initialize() {
		Intake.hatchOut(1.0);
	}

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interrupted) {
		Intake.stopHatch();
	}
}
