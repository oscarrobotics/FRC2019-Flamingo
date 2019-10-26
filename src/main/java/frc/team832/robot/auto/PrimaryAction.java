package frc.team832.robot.auto;

import edu.wpi.first.wpilibj.trajectory.Trajectory;

public class PrimaryAction extends Action {

	private PAction action;

	public PrimaryAction(PAction action) {
		this.action = action;
	}

	@Override
	Trajectory getPath (StartPosition startPos) {
		Trajectory path;

		switch (startPos) {
			case LEFT_HAB:

				break;
			case CENTER_HAB:

				break;

			case RIGHT_HAB:

				break;
		};

		return null;
	}

	public enum PAction {
		ROCKET_CLOSE,
		ROCKET_FAR,
		CARGO_FRONT_LEFT,
		CARGO_FRONT_RIGHT,
		CARGO_LEFTSIDE1,
		CARGO_LEFTSIDE2,
		CARGO_LEFTSIDE3,
		CARGO_RIGHTSIDE1,
		CARGO_RIGHTSIDE2,
		CARGO_RIGHTSIDE3,
	}

}
