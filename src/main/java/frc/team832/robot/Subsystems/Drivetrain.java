package frc.team832.robot.Subsystems;


import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Motion.SmartDifferentialDrive;

public class Drivetrain extends Subsystem {

    private SmartDifferentialDrive _driveSystem;

    private double _kP = .00025, _kI, _kD, _kF;

   /**
     * Defines different Drive control methods for teleop stick control
     */
    public enum DriveMode {
        ARCADE,
        CURVATURE,
        TANK
    }

    public Drivetrain(SmartDifferentialDrive driveSystem) {
        _driveSystem = driveSystem;
    }

    /**
     *
     * @param pathMode The {@link DriveMode} to run inputs through.
     * @param loopMode The {@link SmartDifferentialDrive.LoopMode} to run the motors in.
     * @param stick1 X-Axis translation for Arcade and Curvature, left side for Tank.
     * @param stick2 Z-Axis rotation for Arcade and Curvature, right side for Tank.
     */
    public void teleopControl(double stick1, double stick2, DriveMode pathMode, SmartDifferentialDrive.LoopMode loopMode) {
        switch(pathMode) {
            case ARCADE:
                _driveSystem.arcadeDrive(stick1, stick2, false, loopMode);
                break;
            case CURVATURE:
                _driveSystem.curvatureDrive(stick1, stick2, true, loopMode);
                break;
            case TANK:
                _driveSystem.tankDrive(stick1, stick2, false, loopMode);
                break;
        }
    }

    @Override
    public void periodic() {
//        pullData();
    }

    public void setPIDF(double kP, double kI, double kD, double kF){
        _driveSystem.setPIDF(kP, kI, kD, kF);
//        _kP = kP;
//        _kI = kI;
//        _kD = kD;
//        _kF = kF;
    }


    public void pushData() {
        SmartDashboard.putNumber("Left Drive output", _driveSystem.getLeftOutput());
        SmartDashboard.putNumber("Right Drive output", _driveSystem.getRightOutput());
        SmartDashboard.putNumber("Left Drive position", _driveSystem.getLeftPosition());
        SmartDashboard.putNumber("Right Drive position", _driveSystem.getRightPosition());
        SmartDashboard.putNumber("Left Drive velocity", _driveSystem.getLeftVelocity());
        SmartDashboard.putNumber("Right Drive velocity", _driveSystem.getRightVelocity());

        SmartDashboard.putNumber("Drive kP", _kP);
        SmartDashboard.putNumber("Drive kI", _kI);
        SmartDashboard.putNumber("Drive kD", _kD);
        SmartDashboard.putNumber("Drive kF", _kF);

        //pullData();
    }

    public void pullData() {
        // read PID coefficients from SmartDashboard
        double p = SmartDashboard.getNumber("Drive kP", 0.00025);
        double i = SmartDashboard.getNumber("Drive kI", 0);
        double d = SmartDashboard.getNumber("Drive kD", 0);
        double ff = SmartDashboard.getNumber("Drive kF", 0);

        // if PID coefficients on SmartDashboard have changed, write new values to controller
        if ((p != _kP)) {
            _driveSystem.setP(p);
            _kP = p;
        }
        if ((i != _kI)) {
            _driveSystem.setI(i);
            _kI = i;
        }
        if ((d != _kD)) {
            _driveSystem.setD(d);
            _kD = d;
        }
        if ((ff != _kF)) {
            _driveSystem.setF(ff);
            _kF = ff;
        }
//        if ((max != _kMaxOutput) || (min != kMinOutput)) {
//            m_pidController.setOutputRange(min, max);
//            kMinOutput = min;
//            kMaxOutput = max;
//
//        }
    }


    @Override
    public void initDefaultCommand() { }
}

