package frc.team832.robot.commands.automaticDriving;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.subsystems.*;

public class DriveToTarget extends CommandBase {

	private final Drivetrain drivetrain;
	private final Vision vision;

	public DriveToTarget(Drivetrain drivetrain, Vision vision) {
		addRequirements(drivetrain, vision);
		this.drivetrain = drivetrain;
		this.vision = vision;
	}

	@Override
	public void initialize(){
		vision.setLight(true);
	}

	@Override
	public  void  execute(){
		drivetrain.visionDrive(vision.getArea(), vision.getYaw());
	}

	@Override
	public void end (boolean interrupted) {
		vision.setLight(false);
	}

	@Override
	public boolean isFinished(){
		return false;
	}

	//uhhhhhhh vision
}
