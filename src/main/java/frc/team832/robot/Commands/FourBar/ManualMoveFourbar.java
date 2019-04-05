package frc.team832.robot.Commands.FourBar;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.GrouchLib.Util.OscarMath;
import frc.team832.robot.OI;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Fourbar;

import static frc.team832.robot.Robot.fourbar;

public class ManualMoveFourbar extends Command {

	private boolean isSafe = false;
	private final double CONVERSION = 10;

	public ManualMoveFourbar (){
		requires(fourbar);
	}

	public void execute(){
		double input = OI.operatorBox.getX();
		double realInput = OscarMath.clipMap(input,-1, 1, 0, 1);
		double pos = OscarMath.map(realInput, 0, 1, fourbar.getMinSafePos(), Fourbar.Constants.TOP_MAX_VAL);
		fourbar.setMotionPosition(pos, fourbar.armFF());
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

}
