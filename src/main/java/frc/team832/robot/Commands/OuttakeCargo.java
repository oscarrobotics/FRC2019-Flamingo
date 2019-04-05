package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.SnowBlower;

import java.awt.*;

public class OuttakeCargo extends Command {

	private double _pow;

	public OuttakeCargo(double pow) {
		requires(Robot.snowBlower);
		_pow = -pow;
	}

	@Override
	protected void initialize(){
		Robot.snowBlower.intakeSet(_pow);
		Robot.snowBlower.setLEDs(SnowBlower.LEDMode.BALL_OUTTAKE);
	}

	@Override
	protected boolean isFinished () {
		return false;
	}

	public void end(){
		Robot.snowBlower.stopCargoIntake();
		Robot.snowBlower.setLEDs(SnowBlower.LEDMode.STATIC, SnowBlower.Constants.Colors.DEFAULT);
	}
}
