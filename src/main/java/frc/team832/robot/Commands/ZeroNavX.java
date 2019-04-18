package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team832.robot.RobotMap;

public class ZeroNavX extends InstantCommand {
	public void init(){
		RobotMap.navX.zero();
	}
}
