package frc.team832.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.frc2.command.AsynchronousPIDSubsystem;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Motors.CANSparkMax;
import frc.team832.GrouchLib.Motion.SmartDifferentialDrive;
import frc.team832.GrouchLib.Motors.NeutralMode;
import frc.team832.GrouchLib.Util.OscarMath;
import frc.team832.robot.Constants;
import frc.team832.robot.RobotContainer;

public class Drivetrain extends AsynchronousPIDSubsystem {

    private static Drivetrain instance;
    private static AHRS navx;
    private static CANSparkMax leftMaster, rightMaster, leftSlave, rightSlave;
    private static PIDController yawController = new PIDController(Constants.yawPID[0], Constants.yawPID[1], Constants.yawPID[2]);
    private static SmartDifferentialDrive diffDrive;

    private static double yawCorrection, yawSetpoint;
    private static double rotPower, desiredRPM;

    private static final Object m_lock = new Object();

    private static double[] drivePIDF = Constants.drivePIDF;

    public static Drivetrain getInstance() {
        if(instance == null) {
            instance = new Drivetrain();
        }
        return instance;
    }

    private Drivetrain() {
        super(yawController);
        SmartDashboard.putData("DT Subsys", this);
        SmartDashboard.putData("DT Diff", diffDrive);
        SmartDashboard.putData("DT YawPID", m_runner);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("DesiredVelocity", desiredRPM);
        SmartDashboard.putNumber("DriveVelocity", leftMaster.getSensorVelocity());
        SmartDashboard.putNumber("Yaw Output", yawCorrection);
        SmartDashboard.putNumber("NavX Yaw", getMeasurement());
    }

    public static boolean initialize() {
        boolean good = true;

        navx = new AHRS(I2C.Port.kOnboard);
        if (!navx.isConnected()) {
            good = false;
        }

        leftMaster = new CANSparkMax(Constants.driveIDs[0], CANSparkMaxLowLevel.MotorType.kBrushless);
        leftSlave = new CANSparkMax(Constants.driveIDs[1], CANSparkMaxLowLevel.MotorType.kBrushless);
        rightMaster = new CANSparkMax(Constants.driveIDs[2], CANSparkMaxLowLevel.MotorType.kBrushless);
        rightSlave = new CANSparkMax(Constants.driveIDs[3], CANSparkMaxLowLevel.MotorType.kBrushless);

        if (leftMaster.getInstance().getFirmwareString() == null) good = false;
        if (leftSlave.getInstance().getFirmwareString() == null) good = false;
        if (rightMaster.getInstance().getFirmwareString() == null) good = false;
        if (rightSlave.getInstance().getFirmwareString() == null) good = false;

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

        leftMaster.setClosedLoopRamp(0.25);
        rightMaster.setClosedLoopRamp(0.25);

        diffDrive = new SmartDifferentialDrive(leftMaster, rightMaster, 4600);

        yawController.setContinuous();
        yawController.setInputRange(-180.0, 180.0);
        yawController.setOutputRange(-0.7, 0.7);
        yawController.setAbsoluteTolerance(1.5);

        instance.m_runner.enable();

        return good;
    }

    public static void drive() {
        double moveStick = -RobotContainer.drivePad.getY(Hand.kLeft);
        double rotStick = RobotContainer.drivePad.getX(Hand.kRight);

        rotStick *= 0.7;

        moveStick = OscarMath.signumPow(moveStick, 2);
        rotStick = OscarMath.signumPow(rotStick, 2);

        diffDrive.curvatureDrive(moveStick, rotStick);
    }

    public static void stop() {
        diffDrive.stopMotor();
    }

    @Override
    public void useOutput(double output) {
        synchronized (m_lock) {
            output = OscarMath.round(output, 2);
            if (!yawController.atSetpoint() && yawController.getError() > 15) {
                output += 0.1;
            }
            // failsafe, if navx goes RIP
            yawCorrection = navx.isConnected() ? output : 0.0;
        }
    }

    @Override
    public double getSetpoint() {
        return yawSetpoint;
    }

    @Override
    public double getMeasurement() {
        synchronized (m_lock) {
            return OscarMath.round(navx.getYaw(), 2);
        }
    }
}