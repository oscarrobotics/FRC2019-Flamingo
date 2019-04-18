package frc.team832.robot.Commands.Drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import frc.team832.GrouchLib.Motion.SmartDifferentialDrive;
import frc.team832.robot.OI;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Drivetrain;

import static frc.team832.robot.Robot.drivetrain;

public class TrackVisionTarget extends Command {

	private double _minDist;

	public TrackVisionTarget (double minDist) {
		requires(Robot.drivetrain);
		requires(Robot.vision);
		_minDist = minDist;
	}

	@Override
	protected void initialize () {

	}

	@Override
	protected void execute () {
		double distPow = OI.driverPad.getBButton() ? Robot.vision.getDistPower() : OI.driverPad.getY(GenericHID.Hand.kLeft);
		double rotPow = Robot.vision.getRotPower();

		Robot.drivetrain.joystickDrive(
				distPow,
				rotPow,
				Drivetrain.DriveMode.CURVATURE,
				SmartDifferentialDrive.LoopMode.PERCENTAGE
		);
	}

	@Override
	protected boolean isFinished () {
//		return Robot.vision.getData().adjustedDistance <= _minDist;
		return false;
	}
}
