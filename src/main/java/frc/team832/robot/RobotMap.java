package frc.team832.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMaxLowLevel;
import frc.team832.GrouchLib.Control.*;
import frc.team832.GrouchLib.Mechanisms.*;
import frc.team832.GrouchLib.Motors.*;
import frc.team832.GrouchLib.Motion.*;
import frc.team832.GrouchLib.CANDevice;
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

    static PDP pdp;
    static PCM pcm;

    /** Drivetrain **/
    public static CANSparkMax leftMaster;
    public static CANSparkMax leftSlave;
    public static CANSparkMax rightMaster;
    public static CANSparkMax rightSlave;

    static CANSmartMotorGroup leftDrive;
    static CANSmartMotorGroup rightDrive;

    static SmartDifferentialDrive diffDrive;

    /** Jackstands **/
    static CANTalon frontJackStandMotor;
    static CANTalon backJackStandMotor;
    static LinearMechanism frontJackStand;
    static LinearMechanism backJackStand;

    static CANVictor jackStandDriveMotor;
    static SimpleMechanism jackStandDrive;

    /** Elevator **/
    static CANTalon elevatorMotor;
    static GeniusMechanism elevatorMech;

    /** Fourbars **/
    static CANTalon fourbarTop;
    static CANTalon fourbarBottom;
    // TODO: make combo mech!!!
    static GeniusMechanism fourbarTopMech;
    static GeniusMechanism fourbarBottomMech;

    /** ComplexLift **/
    static GeniusComplexMechanism complexLiftMech;

    /** Snowblower **/
    public static CANVictor cargoIntakeMotor;
    static SimpleMechanism cargoIntake;

    static CANVictor hatchHolderMotor;
    static SimpleMechanism hatchHolder;
    static MiniPID hatchHolderPID;

    static CANTalon hatchGrabborMotor;
    static RotaryMechanism hatchGrabbor;

    static CANifier canifier;

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
        pdp = new PDP(IDs.pdp);
        // SHOULD be CAN-safe (shouldn't suicide if not connected)
        pcm = new PCM(IDs.pcm);
        // SHOULD be CAN-safe (shouldn't suicide if not connected)
        canifier = new CANifier(0);

        CANSparkMaxLowLevel.MotorType driveMotorType = CANSparkMaxLowLevel.MotorType.kBrushless;
        try {
            leftMaster = new CANSparkMax(IDs.leftMaster, driveMotorType);
            leftSlave = new CANSparkMax(IDs.leftSlave, driveMotorType);
            rightMaster = new CANSparkMax(IDs.rightMaster, driveMotorType);
            rightSlave = new CANSparkMax(IDs.rightSlave, driveMotorType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        elevatorMotor = new CANTalon(IDs.elevator);
        fourbarTop = new CANTalon(IDs.fourbarTop);
        fourbarBottom = new CANTalon(IDs.fourbarBottom);


        frontJackStandMotor = new CANTalon(IDs.frontJackStand);
        backJackStandMotor = new CANTalon(IDs.backJackStand);
        jackStandDriveMotor = new CANVictor(IDs.jackStandDrive);
        jackStandDrive = new SimpleMechanism(jackStandDriveMotor);

        // not yet added, and not CAN-safe
        cargoIntakeMotor = new CANVictor(IDs.cargoIntake);
        hatchHolderMotor = new CANVictor(IDs.hatchHolder);
        // hatchGrabborMotor = new OscarCANTalon(IDs.hatchGrabbor);

        hatchHolderPID = new MiniPID(1,0,0);

        // print out all CAN devices
        if (!printCANDeviceStatus()) {
            System.out.println("Missing CAN Device(s)!");
//            return false;
        }
        /** Configuration and Mechanism Creation **/

        leftSlave.setFollowType(com.revrobotics.CANSparkMax.ExternalFollower.kFollowerSparkMax);
        rightSlave.setFollowType(com.revrobotics.CANSparkMax.ExternalFollower.kFollowerSparkMax);
        leftSlave.getInstance().follow(leftMaster.getInstance());
        rightSlave.getInstance().follow(rightMaster.getInstance());

        leftMaster.setOutputRange(-1.0, 1.0);
        leftSlave.setOutputRange(-1.0, 1.0);
        rightMaster.setOutputRange(-1.0, 1.0);
        rightSlave.setOutputRange(-1.0, 1.0);

        leftMaster.getInstance().getPIDController().setP(0.0005);
        rightMaster.getInstance().getPIDController().setP(0.0005);

//        leftMaster.setkP(.007);
//        rightMaster.setkP(.007);

        leftDrive = new CANSmartMotorGroup(leftMaster, leftSlave);
        rightDrive = new CANSmartMotorGroup(rightMaster, rightSlave);
        diffDrive = new SmartDifferentialDrive(leftDrive, rightDrive, 5500);

//        leftDrive.setClosedLoopRamp(0.0);
//        rightDrive.setClosedLoopRamp(0.0);

        leftDrive.setNeutralMode(NeutralMode.Brake);
        rightDrive.setNeutralMode(NeutralMode.Brake);

//        canifier.setLedChannels(CANifier.LEDChannel.LEDChannelB, CANifier.LEDChannel.LEDChannelC, CANifier.LEDChannel.LEDChannelA);
//        canifier.setMaxOutput(1);
//        canifier.setColor(Color.GREEN);
//        canifier.setRGB(1, 0, 1);

        hatchHolder = new SimpleMechanism(hatchHolderMotor);

        fourbarTop.setSensorType(FeedbackDevice.Analog);
        fourbarTop.setNeutralMode(NeutralMode.Brake);
        fourbarBottom.setSensorType(FeedbackDevice.Analog);
        fourbarBottom.setNeutralMode(NeutralMode.Brake);
        fourbarBottom.setInverted(false);
        fourbarBottom.setSensorPhase(false);

        frontJackStandMotor.setNeutralMode(NeutralMode.Brake);
        backJackStandMotor.setNeutralMode(NeutralMode.Brake);
        jackStandDriveMotor.setNeutralMode(NeutralMode.Coast);
        frontJackStandMotor.setSensorType(FeedbackDevice.CTRE_MagEncoder_Relative);
        backJackStandMotor.setSensorType(FeedbackDevice.CTRE_MagEncoder_Relative);

        frontJackStandMotor.setInverted(false);
        backJackStandMotor.setInverted(false);
        backJackStandMotor.setPeakOutputForward(.8);
        backJackStandMotor.setPeakOutputReverse(-.8);
        frontJackStandMotor.setPeakOutputForward(.8);
        frontJackStandMotor.setPeakOutputReverse(-.8);

        frontJackStandMotor.setForwardSoftLimit(0);
        backJackStandMotor.setForwardSoftLimit(0);
        frontJackStandMotor.setReverseSoftLimit(JackStands.Constants.ENC_MIN_VAL);
        backJackStandMotor.setReverseSoftLimit(JackStands.Constants.ENC_MIN_VAL+50000);

        elevatorMotor.setSensorType(FeedbackDevice.Analog);
        elevatorMotor.setNeutralMode(NeutralMode.Brake);
        elevatorMotor.setInverted(true);
        elevatorMotor.setSensorPhase(false);

        fourbarTopMech = new GeniusMechanism(fourbarTop, Fourbar.Constants.Positions);
        fourbarBottomMech = new GeniusMechanism(fourbarBottom, Fourbar.Constants.Positions);

        fourbarTop.setForwardSoftLimit((int)Fourbar.Constants.TOP_MAX_VAL);
        fourbarTop.setReverseSoftLimit((int)Fourbar.Constants.TOP_MIN_VAL);

        fourbarBottom.setForwardSoftLimit((int)Fourbar.Constants.convertUpperToLower(Fourbar.Constants.TOP_MIN_VAL));
        fourbarBottom.setReverseSoftLimit((int)Fourbar.Constants.convertUpperToLower(Fourbar.Constants.TOP_MAX_VAL));

        fourbarTopMech.setPIDF(8,0.0,0.05, 0);
        fourbarBottomMech.setPIDF(8, 0.0, 0.05,0);

        fourbarBottomMech.setIZone(50);

        elevatorMech = new GeniusMechanism(elevatorMotor, Elevator.Constants.Positions);
        elevatorMech.setPIDF(16, 0, 0, 0);

        elevatorMotor.setPeakOutputReverse(-.20);

        elevatorMotor.setForwardSoftLimit(-375);
        elevatorMotor.setReverseSoftLimit(-705);

        cargoIntakeMotor.setNeutralMode(NeutralMode.Brake);

        cargoIntake = new SimpleMechanism(cargoIntakeMotor);

        complexLiftMech = new GeniusComplexMechanism(elevatorMech, fourbarTopMech, ComplexLift.Constants.Positions);
        frontJackStand = new LinearMechanism(frontJackStandMotor, JackStands.Constants.Positions);
        backJackStand = new LinearMechanism(backJackStandMotor, JackStands.Constants.Positions);
        jackStandDrive = new SimpleMechanism(jackStandDriveMotor);
        frontJackStand.setPID(1.0, 0,0);
        backJackStand.setPID(1.0, 0,0);
        frontJackStandMotor.setSensorPhase(true);
        backJackStandMotor.setSensorPhase(true);

        System.out.println("Finish INIT");
        // If we got this far, we're doing pretty good
        return true;
    }

    private static boolean printCANDeviceStatus() {
        StringBuilder deviceList = new StringBuilder("CAN Device Statuses:\n");
        for (CANDevice canDevice : CANDevice.getDevices()) {
            String str = "\t" + canDevice.toString() + "\n";
            deviceList.append(str);
        }
        System.out.println(deviceList.toString());
        return CANDevice.hasMissingDevices();
    }
}
