package frc.team832.robot.Commands.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Motion.SmartDifferentialDrive;
import frc.team832.robot.OI;
import frc.team832.robot.Subsystems.Drivetrain;

import static frc.team832.robot.Robot.drivetrain;

public class TurnToHeading extends Command {

	private double _heading;
	public TurnToHeading(double heading){
		_heading = heading;
	}

	public void initialize(){
		OI.angle += 45;
	}

	public void execute(){
		drivetrain.joystickDrive(
				Drivetrain.gyroCorrectionOutput(_heading),
				0,
				Drivetrain.DriveMode.ARCADE,
				SmartDifferentialDrive.LoopMode.PERCENTAGE);

	}

	@Override
	protected boolean isFinished () {
		return Drivetrain.turnAngleError(_heading) < Drivetrain.Constants.epsilon;
	}
}
