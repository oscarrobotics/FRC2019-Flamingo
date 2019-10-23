package frc.team832.robot;

import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import frc.team832.lib.motion.PathHelper;

public class Paths {
    private Paths() {}

    private static final Pose2d CENTER_HAB_START_POSE = new Pose2d(new Translation2d(1.775, 4.125), new Rotation2d(0));
    private static final Pose2d RIGHT_HAB_START_POSE = new Pose2d(new Translation2d(1.775, 5.2), new Rotation2d(0));
    private static final Pose2d LEFT_HAB_START_POSE = new Pose2d(new Translation2d(1.775, 3.0), new Rotation2d(0));



    public static void initializePaths() {

    }

    public static Trajectory CENTERHAB_TO_FL_CARGO = PathHelper
}