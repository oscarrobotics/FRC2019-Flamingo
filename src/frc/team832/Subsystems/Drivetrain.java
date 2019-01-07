package frc.team832.Subsystems;


import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.OscarLib.IOscarSimpleMotor;
import frc.team832.OscarLib.OscarDiffDrive;

public class Drivetrain extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private OscarDiffDrive m_driveSystem;
    private IOscarSimpleMotor m_hWheel;
    private ControlMode m_ctrlMode;

    private enum ControlMode {
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

    public Drivetrain(OscarDiffDrive driveSystem, IOscarSimpleMotor hWheel) {
        m_driveSystem = driveSystem;
        m_hWheel = hWheel;
    }

    /**
     *
     * @param mode The {@link DriveMode} to run inputs through
     * @param stick1 X-Axis translation for Arcade and Curvature, left side for Tank
     * @param stick2 Z-Axis rotation for Arcade and Curvature, right side for Tank
     * @param stick3 Y-Axis translation
     */
    public void teleopControl(DriveMode mode, double stick1, double stick2, double stick3) {
        switch(mode) {
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

        m_hWheel.set(stick3);
    }

    public void setControlMode(ControlMode mode) {
        m_ctrlMode = mode;
    }

    public void initDefaultCommand() {
        // TODO: Set the default command, if any, for a subsystem here. Example:
        //    setDefaultCommand(new MySpecialCommand());
    }
}

