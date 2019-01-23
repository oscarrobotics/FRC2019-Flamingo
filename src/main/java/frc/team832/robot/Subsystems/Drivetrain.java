package frc.team832.robot.Subsystems;


import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Motion.OscarDiffDrive;
import frc.team832.GrouchLib.Motors.IOscarSimpleMotor;

public class Drivetrain extends Subsystem {

    private OscarDiffDrive m_driveSystem;
    private ControlMode m_ctrlMode;

    public enum ControlMode {
        SPEED,
        PERCENTAGE
    }

    /**
     * Defines different Drive control methods for teleop stick control
     */
    public enum DriveMode {
        ARCADE,
        CURVATURE,
        TANK
    }

    public Drivetrain(OscarDiffDrive driveSystem) {
        m_driveSystem = driveSystem;
    }

    /**
     *
     * @param pathMode The {@link DriveMode} to run inputs through
     * @param loopMode The {@Link ControlMode} to use on the drivetraim
     * @param stick1 X-Axis translation for Arcade and Curvature, left side for Tank
     * @param stick2 Z-Axis rotation for Arcade and Curvature, right side for Tank
     * @param stick3 Y-Axis translation
     */
    public void teleopControl(DriveMode pathMode, ControlMode loopMode, double stick1, double stick2, double stick3) {
        switch(pathMode) {
            case ARCADE:
                m_driveSystem.arcadeDrive(stick1, stick2, false);
                break;
            case CURVATURE:
                m_driveSystem.curvatureDrive(stick1, stick2, true);
                break;
            case TANK:
                m_driveSystem.tankDrive(stick1, stick2, false);
                break;
        }
    }

    public void setControlMode(ControlMode mode) {
        m_ctrlMode = mode;
    }

    public void initDefaultCommand() {
        // TODO: Set the default command, if any, for a subsystem here. Example:
        //    setDefaultCommand(new MySpecialCommand());
    }
}

