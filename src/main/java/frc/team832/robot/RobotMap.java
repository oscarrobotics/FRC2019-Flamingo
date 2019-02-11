package frc.team832.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import frc.team832.GrouchLib.Control.*;
import frc.team832.GrouchLib.Mechanisms.*;
import frc.team832.GrouchLib.Motors.*;
import frc.team832.GrouchLib.Motion.*;
import frc.team832.GrouchLib.Sensors.*;
import frc.team832.robot.Subsystems.ComplexLift;
import frc.team832.robot.Subsystems.Elevator;
import frc.team832.robot.Subsystems.Fourbar;
import frc.team832.robot.Subsystems.JackStands;

import java.util.zip.CheckedOutputStream;

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
        public static final int fourbarTop = 7;
        public static final int fourbarBottom = 8;
        public static final int frontJackStand = 9;
        public static final int backJackStand = 10;
        public static final int jackStandDrive = 11;
        public static final int canifier = 0;
    }

    static OscarPDP pdp;
    static OscarPCM pcm;
    static OscarDiffDrive diffDrive;
    static OscarLinearMechanism elevatorMech;
    static OscarLinearMechanism fourbarTopMech;
    static OscarLinearMechanism fourbarBottomMech;
    static OscarComplexMechanism complexLiftMech;
    static OscarSimpleMechanism cargoIntake;
    static OscarRotaryMechanism hatchHolder;
    static OscarRotaryMechanism hatchGrabbor;
    static OscarLinearMechanism frontJackStand;
    static OscarLinearMechanism backJackStand;
    static OscarSimpleMechanism jackStandDrive;
    static OscarCANifier canifier;
    static OscarCANSmartMotorGroup leftDrive;
    static OscarCANSmartMotorGroup rightDrive;
    static OscarCANSparkMax leftMaster;
    static OscarCANSparkMax leftSlave;
    static OscarCANSparkMax rightMaster;
    static OscarCANSparkMax rightSlave;

    public static OscarCANTalon frontJackStandMotor;
    public static OscarCANTalon backJackStandMotor;


    /**
     * Initializes robot hardware
     */
    static void init() {
        pdp = new OscarPDP(IDs.pdp);

        //pcm = new OscarPCM(IDs.pcm);

        CANSparkMaxLowLevel.MotorType driveMotorType = CANSparkMaxLowLevel.MotorType.kBrushless;

        leftMaster = new OscarCANSparkMax(IDs.leftMaster, driveMotorType);
        leftSlave = new OscarCANSparkMax(IDs.leftSlave, driveMotorType);
        rightMaster = new OscarCANSparkMax(IDs.rightMaster, driveMotorType);
        rightSlave = new OscarCANSparkMax(IDs.rightSlave, driveMotorType);

        leftSlave.setFollowType(CANSparkMax.ExternalFollower.kFollowerSparkMax);
        rightSlave.setFollowType(CANSparkMax.ExternalFollower.kFollowerSparkMax);
        leftSlave.getInstance().follow(leftMaster.getInstance());
        rightSlave.getInstance().follow(rightMaster.getInstance());

        leftMaster.setOutputRange(-1.0, 1.0);
        leftSlave.setOutputRange(-1.0, 1.0);
        rightMaster.setOutputRange(-1.0, 1.0);
        rightSlave.setOutputRange(-1.0, 1.0);

        leftDrive = new OscarCANSmartMotorGroup(leftMaster, leftSlave);
        rightDrive = new OscarCANSmartMotorGroup(rightMaster, rightSlave);
        diffDrive = new OscarDiffDrive(leftDrive, rightDrive);

        leftDrive.setClosedLoopRamp(0);
        rightDrive.setClosedLoopRamp(0);

        leftDrive.setNeutralMode(NeutralMode.Coast);
        rightDrive.setNeutralMode(NeutralMode.Coast);

        leftMaster.setkP(.000025);
        rightMaster.setkP(.000025);

//        canifier = new OscarCANifier(0);
//        canifier.setLedChannels(CANifier.LEDChannel.LEDChannelB, CANifier.LEDChannel.LEDChannelC, CANifier.LEDChannel.LEDChannelA);
//        canifier.setLedVoltage(5);
//        canifier.setLedMaxOutput(1);
//        canifier.setLedColor(Color.GREEN);

//        canifier.setLedRGB(1, 0, 1);

//        OscarCANTalon elevatorMotor = new OscarCANTalon(IDs.elevator);
        OscarCANTalon fourbarTop = new OscarCANTalon(IDs.fourbarTop);
        OscarCANTalon fourbarBottom = new OscarCANTalon(IDs.fourbarBottom);
        frontJackStandMotor = new OscarCANTalon(IDs.frontJackStand);
        backJackStandMotor = new OscarCANTalon(IDs.backJackStand);
        OscarCANVictor jackStandDriveMotor = new OscarCANVictor(IDs.jackStandDrive);

        fourbarTop.setSensor(FeedbackDevice.Analog);
        fourbarTop.setNeutralMode(NeutralMode.Coast);
        fourbarBottom.setSensor(FeedbackDevice.Analog);
        fourbarBottom.setNeutralMode(NeutralMode.Coast);
        fourbarBottom.setInverted(true);
        fourbarBottom.setSensorPhase(true);

        frontJackStandMotor.setNeutralMode(NeutralMode.Brake);
        backJackStandMotor.setNeutralMode(NeutralMode.Brake);
        jackStandDriveMotor.setNeutralMode(NeutralMode.Coast);
        frontJackStandMotor.setSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        backJackStandMotor.setSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        backJackStandMotor.setInverted(true);

        OscarCANTalon elevatorMotor = new OscarCANTalon(IDs.elevator);

        fourbarTopMech = new OscarLinearMechanism(fourbarTop, Fourbar.Constants.Positions);
        fourbarBottomMech = new OscarLinearMechanism(fourbarBottom, Fourbar.Constants.Positions);
        elevatorMech = new OscarLinearMechanism(elevatorMotor, Elevator.Constants.Positions);
//        complexLiftMech = new OscarComplexMechanism(elevatorMech, fourbarMech, ComplexLift.Constants.Positions);
        frontJackStand = new OscarLinearMechanism(frontJackStandMotor, JackStands.Constants.Positions);
        backJackStand = new OscarLinearMechanism(backJackStandMotor, JackStands.Constants.Positions);
//        jackStandDrive = new OscarSimpleMechanism(jackStandDriveMotor);
        frontJackStand.setPID(1.0, 1.0,1.0);
        backJackStand.setPID(1.0, 1.0,1.0);
    }
}
