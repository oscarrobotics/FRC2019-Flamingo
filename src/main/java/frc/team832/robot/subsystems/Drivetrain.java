package frc.team832.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpilibj2.command.RunEndCommand;
import edu.wpi.first.wpiutil.math.MathUtils;
import frc.team832.lib.driverstation.dashboard.DashboardManager;
import frc.team832.lib.driverstation.dashboard.DashboardUpdatable;
import frc.team832.lib.drive.SmartDifferentialDrive;
import frc.team832.lib.driverstation.dashboard.DashboardWidget;
import frc.team832.lib.motorcontrol.vendor.CANSparkMax;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motors.Motors;
import frc.team832.lib.util.OscarMath;
import frc.team832.robot.Constants;
import frc.team832.robot.RobotContainer;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;

public class Drivetrain extends PIDSubsystem implements DashboardUpdatable {

    private AHRS navx;
    private CANSparkMax leftMaster, rightMaster, leftSlave, rightSlave;
    private static PIDController yawController = new PIDController(Constants.YAW_PID[0], Constants.YAW_PID[1], Constants.YAW_PID[2]);
    private SmartDifferentialDrive diffDrive;

    public final DifferentialDriveKinematics driveKinematics = new DifferentialDriveKinematics(0.711); //track width in meters
    private DifferentialDriveOdometry driveOdometry;

    private static final Pose2d CENTER_HAB_START_POSE = new Pose2d(new Translation2d(1.775, 4.125), new Rotation2d(0));
    private static final Pose2d RIGHT_HAB_START_POSE = new Pose2d(new Translation2d(1.775, 5.2), new Rotation2d(0));
    private static final Pose2d LEFT_HAB_START_POSE = new Pose2d(new Translation2d(1.775, 3.0), new Rotation2d(0));

    private Pose2d startingPose = CENTER_HAB_START_POSE; // TODO: Set from Dashboard

    private NetworkTableEntry dashboard_leftFPS, dashboard_rightFPS, dashboard_leftFPSPeak, dashboard_rightFPSPeak,
                              dashboard_leftAmps, dashboard_rightAmps, dashboard_leftAmpsPeak, dashboard_rightAmpsPeak,
                              dashboard_totalAmps, dashboard_totalAmpsPeak, dashboard_desiredVelocity,
                              dashboard_leftVelocity, dashboard_rightVelocity, dashboard_leftVelocityError, dashboard_rightVelocityError,
                              dashboard_navxYaw, dashboard_yawOutput, dashboard_isRightDecel, dashboard_isLeftDecel,
                              dashboard_leftPos, dashboard_rightPos;

    private boolean holdYaw;
    private double yawCorrection, yawSetpoint;
    private double desiredRPM;

    private double leftFPS, rightFPS, leftPeakFPS, rightPeakFPS;
    private double leftAmps, rightAmps, leftPeakAmps, rightPeakAmps;
    private double totalAmps, totalPeakAmps;

    private double[] drivePIDF = Constants.DRIVE_PIDF;

    public Drivetrain() {
        super(yawController);
        setName("Drive Subsys");
        DashboardManager.addTab(this);
        DashboardManager.addTabSubsystem(this, this);
    }

    @Override
    public void periodic() {
        leftFPS = Constants.DRIVE_POWERTRAIN.calculateFeetPerSec(leftMaster.getSensorVelocity());
        rightFPS = Constants.DRIVE_POWERTRAIN.calculateFeetPerSec(rightMaster.getSensorVelocity());

        leftAmps = leftMaster.getOutputCurrent() + leftSlave.getOutputCurrent();
        rightAmps = rightMaster.getOutputCurrent() + rightSlave.getOutputCurrent();

        totalAmps = leftAmps + rightAmps;

        if (Math.abs(leftFPS) > leftPeakFPS) leftPeakFPS = Math.abs(leftFPS);
        if (Math.abs(rightFPS) > rightPeakFPS) rightPeakFPS = Math.abs(rightFPS);

        if (leftAmps > leftPeakAmps) leftPeakAmps = leftAmps;
        if (rightAmps > rightPeakAmps) rightPeakAmps = rightAmps;

        if (totalAmps > totalPeakAmps) totalPeakAmps = totalAmps;

        desiredRPM = RobotContainer.drivePad.getY(Hand.kLeft) * Motors.NEO.freeSpeed;

        handleDecelLimiting();
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

        diffDrive = new SmartDifferentialDrive(leftMaster, rightMaster, 5676);

        driveOdometry = new DifferentialDriveOdometry(driveKinematics, startingPose);

        // yawController.setContinuous();
//        yawController.
//        yawController.setInputRange(-180.0, 180.0);
//        yawController.setOutputRange(-0.7, 0.7);
//        yawController.setAbsoluteTolerance(1.5);

        dashboard_leftFPS = DashboardManager.addTabItem(this, "Left FPS", 0.0);
        dashboard_rightFPS = DashboardManager.addTabItem(this, "Right FPS", 0.0);
        dashboard_leftFPSPeak = DashboardManager.addTabItem(this, "Left FPS Peak", 0.0);
        dashboard_rightFPSPeak = DashboardManager.addTabItem(this, "Right FPS Peak", 0.0);
        dashboard_leftAmps = DashboardManager.addTabItem(this, "Left Amps", 0.0);
        dashboard_rightAmps = DashboardManager.addTabItem(this, "Right Amps", 0.0);
        dashboard_leftAmpsPeak = DashboardManager.addTabItem(this, "Left Amps Peak", 0.0);
        dashboard_rightAmpsPeak = DashboardManager.addTabItem(this, "Right Amps Peak", 0.0);
        dashboard_totalAmps = DashboardManager.addTabItem(this, "Total Amps", 0.0);
        dashboard_totalAmpsPeak = DashboardManager.addTabItem(this, "Total Peak Amps", 0.0);
        dashboard_desiredVelocity = DashboardManager.addTabItem(this, "Desired Velocity", 0.0);
        dashboard_leftVelocity = DashboardManager.addTabItem(this, "Left Drive Velocity", 0.0);
        dashboard_rightVelocity = DashboardManager.addTabItem(this, "Right Drive Velocity", 0.0);
        dashboard_leftVelocityError = DashboardManager.addTabItem(this, "Left Velocity Error", 0.0);
        dashboard_rightVelocityError = DashboardManager.addTabItem(this, "Right Velocity Error", 0.0);
        dashboard_navxYaw = DashboardManager.addTabItem(this, "NavX Yaw", 0.0);
        dashboard_yawOutput = DashboardManager.addTabItem(this, "Yaw Output", 0.0);
        dashboard_isRightDecel = DashboardManager.addTabItem(this, "Is Right Decel", false, DashboardWidget.BooleanBox);
        dashboard_isLeftDecel = DashboardManager.addTabItem(this, "Is Left Decel", false, DashboardWidget.BooleanBox);

        dashboard_leftPos = DashboardManager.addTabItem(this, "Left Drive Pos", 0.0);
        dashboard_rightPos = DashboardManager.addTabItem(this, "Right Drive Pos", 0.0);

        RunEndCommand driveCommand = new RunEndCommand(this::drive, this::stop, this);
        driveCommand.setName("TeleDriveCommand");

        navx.zeroYaw();
        resetEncoders();

        setDefaultCommand(driveCommand);

        return success;
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        double leftMetersPerSec = Constants.DRIVE_POWERTRAIN.calculateMetersPerSec(leftMaster.getSensorVelocity());
        double rightMetersPerSec = Constants.DRIVE_POWERTRAIN.calculateMetersPerSec(rightMaster.getSensorVelocity());
        return new DifferentialDriveWheelSpeeds(leftMetersPerSec, rightMetersPerSec);
    }

    public void consumeWheelSpeeds(Double leftWheelSpeedMeters, Double rightWheelSpeedMeters) {
        
    }

    public Pose2d getLatestPose2d() {
        var newRotation2d = new Rotation2d(OscarMath.degreesToRadians(getMeasurement()));
        return driveOdometry.update(newRotation2d, getWheelSpeeds());
    }

    private void handleDecelLimiting() {
        if (isLeftDecel()) {
            leftMaster.setClosedLoopRamp(Constants.DRIVE_DECEL_RAMP);
        } else {
            leftMaster.setClosedLoopRamp(Constants.DRIVE_ACCEL_RAMP);
        }

        if (isRightDecel()) {
            rightMaster.setClosedLoopRamp(Constants.DRIVE_DECEL_RAMP);
        } else {
            rightMaster.setClosedLoopRamp(Constants.DRIVE_ACCEL_RAMP);
        }
    }

    private void drive() {
        double moveStick = -RobotContainer.drivePad.getY(Hand.kLeft);
        double rotStick = RobotContainer.drivePad.getX(Hand.kRight);
        double rotStickMultiplier = 0.6;
        boolean rotHold = RobotContainer.drivePad.rightStickPress.get();

        if (rotHold) {
            if (!holdYaw) {
                yawSetpoint = navx.getYaw();
                holdYaw = true;
                yawController.setSetpoint(yawSetpoint);
            }
        } else {
            holdYaw = false;
        }

        rotStick *= rotStickMultiplier;

        moveStick = OscarMath.signumPow(moveStick, 2);
        rotStick = OscarMath.signumPow(rotStick, 2);

        double rotPow = holdYaw ? yawCorrection : rotStick;

        diffDrive.curvatureDrive(moveStick, rotPow);
    }

    private void stop() {
        diffDrive.stopMotor();
    }

    @Override
    public void useOutput(double output) {
        output = OscarMath.round(output, 2);
        if (!yawController.atSetpoint() && yawController.getPositionError() > 15) {
            output += 0.1;
        }
        // failsafe, if navx goes RIP
        yawCorrection = navx.isConnected() ? output : 0.0;
    }

    @Override
    public double getSetpoint() {
        return yawSetpoint;
    }

    @Override
    public double getMeasurement() {
        return OscarMath.round(navx.getYaw(), 2);
    }

    public boolean isRightDecel() {
        if (!diffDrive.isQuickTurning()) return false;
        return Math.abs(rightMaster.getSensorVelocity()) - Math.abs(desiredRPM) > 0;
    }

    public boolean isLeftDecel() {
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

        dashboard_navxYaw.setDouble(getMeasurement());
        dashboard_yawOutput.setDouble(yawCorrection);

        dashboard_leftAmps.setDouble(leftAmps);
        dashboard_leftAmpsPeak.setDouble(leftPeakAmps);
        dashboard_leftFPS.setDouble(leftFPS);
        dashboard_leftFPSPeak.setDouble(leftPeakFPS);
        dashboard_leftVelocity.setDouble(leftMaster.getSensorVelocity());
        dashboard_leftVelocityError.setDouble(desiredRPM - leftMaster.getSensorVelocity());
        dashboard_leftPos.setDouble(leftMaster.getSensorPosition());

        dashboard_rightAmps.setDouble(rightAmps);
        dashboard_rightAmpsPeak.setDouble(rightPeakAmps);
        dashboard_rightFPS.setDouble(rightFPS);
        dashboard_rightFPSPeak.setDouble(rightPeakFPS);
        dashboard_rightVelocity.setDouble(rightMaster.getSensorVelocity());
        dashboard_rightVelocityError.setDouble(desiredRPM - rightMaster.getSensorVelocity());
        dashboard_rightPos.setDouble(rightMaster.getSensorPosition());

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