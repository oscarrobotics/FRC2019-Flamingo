package frc.team832.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import frc.team832.GrouchLib.Control.*;
import frc.team832.GrouchLib.Mechanisms.*;
import frc.team832.GrouchLib.Motors.*;
import frc.team832.GrouchLib.Motion.*;
import frc.team832.GrouchLib.OscarCANDevice;
import frc.team832.GrouchLib.Sensors.*;
import frc.team832.GrouchLib.Util.MiniPID;
import frc.team832.robot.Subsystems.*;


/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

    static class IDs {
        public static final int pdp = 0;
        public static final int pcm = 1;
        public static final int leftMaster = 22;
        public static final int leftSlave = 23;
        public static final int rightMaster = 24;
        public static final int rightSlave = 25;
        public static final int elevator = 6;
        public static final int fourbarTop = 7;
        public static final int fourbarBottom = 8;
        public static final int frontJackStand = 9;
        public static final int backJackStand = 10;
        public static final int jackStandDrive = 11;
        public static final int cargoIntake = 12;
        public static final int hatchHolder = 13;
        public static final int hatchGrabbor = 14;
        public static final int canifier = 0;
        public static final int lightPort = 0;
    }

    // Keep organized by subsystem

    static OscarPDP pdp;
    static OscarPCM pcm;

    /** Drivetrain **/
    static OscarCANSparkMax leftMaster;
    static OscarCANSparkMax leftSlave;
    static OscarCANSparkMax rightMaster;
    static OscarCANSparkMax rightSlave;

    static OscarCANSmartMotorGroup leftDrive;
    static OscarCANSmartMotorGroup rightDrive;

    static OscarSmartDiffDrive diffDrive;

    /** Jackstands **/
    static OscarCANTalon frontJackStandMotor;
    static OscarCANTalon backJackStandMotor;
    static OscarLinearMechanism frontJackStand;
    static OscarLinearMechanism backJackStand;

    static OscarCANVictor jackStandDriveMotor;
    static OscarSimpleMechanism jackStandDrive;

    /** Elevator **/
    static OscarCANTalon elevatorMotor;
    static OscarGeniusMechanism elevatorMech;

    /** Fourbars **/
    static OscarCANTalon fourbarTop;
    static OscarCANTalon fourbarBottom;
    // TODO: make combo mech!!!
    static OscarGeniusMechanism fourbarTopMech;
    static OscarGeniusMechanism fourbarBottomMech;

    /** ComplexLift **/
    static OscarGeniusComplexMechanism complexLiftMech;

    /** Snowblower **/
    static OscarCANVictor cargoIntakeMotor;
    static OscarSimpleMechanism cargoIntake;

    static OscarCANVictor hatchHolderMotor;
    static OscarSimpleMechanism hatchHolder;

    static OscarCANTalon hatchGrabborMotor;
    static OscarRotaryMechanism hatchGrabbor;

    static OscarCANifier canifier;

    /**
     * Initializes robot hardware
     */
    static boolean init() {
        /**
         * EXTREMELY IMPORTANT!!!!!!
         * INITIALIZE ALL CAN DEVICES BEFORE ACCESSING THEM
         * This way we can check for missing devices without WPILib having a fit.
         * Don't worry, we'll have our own fit...
         **/

        // SHOULD be CAN-safe (shouldn't suicide if not connected)
        pdp = new OscarPDP(IDs.pdp);
        // SHOULD be CAN-safe (shouldn't suicide if not connected)
        pcm = new OscarPCM(IDs.pcm);

        CANSparkMaxLowLevel.MotorType driveMotorType = CANSparkMaxLowLevel.MotorType.kBrushless;
        try {
            leftMaster = new OscarCANSparkMax(IDs.leftMaster, driveMotorType);
            leftSlave = new OscarCANSparkMax(IDs.leftSlave, driveMotorType);
            rightMaster = new OscarCANSparkMax(IDs.rightMaster, driveMotorType);
            rightSlave = new OscarCANSparkMax(IDs.rightSlave, driveMotorType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        elevatorMotor = new OscarCANTalon(IDs.elevator);
        fourbarTop = new OscarCANTalon(IDs.fourbarTop);
        fourbarBottom = new OscarCANTalon(IDs.fourbarBottom);

        frontJackStandMotor = new OscarCANTalon(IDs.frontJackStand);
        backJackStandMotor = new OscarCANTalon(IDs.backJackStand);
        jackStandDriveMotor = new OscarCANVictor(IDs.jackStandDrive);
        jackStandDrive = new OscarSimpleMechanism(jackStandDriveMotor);

        // not yet added, and not CAN-safe
         cargoIntakeMotor = new OscarCANVictor(IDs.cargoIntake);
         hatchHolderMotor = new OscarCANVictor(IDs.hatchHolder);
        // hatchGrabborMotor = new OscarCANTalon(IDs.hatchGrabbor);

        // SHOULD be CAN-safe (shouldn't suicide if not connected)
        canifier = new OscarCANifier(0);

        // print out all CAN devices
        if (!printCANDeviceStatus()) {
            System.out.println("Missing CAN Device(s)!");
//            return false;
        }
        /** Configuration and Mechanism Creation **/

        leftSlave.setFollowType(CANSparkMax.ExternalFollower.kFollowerSparkMax);
        rightSlave.setFollowType(CANSparkMax.ExternalFollower.kFollowerSparkMax);
        leftSlave.getInstance().follow(leftMaster.getInstance());
        rightSlave.getInstance().follow(rightMaster.getInstance());

        leftMaster.setOutputRange(-1.0, 1.0);
        leftSlave.setOutputRange(-1.0, 1.0);
        rightMaster.setOutputRange(-1.0, 1.0);
        rightSlave.setOutputRange(-1.0, 1.0);

        leftMaster.setkP(.000025);
        rightMaster.setkP(.000025);

        leftDrive = new OscarCANSmartMotorGroup(leftMaster, leftSlave);
        rightDrive = new OscarCANSmartMotorGroup(rightMaster, rightSlave);
        diffDrive = new OscarSmartDiffDrive(leftDrive, rightDrive, 5700);

        leftDrive.setClosedLoopRamp(0.0);
        rightDrive.setClosedLoopRamp(0.0);

        leftDrive.setNeutralMode(NeutralMode.Brake);
        rightDrive.setNeutralMode(NeutralMode.Brake);

//        canifier.setLedChannels(CANifier.LEDChannel.LEDChannelB, CANifier.LEDChannel.LEDChannelC, CANifier.LEDChannel.LEDChannelA);
//        canifier.setLedMaxOutput(1);
//        canifier.setLedColor(Color.GREEN);
//        canifier.setLedRGB(1, 0, 1);

        fourbarTop.setSensorType(FeedbackDevice.Analog);
        fourbarTop.setNeutralMode(NeutralMode.Brake);
        fourbarBottom.setSensorType(FeedbackDevice.Analog);
        fourbarBottom.setNeutralMode(NeutralMode.Brake);
        fourbarBottom.setInverted(false);
//        fourbarBottom.setSensorPhase(true);

        frontJackStandMotor.setNeutralMode(NeutralMode.Brake);
        backJackStandMotor.setNeutralMode(NeutralMode.Brake);
        jackStandDriveMotor.setNeutralMode(NeutralMode.Coast);
        frontJackStandMotor.setSensorType(FeedbackDevice.CTRE_MagEncoder_Relative);
        backJackStandMotor.setSensorType(FeedbackDevice.CTRE_MagEncoder_Relative);

        backJackStandMotor.setInverted(true);
        backJackStandMotor.setPeakOutputForward(.2);
        backJackStandMotor.setPeakOutputReverse(-.2);
        frontJackStandMotor.setPeakOutputForward(.2);
        frontJackStandMotor.setPeakOutputReverse(-.2);

        frontJackStandMotor.setUpperLimit(78500);
        backJackStandMotor.setUpperLimit(78500);
        frontJackStandMotor.setLowerLimit(0);
        backJackStandMotor.setLowerLimit(0);

        elevatorMotor.setSensorType(FeedbackDevice.Analog);
        elevatorMotor.setNeutralMode(NeutralMode.Brake);
        elevatorMotor.setInverted(true);

        fourbarTopMech = new OscarGeniusMechanism(fourbarTop, Fourbar.Constants.Positions);
        fourbarBottomMech = new OscarGeniusMechanism(fourbarBottom, Fourbar.Constants.Positions);

        fourbarTopMech.setUpperLimit(680);
        fourbarTopMech.setLowerLimit(164);
        fourbarBottomMech.setUpperLimit(915);
        fourbarBottomMech.setLowerLimit(200);

        fourbarTopMech.setPIDF(8,0,0, 0);

        elevatorMech = new OscarGeniusMechanism(elevatorMotor, Elevator.Constants.Positions);
        elevatorMech.setPIDF(8, 0, 0, 0);

        elevatorMech.setUpperLimit(350);
        elevatorMech.setLowerLimit(725);

        complexLiftMech = new OscarGeniusComplexMechanism(elevatorMech, fourbarTopMech, ComplexLift.Constants.Positions);
        frontJackStand = new OscarLinearMechanism(frontJackStandMotor, JackStands.Constants.Positions);
        backJackStand = new OscarLinearMechanism(backJackStandMotor, JackStands.Constants.Positions);
        jackStandDrive = new OscarSimpleMechanism(jackStandDriveMotor);
        frontJackStand.setPID(1.0, 0,0);
        backJackStand.setPID(1.0, 0,0);
        frontJackStandMotor.setSensorPhase(true);
        backJackStandMotor.setSensorPhase(true);

//        hatchHolder = new OscarSimpleMechanism(hatchHolderMotor);

        System.out.println("Finish INIT");
        // If we got this far, we're doing pretty good
        return true;
    }

    private static boolean printCANDeviceStatus() {
        StringBuilder deviceList = new StringBuilder("CAN Device Statuses:\n");
        for (OscarCANDevice canDevice : OscarCANDevice.getDevices()) {
            String str = "\t" + canDevice.toString() + "\n";
            deviceList.append(str);
        }
        System.out.println(deviceList.toString());
        return OscarCANDevice.hasMissingDevices();
    }
}
