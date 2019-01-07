package frc.team832;

import OscarLib.Motors.OscarCANTalon;
import OscarLib.Motion.OscarDiffDrive;
import OscarLib.Motors.OscarSmartMotorGroup;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

    public static OscarSmartMotorGroup leftDrive, rightDrive;
    public static OscarCANTalon leftMaster, leftSlave;
    public static OscarCANTalon rightMaster, rightSlave;
    public static OscarDiffDrive diffDrive;

    /**
     * Initializes robot hardware
     */
    public static void init() {
        leftMaster = new OscarCANTalon(0);
        leftSlave = new OscarCANTalon(1);
        rightMaster = new OscarCANTalon(2);
        rightSlave = new OscarCANTalon(3);

        leftDrive = new OscarSmartMotorGroup(leftMaster, leftSlave);
        rightDrive = new OscarSmartMotorGroup(rightMaster, rightSlave);

        diffDrive = new OscarDiffDrive(leftDrive, rightDrive);
    }
}
