package frc.team832.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.RunEndCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.lib.driverstation.dashboard.DashboardManager;
import frc.team832.lib.driverstation.dashboard.DashboardUpdatable;
import frc.team832.lib.drive.SmartDifferentialDrive;
import frc.team832.lib.driverstation.dashboard.DashboardWidget;
import frc.team832.lib.motorcontrol.vendor.CANSparkMax;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motors.Motors;
import frc.team832.lib.util.OscarMath;
import frc.team832.robot.Constants;
import frc.team832.robot.Robot;
import frc.team832.robot.RobotContainer;

import static frc.team832.robot.Paths.CENTER_HAB_START_POSE;

public class Drivetrain extends SubsystemBase implements DashboardUpdatable {

    private AHRS navx;
    private CANSparkMax leftMaster, rightMaster, leftSlave, rightSlave;
    private static PIDController yawController = new PIDController(Constants.YAW_PID[0], Constants.YAW_PID[1], Constants.YAW_PID[2]);
    private SmartDifferentialDrive diffDrive;
    private DifferentialDriveOdometry driveOdometry;

    private Pose2d startingPose = CENTER_HAB_START_POSE; // TODO: Set from Dashboard

    private NetworkTableEntry dashboard_leftFPS, dashboard_rightFPS, dashboard_leftFPSPeak, dashboard_rightFPSPeak, dashboard_leftMPS, dashboard_rightMPS,
                              dashboard_leftAmps, dashboard_rightAmps, dashboard_leftAmpsPeak, dashboard_rightAmpsPeak,
                              dashboard_totalAmps, dashboard_totalAmpsPeak, dashboard_desiredVelocity,
                              dashboard_leftVelocity, dashboard_rightVelocity, dashboard_leftVelocityError, dashboard_rightVelocityError,
                              dashboard_navxYaw, dashboard_yawOutput, dashboard_isRightDecel, dashboard_isLeftDecel,
                              dashboard_leftPos, dashboard_rightPos, dashboard_poseX, dashboard_poseY, dashboard_poseHeading;

    private NetworkTable falconTable = NetworkTableInstance.getDefault().getTable("Live_Dashboard");
    private NetworkTableEntry falconPoseXEntry = falconTable.getEntry("robotX");
    private NetworkTableEntry falconPoseYEntry = falconTable.getEntry("robotY");
    private NetworkTableEntry falconPoseHeadingEntry = falconTable.getEntry("robotHeading");
    private NetworkTableEntry falconIsPathingEntry = falconTable.getEntry("isFollowingPath");
    private NetworkTableEntry falconPathXEntry = falconTable.getEntry("pathX");
    private NetworkTableEntry falconPathYEntry = falconTable.getEntry("pathY");
    private NetworkTableEntry falconPathHeadingEntry = falconTable.getEntry("pathHeading");

    private static final double DefaultRotMultiplier = 0.6;
    private static final double PreciseRotMultiplier = 0.1;
    private static final double PreciseDriveMultiplier = 0.2;

    private boolean holdYaw;
    private double yawCorrection, yawSetpoint;
    private double desiredRPM;

    private double leftFPS, rightFPS, leftPeakFPS, rightPeakFPS, leftMPS, rightMPS;
    private double leftAmps, rightAmps, leftPeakAmps, rightPeakAmps;
    private double totalAmps, totalPeakAmps;

    private double[] drivePIDF = Constants.DRIVE_PIDF;


    public Drivetrain() {
        setName("Drive Subsys");
        DashboardManager.addTab(this);
        DashboardManager.addTabSubsystem(this, this);
    }

    @Override
    public void periodic() {
        leftFPS = Constants.DRIVE_POWERTRAIN.calculateFeetPerSec(leftMaster.getSensorVelocity());
        rightFPS = Constants.DRIVE_POWERTRAIN.calculateFeetPerSec(rightMaster.getSensorVelocity());

        leftMPS = Constants.DRIVE_POWERTRAIN.calculateMetersPerSec(leftMaster.getSensorVelocity());
        rightMPS = Constants.DRIVE_POWERTRAIN.calculateMetersPerSec(rightMaster.getSensorVelocity());

        leftAmps = leftMaster.getOutputCurrent() + leftSlave.getOutputCurrent();
        rightAmps = rightMaster.getOutputCurrent() + rightSlave.getOutputCurrent();

        totalAmps = leftAmps + rightAmps;

        if (Math.abs(leftFPS) > leftPeakFPS) leftPeakFPS = Math.abs(leftFPS);
        if (Math.abs(rightFPS) > rightPeakFPS) rightPeakFPS = Math.abs(rightFPS);

        if (leftAmps > leftPeakAmps) leftPeakAmps = leftAmps;
        if (rightAmps > rightPeakAmps) rightPeakAmps = rightAmps;

        if (totalAmps > totalPeakAmps) totalPeakAmps = totalAmps;

        desiredRPM = RobotContainer.drivePad.getY(Hand.kLeft) * Motors.NEO.freeSpeed;

//        handleDecelLimiting();
    }

    public boolean initialize() {
        boolean success = true;

        navx = new AHRS(I2C.Port.kOnboard);
        if (!navx.isConnected()) {
//            good = false;
            System.out.println("Drivetrain INIT - navX failure!");
        }

        leftMaster = new CANSparkMax(Constants.DRIVE_IDS[0], CANSparkMaxLowLevel.MotorType.kBrushless);
        leftSlave = new CANSparkMax(Constants.DRIVE_IDS[1], CANSparkMaxLowLevel.MotorType.kBrushless);
        rightMaster = new CANSparkMax(Constants.DRIVE_IDS[2], CANSparkMaxLowLevel.MotorType.kBrushless);
        rightSlave = new CANSparkMax(Constants.DRIVE_IDS[3], CANSparkMaxLowLevel.MotorType.kBrushless);

        if (leftMaster.getInstance().getFirmwareString() == null) success = false;
        if (leftSlave.getInstance().getFirmwareString() == null) success = false;
        if (rightMaster.getInstance().getFirmwareString() == null) success = false;
        if (rightSlave.getInstance().getFirmwareString() == null) success = false;

        leftMaster.resetSettings();
        leftSlave.resetSettings();
        rightMaster.resetSettings();
        rightSlave.resetSettings();

        leftSlave.follow(leftMaster);
        rightSlave.follow(rightMaster);

        leftMaster.setInverted(true);
        rightMaster.setInverted(true);

        NeutralMode allIdleMode = NeutralMode.kBrake;

        leftMaster.setNeutralMode(allIdleMode);
        leftSlave.setNeutralMode(allIdleMode);
        rightMaster.setNeutralMode(allIdleMode);
        rightSlave.setNeutralMode(allIdleMode);

        leftMaster.setkP(drivePIDF[0]);
        leftMaster.setkI(drivePIDF[1]);
        leftMaster.setkD(drivePIDF[2]);
        leftMaster.setkF(drivePIDF[3]);

        rightMaster.setkP(drivePIDF[0]);
        rightMaster.setkI(drivePIDF[1]);
        rightMaster.setkD(drivePIDF[2]);
        rightMaster.setkF(drivePIDF[3]);

        leftMaster.setClosedLoopRamp(Constants.DRIVE_ACCEL_RAMP);
        rightMaster.setClosedLoopRamp(Constants.DRIVE_ACCEL_RAMP);

        leftMaster.setPeakCurrentLimit(50);
        leftSlave.setPeakCurrentLimit(50);
        rightMaster.setPeakCurrentLimit(50);
        rightSlave.setPeakCurrentLimit(50);

        diffDrive = new SmartDifferentialDrive(leftMaster, rightMaster, 5000);

        driveOdometry = new DifferentialDriveOdometry(Constants.DRIVE_KINEMATICS, startingPose);

        // yawController.setContinuous();
//        yawController.
//        yawController.setInputRange(-180.0, 180.0);
//        yawController.setOutputRange(-0.7, 0.7);
//        yawController.setAbsoluteTolerance(1.5);

        dashboard_leftFPS = DashboardManager.addTabItem(this, "Speeds/LeftFPS", 0.0);
        dashboard_rightFPS = DashboardManager.addTabItem(this, "Speeds/RightFPS", 0.0);
        dashboard_leftMPS = DashboardManager.addTabItem(this, "Speeds/LeftMS", 0.0);
        dashboard_rightMPS = DashboardManager.addTabItem(this, "Speeds/RightMS", 0.0);
        dashboard_leftFPSPeak = DashboardManager.addTabItem(this, "FPS/LeftPeak", 0.0);
        dashboard_rightFPSPeak = DashboardManager.addTabItem(this, "FPS/RightPeak", 0.0);
        dashboard_totalAmps = DashboardManager.addTabItem(this, "Amps/Total", 0.0);
        dashboard_totalAmpsPeak = DashboardManager.addTabItem(this, "Amps/TotalPeak", 0.0);
        dashboard_leftAmps = DashboardManager.addTabItem(this, "Amps/Left", 0.0);
        dashboard_rightAmps = DashboardManager.addTabItem(this, "Amps/Right", 0.0);
        dashboard_leftAmpsPeak = DashboardManager.addTabItem(this, "Amps/LeftPeak", 0.0);
        dashboard_rightAmpsPeak = DashboardManager.addTabItem(this, "Amps/RightPeak", 0.0);
        dashboard_desiredVelocity = DashboardManager.addTabItem(this, "Velocity/Target", 0.0);
        dashboard_leftVelocity = DashboardManager.addTabItem(this, "Velocity/Left", 0.0);
        dashboard_rightVelocity = DashboardManager.addTabItem(this, "Velocity/Right", 0.0);
        dashboard_leftVelocityError = DashboardManager.addTabItem(this, "Velocity/LeftError", 0.0);
        dashboard_rightVelocityError = DashboardManager.addTabItem(this, "Velocity/RightError", 0.0);
        dashboard_navxYaw = DashboardManager.addTabItem(this, "NavXYaw", 0.0);
        dashboard_yawOutput = DashboardManager.addTabItem(this, "YawOutput", 0.0);
        dashboard_isRightDecel = DashboardManager.addTabItem(this, "RightDecel", false, DashboardWidget.BooleanBox);
        dashboard_isLeftDecel = DashboardManager.addTabItem(this, "LeftDecel", false, DashboardWidget.BooleanBox);
        dashboard_leftPos = DashboardManager.addTabItem(this, "Position/Left", 0.0);
        dashboard_rightPos = DashboardManager.addTabItem(this, "Position/Right", 0.0);
        dashboard_poseX = DashboardManager.addTabItem(this, "Pose/X", 0.0);
        dashboard_poseY = DashboardManager.addTabItem(this, "Pose/Y", 0.0);
        dashboard_poseHeading = DashboardManager.addTabItem(this, "Pose/Heading", 0.0);

        RunEndCommand driveCommand = new RunEndCommand(this::drive, this::stop, this);
        driveCommand.setName("TeleDriveCommand");

        navx.zeroYaw();
        resetEncoders();

        setDefaultCommand(driveCommand);

        return success;
    }

    private DifferentialDriveWheelSpeeds getWheelSpeeds() {
        double leftMetersPerSec = Constants.DRIVE_POWERTRAIN.calculateMetersPerSec(leftMaster.getSensorVelocity());
        double rightMetersPerSec = Constants.DRIVE_POWERTRAIN.calculateMetersPerSec(rightMaster.getSensorVelocity());
        return new DifferentialDriveWheelSpeeds(leftMetersPerSec, -rightMetersPerSec);
    }

    public void consumeWheelSpeeds(Double leftWheelSpeedMeters, Double rightWheelSpeedMeters) {
        var leftVelocity = Constants.DRIVE_POWERTRAIN.calculateMotorSpeed(leftWheelSpeedMeters);
        var rightVelocity = Constants.DRIVE_POWERTRAIN.calculateMotorSpeed(rightWheelSpeedMeters);

        leftMaster.setVelocity(leftVelocity);
        rightMaster.setVelocity(rightVelocity);
    }

    public Pose2d getLatestPose2d() {
        var newRotation2d = new Rotation2d(OscarMath.degreesToRadians(-navx.getYaw()));
        return driveOdometry.update(newRotation2d, getWheelSpeeds());
    }

    private void handleDecelLimiting() {
        leftMaster.setClosedLoopRamp(isLeftDecel() ? Constants.DRIVE_DECEL_RAMP : Constants.DRIVE_ACCEL_RAMP);
        rightMaster.setClosedLoopRamp(isRightDecel() ? Constants.DRIVE_DECEL_RAMP : Constants.DRIVE_ACCEL_RAMP);
    }

    private void drive() {
        double moveStick = -RobotContainer.drivePad.getY(Hand.kLeft);
        double rotStick = RobotContainer.drivePad.getX(Hand.kRight);

        boolean preciseRot = RobotContainer.drivePad.rightBumper.get();
        boolean preciseMove = RobotContainer.drivePad.leftBumper.get();

        boolean visionRot = RobotContainer.drivePad.rightStickPress.get();

        moveStick = OscarMath.signumPow(moveStick, 2);
        rotStick = OscarMath.signumPow(rotStick, 3);

        double rotPow = preciseRot ? rotStick * PreciseRotMultiplier : rotStick * DefaultRotMultiplier;
        double movePow = preciseMove ? moveStick * PreciseDriveMultiplier : moveStick * getPreciseDriveMultiplier();

        double yawKp = .007
                ;

        if (visionRot) {
            rotPow = RobotContainer.vision.getYaw() * yawKp;
        }

        diffDrive.curvatureDrive(movePow, rotPow, true, SmartDifferentialDrive.LoopMode.PERCENTAGE);
    }

    public void visionDrive(double area, double yaw){

    }

    private double getPreciseDriveMultiplier() {
        return RobotContainer.jackstand.getFrontTarget() < -1000 || RobotContainer.jackstand.getBackTarget() < -1000 ? PreciseDriveMultiplier  : 1.0;
    }

    private void stop() {
        diffDrive.stopMotor();
    }

    private boolean isRightDecel() {
        if (!diffDrive.isQuickTurning()) return false;
        return Math.abs(rightMaster.getSensorVelocity()) - Math.abs(desiredRPM) > 0;
    }

    private boolean isLeftDecel() {
        if (!diffDrive.isQuickTurning()) return false;
        return Math.abs(leftMaster.getSensorVelocity()) - Math.abs(desiredRPM) > 0;
    }

    @Override
    public String getDashboardTabName() {
        return m_name;
    }

    @Override
    public void updateDashboardData() {
        dashboard_desiredVelocity.setDouble(desiredRPM);
        dashboard_isRightDecel.setBoolean(isRightDecel());
        dashboard_isLeftDecel.setBoolean(isLeftDecel());

        dashboard_totalAmps.setDouble(totalAmps);
        dashboard_totalAmpsPeak.setDouble(totalPeakAmps);

        dashboard_navxYaw.setDouble(navx.getYaw());
        dashboard_yawOutput.setDouble(yawCorrection);

        dashboard_leftAmps.setDouble(leftAmps);
        dashboard_leftAmpsPeak.setDouble(leftPeakAmps);
        dashboard_leftFPS.setDouble(leftFPS);
        dashboard_leftMPS.setDouble(leftMPS);
        dashboard_leftFPSPeak.setDouble(leftPeakFPS);
        dashboard_leftVelocity.setDouble(leftMaster.getSensorVelocity());
        dashboard_leftVelocityError.setDouble(desiredRPM - leftMaster.getSensorVelocity());
        dashboard_leftPos.setDouble(leftMaster.getSensorPosition());

        dashboard_rightAmps.setDouble(rightAmps);
        dashboard_rightAmpsPeak.setDouble(rightPeakAmps);
        dashboard_rightFPS.setDouble(rightFPS);
        dashboard_rightMPS.setDouble(rightMPS);
        dashboard_rightFPSPeak.setDouble(rightPeakFPS);
        dashboard_rightVelocity.setDouble(rightMaster.getSensorVelocity());
        dashboard_rightVelocityError.setDouble(desiredRPM - rightMaster.getSensorVelocity());
        dashboard_rightPos.setDouble(rightMaster.getSensorPosition());

        var latestPose = getLatestPose2d();
        var poseTranslation = latestPose.getTranslation();
        var poseX = poseTranslation.getX();
        var poseY = poseTranslation.getY();
        var poseHeading = latestPose.getRotation().getRadians();

        falconPoseXEntry.setDouble(Units.metersToFeet(poseX));
        falconPoseYEntry.setDouble(Units.metersToFeet(poseY));
        falconPoseHeadingEntry.setDouble(poseHeading);

        dashboard_poseX.setDouble(poseX);
        dashboard_poseY.setDouble(poseY);
        dashboard_poseHeading.setDouble(poseHeading);
    }

    public void setIdleMode(NeutralMode neutralMode) {
        leftMaster.setNeutralMode(neutralMode);
        leftSlave.setNeutralMode(neutralMode);
        rightMaster.setNeutralMode(neutralMode);
        rightSlave.setNeutralMode(neutralMode);
    }

    public void resetGyro() {
        navx.zeroYaw();
    }

    public void resetEncoders() {
        leftMaster.setSensorPosition(0);
        rightMaster.setSensorPosition(0);
    }
}
