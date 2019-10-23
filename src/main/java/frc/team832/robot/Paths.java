package frc.team832.robot;

import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import frc.team832.lib.motion.PathHelper;

public class Paths {
    private Paths() {}

    private static final double LEFT_ROCKET_FRONT_ANGLE = 27.5;
    private static final double RIGHT_ROCKET_FRONT_ANGLE = -27.5;
 
    private static final Pose2d CENTER_HAB_START_POSE = new Pose2d(new Translation2d(1.775, 4.125), new Rotation2d(0));
    private static final Pose2d RIGHT_HAB_START_POSE = new Pose2d(new Translation2d(1.775, 5.2), new Rotation2d(0));
    private static final Pose2d LEFT_HAB_START_POSE = new Pose2d(new Translation2d(1.775, 3.0), new Rotation2d(0));

    private static final List<Translation2d> RIGHTHAB_RIGHTFRONTROCKET_WAYPOINTS = Arrays.asList(
        RIGHT_HAB_START_POSE.getTranslation(),
        new Translation2d(4.521, 5.714), 
        new Translation2d(5.198, 7.633)      
    );

    private static final List<Translation2d> TEST_THREE_METERS_FORWARD = Arrays.asList(
        RIGHT_HAB_START_POSE.getTranslation(),
        RIGHT_HAB_START_POSE.getTranslation().plus(new Translation2d(3
                , 0))
    );

    private static PathHelper pathHelper;
    public static Trajectory RightHab_RightFrontRocket;
    public static Trajectory Test_Three_Meters_Forward;

    //egg in me Mr.Muropulesoesiesooiseioesoeoiseosioesioeoseis
    public static void initializePaths(DifferentialDriveKinematics kinematics, double maxVelocityMetersPerSecond, double maxAccelerationMetersPerSecondSq) {
        pathHelper = new PathHelper(kinematics, maxVelocityMetersPerSecond, maxAccelerationMetersPerSecondSq, false);
        RightHab_RightFrontRocket = pathHelper.generatePath(0, RIGHTHAB_RIGHTFRONTROCKET_WAYPOINTS, RIGHT_ROCKET_FRONT_ANGLE);
        Test_Three_Meters_Forward = pathHelper.generatePath(0, TEST_THREE_METERS_FORWARD, 0);
    }
}
