package frc.team832.robot;

import com.ctre.phoenix.CANifier;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import frc.team832.GrouchLib.Control.OscarPCM;
import frc.team832.GrouchLib.Control.OscarPDP;
import frc.team832.GrouchLib.Mechanisms.OscarComplexMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarLinearMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarRotaryMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarSimpleMechanism;
import frc.team832.GrouchLib.Motors.OscarCANSparkMax;
import frc.team832.GrouchLib.Motion.OscarDiffDrive;
import frc.team832.GrouchLib.Motors.OscarSmartMotorGroup;
import frc.team832.GrouchLib.Sensors.OscarCANifier;

import java.awt.*;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
class RobotMap {

    static class IDs {
        public static final int pdp = 0;
        public static final int pcm = 1;
        public static final int leftMaster = 2;
        public static final int leftSlave = 3;
        public static final int rightMaster = 4;
        public static final int rightSlave = 5;
        public static final int elevator = 6;
        public static final int fourbarMaster = 7;
        public static final int fourbarSlave = 8;
    }

    static OscarPDP pdp;
    static OscarPCM pcm;
    static OscarDiffDrive diffDrive;
    static OscarLinearMechanism elevatorMech;
    static OscarRotaryMechanism fourbarMech;
    static OscarComplexMechanism complexLiftMech;
    static OscarSimpleMechanism cargoIntake;
    static OscarRotaryMechanism hatchHolder;
    static OscarRotaryMechanism hatchGrabbor;
    static OscarCANifier canifier;
    /**
     * Initializes robot hardware
     */
    static void init() {
//        pdp = new OscarPDP(IDs.pdp);
//        pcm = new OscarPCM(IDs.pcm);

        CANSparkMaxLowLevel.MotorType driveMotorType = CANSparkMaxLowLevel.MotorType.kBrushless;

        OscarCANSparkMax leftMaster = new OscarCANSparkMax(IDs.leftMaster, driveMotorType);
        OscarCANSparkMax leftSlave = new OscarCANSparkMax(IDs.leftSlave, driveMotorType);
        OscarCANSparkMax rightMaster = new OscarCANSparkMax(IDs.rightMaster, driveMotorType);
        OscarCANSparkMax rightSlave = new OscarCANSparkMax(IDs.rightSlave, driveMotorType);

        leftSlave.setFollowType(CANSparkMax.ExternalFollower.kFollowerSparkMax);
        rightSlave.setFollowType(CANSparkMax.ExternalFollower.kFollowerSparkMax);

        OscarSmartMotorGroup leftDrive = new OscarSmartMotorGroup(leftMaster, leftSlave);
        OscarSmartMotorGroup rightDrive = new OscarSmartMotorGroup(rightMaster, rightSlave);
        diffDrive = new OscarDiffDrive(leftDrive, rightDrive);

        canifier = new OscarCANifier(0);
        canifier.setLedChannels(CANifier.LEDChannel.LEDChannelB, CANifier.LEDChannel.LEDChannelC, CANifier.LEDChannel.LEDChannelA);
        canifier.setLedVoltage(5);
        canifier.setLedMaxOutput(1);
        canifier.setLedColor(Color.GREEN);

//        canifier.setLedRGB(1, 0, 1);

//        OscarCANTalon elevatorMotor = new OscarCANTalon(IDs.elevator);
//        OscarCANTalon fourbarMaster = new OscarCANTalon(IDs.fourbarMaster);
//        OscarCANVictor fourbarSlave = new OscarCANVictor(IDs.fourbarSlave);
//        OscarSmartMotorGroup fourbarGroup = new OscarSmartMotorGroup(fourbarMaster, fourbarSlave);
//        elevatorMech = new OscarLinearMechanism(elevatorMotor, Elevator.Constants.Positions);
//        fourbarMech = new OscarRotaryMechanism(fourbarGroup, Fourbar.Constants.FourBarPositions);
//        complexLiftMech = new OscarComplexMechanism(elevatorMech, fourbarMech, ComplexLift.Constants.LiftPositions);
    }
}
