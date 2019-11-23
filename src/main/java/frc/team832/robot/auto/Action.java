package frc.team832.robot.auto;

import edu.wpi.first.wpilibj.trajectory.Trajectory;

public abstract class Action {
	abstract Trajectory getPath(StartPosition startPos);
}
