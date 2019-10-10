package frc.team832.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.driverstation.dashboard.DashboardUpdatable;
import frc.team832.GrouchLib.motorcontrol.CANSparkMax;
import frc.team832.GrouchLib.motion.SmartDifferentialDrive;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;
import frc.team832.GrouchLib.motors.DTPowerTrain;
import frc.team832.GrouchLib.motors.Gearbox;
import frc.team832.GrouchLib.motors.Motors;
import frc.team832.GrouchLib.util.OscarMath;
import frc.team832.robot.Constants;
import frc.team832.robot.RobotContainer;
import frc.team832.robot.commands.MainDrive;

public class Drivetrain extends PIDSubsystem implements DashboardUpdatable {

    private AHRS navx;
    private CANSparkMax leftMaster, rightMaster, leftSlave, rightSlave;
    private static PIDController yawController = new PIDController(Constants.yawPID[0], Constants.yawPID[1], Constants.yawPID[2]);
    private SmartDifferentialDrive diffDrive;

    private DTPowerTrain dtPowerTrain;

    private boolean holdYaw;
    private double yawCorrection, yawSetpoint;
    private double desiredRPM;

    private double leftFPS, rightFPS, leftPeakFPS, rightPeakFPS;
    private double leftAmps, rightAmps, leftPeakAmps, rightPeakAmps;
    private double totalAmps, totalPeakAmps;

    private final Object m_lock = new Object();

    private double[] drivePIDF = Constants.drivePIDF;

    public Drivetrain() {
        super(yawController);
        setName("Drive Subsys");
        SmartDashboard.putData("DT Subsys", this);
        SmartDashboard.putData("DT YawPID", yawController);
    }

    @Override
    public void periodic() {
//        leftFPS = dtPowerTrain.calculateFeetPerSec(leftMaster.getSensorVelocity());
//        rightFPS = dtPowerTrain.calculateFeetPerSec(rightMaster.getSensorVelocity());

        leftAmps = leftMaster.getOutputCurrent() + leftSlave.getOutputCurrent();
        rightAmps = rightMaster.getOutputCurrent() + rightSlave.getOutputCurrent();

        totalAmps = leftAmps + rightAmps;

        if (Math.abs(leftFPS) > leftPeakFPS) leftPeakFPS = Math.abs(leftFPS);
        if (Math.abs(rightFPS) > rightPeakFPS) rightPeakFPS = Math.abs(rightFPS);

        if (leftAmps > leftPeakAmps) leftPeakAmps = leftAmps;
        if (rightAmps > rightPeakAmps) rightPeakAmps = rightAmps;

        if (totalAmps > totalPeakAmps) totalPeakAmps = totalAmps;

        desiredRPM = RobotContainer.drivePad.getY(Hand.kLeft) * 5676;

        SmartDashboard.putNumber("Left FPS", leftFPS);
        SmartDashboard.putNumber("Right FPS", rightFPS);
        SmartDashboard.putNumber("Left FPS Peak", leftPeakFPS);
        SmartDashboard.putNumber("Right FPS Peak", rightPeakFPS);

        SmartDashboard.putNumber("Left Amps", leftAmps);
        SmartDashboard.putNumber("Right Amps", rightAmps);
        SmartDashboard.putNumber("Left Amps Peak", leftPeakAmps);
        SmartDashboard.putNumber("Right Amps Peak", rightPeakAmps);

        SmartDashboard.putNumber("Total Amps", totalAmps);
        SmartDashboard.putNumber("Total Peak Amps", totalPeakAmps);

        SmartDashboard.putNumber("DesiredVelocity", desiredRPM);
        SmartDashboard.putNumber("DriveVelocity", leftMaster.getSensorVelocity());
        SmartDashboard.putNumber("VelocityError", desiredRPM - rightMaster.getSensorVelocity());
        SmartDashboard.putNumber("Yaw Output", yawCorrection);
        SmartDashboard.putNumber("NavX Yaw", getMeasurement());
    }

    public boolean initialize() {
        boolean good = true;

//        SmartDashboard.putData("DT Diff", diffDrive);


        navx = new AHRS(I2C.Port.kOnboard);
        if (!navx.isConnected()) {
            good = false;
        }

//        dtPowerTrain = new DTPowerTrain(new Gearbox(Constants.dtReductions[0], Constants.dtReductions[1]), Motors.NEO, 2, 6);

        leftMaster = new CANSparkMax(Constants.driveIDs[0], CANSparkMaxLowLevel.MotorType.kBrushless);
        leftSlave = new CANSparkMax(Constants.driveIDs[1], CANSparkMaxLowLevel.MotorType.kBrushless);
        rightMaster = new CANSparkMax(Constants.driveIDs[2], CANSparkMaxLowLevel.MotorType.kBrushless);
        rightSlave = new CANSparkMax(Constants.driveIDs[3], CANSparkMaxLowLevel.MotorType.kBrushless);

        if (leftMaster.getInstance().getFirmwareString() == null) good = false;
        if (leftSlave.getInstance().getFirmwareString() == null) good = false;
        if (rightMaster.getInstance().getFirmwareString() == null) good = false;
        if (rightSlave.getInstance().getFirmwareString() == null) good = false;

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

        leftMaster.setClosedLoopRamp(0.25);
        rightMaster.setClosedLoopRamp(0.25);

        leftMaster.setPeakCurrentLimit(50);
        leftSlave.setPeakCurrentLimit(50);
        rightMaster.setPeakCurrentLimit(50);
        rightSlave.setPeakCurrentLimit(50);

        diffDrive = new SmartDifferentialDrive(leftMaster, rightMaster, 5676);

        // yawController.setContinuous();
        yawController.setInputRange(-180.0, 180.0);
        yawController.setOutputRange(-0.7, 0.7);
        yawController.setAbsoluteTolerance(1.5);

        setDefaultCommand(new MainDrive(this));

        return good;
    }

    public void drive() {
        double moveStick = -RobotContainer.drivePad.getY(Hand.kLeft);
        double rotStick = RobotContainer.drivePad.getX(Hand.kRight);
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

        rotStick *= 0.625;

        moveStick = OscarMath.signumPow(moveStick, 2);
        rotStick = OscarMath.signumPow(rotStick, 2);

        double rotPow = holdYaw ? yawCorrection : rotStick;

        diffDrive.curvatureDrive(moveStick, rotPow);
    }

    public void stop() {
        diffDrive.stopMotor();
    }

    @Override
    public void useOutput(double output) {
        synchronized (m_lock) {
            output = OscarMath.round(output, 2);
            if (!yawController.atSetpoint() && yawController.getPositionError() > 15) {
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

    @Override
    public String getDashboardTabName () {
        return m_name;
    }

    @Override
    public void updateDashboardData () {

    }
}