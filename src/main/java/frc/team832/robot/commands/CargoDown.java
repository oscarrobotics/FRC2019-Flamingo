package frc.team832.robot.commands;

import edu.wpi.first.wpilibj.frc2.command.SendableCommandBase;
import frc.team832.robot.subsystems.Intake;

public class CargoDown extends SendableCommandBase {
	public CargoDown() {
		addRequirements(Intake.getInstance());
	}

	public void initialize() {
		Intake.cargoDown(1.0);
	}

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interrupted) {
		Intake.stopCargo();
	}
}
