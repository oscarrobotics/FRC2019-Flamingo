package frc.team832.robot.Commands.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.GrouchLib.Util.OscarMath;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Drivetrain;

public class DrivetrainTeleop extends Command {

	public DrivetrainTeleop(){
		requires(Robot.drivetrain);
	}

	public void execute(){
		Drivetrain.teleopControl();
	}

	@Override
	protected boolean isFinished () {
		return false;
	}
}
