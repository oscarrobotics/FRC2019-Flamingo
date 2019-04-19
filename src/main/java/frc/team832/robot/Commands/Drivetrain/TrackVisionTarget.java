package frc.team832.robot.Commands.Drivetrain;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import frc.team832.GrouchLib.Motion.SmartDifferentialDrive;
import frc.team832.robot.OI;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Drivetrain;

public class TrackVisionTarget extends Command {

	private double _minDist;

	public TrackVisionTarget() {
		requires(Robot.drivetrain);
		requires(Robot.vision);
		_minDist = 0.0;
	}

	public TrackVisionTarget (double minDist) {
		requires(Robot.drivetrain);
		requires(Robot.vision);
		_minDist = minDist;
	}

	@Override
	protected void initialize() {
		NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(0);
		NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(0);
	}

	@Override
	protected void execute () {
		double distPow = OI.driverPad.getStickButton(GenericHID.Hand.kLeft) ? Robot.vision.getDistPower() : OI.driverPad.getY(GenericHID.Hand.kLeft);
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

	@Override
	protected void end() {
		NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
		NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
	}
}
