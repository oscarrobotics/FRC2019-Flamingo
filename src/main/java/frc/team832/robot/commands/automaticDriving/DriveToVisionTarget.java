package frc.team832.robot.commands.automaticDriving;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.LEDs;
import frc.team832.robot.subsystems.*;

public class DriveToVisionTarget extends CommandBase {

	private final Drivetrain drivetrain;
	private final Vision vision;

	public DriveToVisionTarget (Drivetrain drivetrain, Vision vision) {
		addRequirements(drivetrain, vision);
		this.drivetrain = drivetrain;
		this.vision = vision;
	}

	@Override
	public void initialize(){
		vision.setLight(true);
		LEDs.setLEDs(LEDs.LEDMode.OFF);
	}

	@Override
	public void execute(){
		if (vision.isValid()) {
			drivetrain.visionDrive(vision.getArea(), vision.getYaw());
		}
	}

	@Override
	public void end (boolean interrupted) {
		vision.setLight(false);
		LEDs.setLEDs(LEDs.LEDMode.DEFAULT);
	}

	@Override
	public boolean isFinished(){
		return false;
	}

	//uhhhhhhh vision
}
