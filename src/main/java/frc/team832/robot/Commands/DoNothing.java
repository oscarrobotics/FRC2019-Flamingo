package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;

public class DoNothing extends Command {
	public DoNothing() {
	}


	protected void initialize() {
	}


	protected void execute(){
		System.out.println("Doing Nothing...");

	}
	protected boolean isFinished() {
		return true;
	}
}
