package frc.team832.robot.Subsystems;


import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Motion.OscarSmartDiffDrive;

public class Drivetrain extends Subsystem {

    private OscarSmartDiffDrive _driveSystem;

   /**
     * Defines different Drive control methods for teleop stick control
     */
    public enum DriveMode {
        ARCADE,
        CURVATURE,
        TANK
    }

    public Drivetrain(OscarSmartDiffDrive driveSystem) {
        _driveSystem = driveSystem;
    }

    /**
     *
     * @param pathMode The {@link DriveMode} to run inputs through.
     * @param loopMode The {@link OscarSmartDiffDrive.LoopMode} to run the motors in.
     * @param stick1 X-Axis translation for Arcade and Curvature, left side for Tank.
     * @param stick2 Z-Axis rotation for Arcade and Curvature, right side for Tank.
     */
    public void teleopControl(double stick1, double stick2, DriveMode pathMode, OscarSmartDiffDrive.LoopMode loopMode) {
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
    }

    public void pushData() {
        SmartDashboard.putNumber("Left Drive output", _driveSystem.getLeftOutput());
        SmartDashboard.putNumber("Right Drive output", _driveSystem.getRightOutput());
        SmartDashboard.putNumber("Left Drive position", _driveSystem.getLeftPosition());
        SmartDashboard.putNumber("Right Drive position", _driveSystem.getRightPosition());
        SmartDashboard.putNumber("Left Drive velocity", _driveSystem.getLeftVelocity());
        SmartDashboard.putNumber("Right Drive velocity", _driveSystem.getRightVelocity());
    }

    public void pullData() {

    }

    @Override
    public void initDefaultCommand() { }
}

