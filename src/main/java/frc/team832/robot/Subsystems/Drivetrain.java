package frc.team832.robot.Subsystems;


import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Motion.OscarDiffDrive;
import frc.team832.GrouchLib.Motion.OscarSmartDiffDrive;

public class Drivetrain extends Subsystem {

    private OscarSmartDiffDrive _driveSystem;

    public enum LoopMode {
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

    public Drivetrain(OscarSmartDiffDrive driveSystem) {
        _driveSystem = driveSystem;
    }

    /**
     *
     * @param pathMode The {@link DriveMode} to run inputs through.
     * @param loopMode The {@link LoopMode} to run the motors in.
     * @param stick1 X-Axis translation for Arcade and Curvature, left side for Tank.
     * @param stick2 Z-Axis rotation for Arcade and Curvature, right side for Tank.
     */
    public void teleopControl(double stick1, double stick2, DriveMode pathMode, LoopMode loopMode) {
        switch(pathMode) {
            case ARCADE:
                _driveSystem.arcadeDrive(stick1, stick2, false);
                break;
            case CURVATURE:
                _driveSystem.curvatureDrive(stick1, stick2, true);
                break;
            case TANK:
                _driveSystem.tankDrive(stick1, stick2, false);
                break;
        }
    }

    public void test(double stick){

    }

    @Override
    public void initDefaultCommand() { }
}

