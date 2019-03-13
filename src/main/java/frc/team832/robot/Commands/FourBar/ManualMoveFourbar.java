package frc.team832.robot.Commands.FourBar;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.OI;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Fourbar;

public class ManualMoveFourbar extends Command {

	boolean isSafe = false;
	final double CONVERSION = 10;

	public ManualMoveFourbar (){
		requires(Robot.fourbar);
	}

	public void initialize(){

	}

	public void execute(){


			Robot.fourbar.setMotionPosition(OI.operatorBox.getX(), Robot.fourbar.armFF());


	}

	@Override
	protected boolean isFinished() {
		return false;
	}

}
