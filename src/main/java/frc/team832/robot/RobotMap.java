package frc.team832.robot;

import com.revrobotics.CANSparkMaxLowLevel;
import frc.team832.GrouchLib.Mechanisms.OscarComplexMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarLinearMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarRotaryMechanism;
import frc.team832.GrouchLib.Motors.OscarCANSparkMax;
import frc.team832.GrouchLib.Motors.OscarCANTalon;
import frc.team832.GrouchLib.Motion.OscarDiffDrive;
import frc.team832.GrouchLib.Motors.OscarCANVictor;
import frc.team832.GrouchLib.Motors.OscarSmartMotorGroup;
import frc.team832.robot.Subsystems.ComplexLift;
import frc.team832.robot.Subsystems.Elevator;
import frc.team832.robot.Subsystems.Fourbar;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
class RobotMap {

    static OscarDiffDrive diffDrive;
    static OscarLinearMechanism elevatorMech;
    static OscarRotaryMechanism fourbarMech;
    static OscarComplexMechanism complexLiftMech;
    /**
     * Initializes robot hardware
     */
    static void init() {
        CANSparkMaxLowLevel.MotorType driveMotorType = CANSparkMaxLowLevel.MotorType.kBrushless;

        OscarCANSparkMax leftMaster = new OscarCANSparkMax(2, driveMotorType);
        OscarCANSparkMax leftSlave = new OscarCANSparkMax(3, driveMotorType);
        OscarCANSparkMax rightMaster = new OscarCANSparkMax(4, driveMotorType);
        OscarCANSparkMax rightSlave = new OscarCANSparkMax(5, driveMotorType);

        OscarSmartMotorGroup leftDrive = new OscarSmartMotorGroup(leftMaster, leftSlave);
        OscarSmartMotorGroup rightDrive = new OscarSmartMotorGroup(rightMaster, rightSlave);

        diffDrive = new OscarDiffDrive(leftDrive, rightDrive);

        OscarCANTalon elevatorMotor = new OscarCANTalon(6);
        elevatorMech = new OscarLinearMechanism(elevatorMotor, Elevator.Constants.ElevatorPositions);

        OscarCANTalon fourbarMaster = new OscarCANTalon(7);
        OscarCANVictor fourbarSlave = new OscarCANVictor(8);

        OscarSmartMotorGroup fourbarGroup = new OscarSmartMotorGroup(fourbarMaster, fourbarSlave);
        fourbarMech = new OscarRotaryMechanism(fourbarGroup, Fourbar.Constants.FourBarPositions);

        complexLiftMech = new OscarComplexMechanism(elevatorMech, fourbarMech, ComplexLift.Constants.LiftPositions);
    }
}
