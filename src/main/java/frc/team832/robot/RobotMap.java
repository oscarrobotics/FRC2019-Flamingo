package frc.team832.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Filesystem;
import frc.team832.GrouchLib.Control.*;
import frc.team832.GrouchLib.Mechanisms.*;
import frc.team832.GrouchLib.Motors.*;
import frc.team832.GrouchLib.Motion.*;
import frc.team832.GrouchLib.CANDevice;
import frc.team832.GrouchLib.Sensors.*;
import frc.team832.GrouchLib.Util.MiniPID;
import frc.team832.robot.Subsystems.*;

import java.awt.*;
import java.io.File;

import static com.ctre.phoenix.CANifier.LEDChannel.*;


/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	static class IDs {
		@SuppressWarnings("WeakerAccess")
		static class CAN {
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
		}

		static class PCM {
			public static final int lightPort = 0;
		}
	}

	// Keep organized by subsystem

	public static PDP pdp;
	/*
	SLOT ASSIGNMENTS

		COMP
							0        15
							1        14
							2        13
							3        12
							4        11
							5        10
							6        9
							7        8
		PRAC
				LeftDrive1	0        15	RightDrive1
				LeftDrive2	1        14	RightDrive2
			 BackJackstand	2        13
					   N/A  3        12
							4        11
							5        10
							6        9
							7        8


	 */


	public static PCM pcm;

	/** Drivetrain **/
	public static CANSparkMax leftMaster;
	public static CANSparkMax leftSlave;
	public static CANSparkMax rightMaster;
	public static CANSparkMax rightSlave;

	public static CANSmartMotorGroup leftDrive;
	public static CANSmartMotorGroup rightDrive;

	public static SmartDifferentialDrive diffDrive;

	/** Jackstands **/
	public static CANTalon frontJackStandMotor;
	public static CANTalon backJackStandMotor;
	public static LinearMechanism frontJackStand;
	public static LinearMechanism backJackStand;

	public static CANVictor jackStandDriveMotor;
	public static SimpleMechanism jackStandDrive;

	/** Elevator **/
	public static CANTalon elevatorMotor;
	public static GeniusMechanism elevatorMech;

	/** Fourbars **/
	public static CANTalon fourbarTop;
	public static CANTalon fourbarBottom;
	public static GeniusMechanism fourbarTopMech;

	/** ComplexLift **/
	static GeniusComplexMechanism complexLiftMech;

	/** Snowblower **/
	public static CANVictor cargoIntakeMotor;
	static SimpleMechanism cargoIntake;

	static CANVictor hatchHolderMotor;
	static SimpleMechanism hatchHolder;
	static MiniPID hatchHolderPID;

	static CANTalon hatchGrabborMotor;
	static SimplySmartMotor hatchHolderSmartMotor;
	static RotaryMechanism hatchGrabbor;

	static CANifier canifier;

	public static NavXMicro navX;

	static Solenoid visionLight;

	public static boolean isComp = false;

	/**
	 * Initializes robot hardware
	 */
	static boolean init () {
		// check which robot we're on...
		String path = String.format("%s/practice", Filesystem.getOperatingDirectory());
		try {
			isComp = !(new File(path).exists());
		} catch (Exception e) {
			isComp = true;
		}

		CameraServer.getInstance().startAutomaticCapture(0);

		/**
		 * EXTREMELY IMPORTANT!!!!!!
		 * INITIALIZE ALL CAN DEVICES BEFORE ACCESSING THEM
		 * This way we can check for missing devices without WPILib having a fit.
		 * Don't worry, we'll have our own fit...
		 */

		// SHOULD be CAN-safe (shouldn't suicide if not connected)
		pdp = new PDP(IDs.CAN.pdp);
		// SHOULD be CAN-safe (shouldn't suicide if not connected) lol you wish
//		pcm = new PCM(IDs.CAN.pcm);
		// SHOULD be CAN-safe (shouldn't suicide if not connected)
		canifier = new CANifier(0);

		canifier.setLedChannels(LEDChannelC, LEDChannelB, LEDChannelA);
		canifier.setLedMaxOutput(0.3);

		// NO PCM
//		visionLight = new Solenoid(pcm, IDs.PCM.lightPort);

		CANSparkMaxLowLevel.MotorType driveMotorType = CANSparkMaxLowLevel.MotorType.kBrushless;
		leftMaster = new CANSparkMax(IDs.CAN.leftMaster, driveMotorType);
		leftSlave = new CANSparkMax(IDs.CAN.leftSlave, driveMotorType);
		rightMaster = new CANSparkMax(IDs.CAN.rightMaster, driveMotorType);
		rightSlave = new CANSparkMax(IDs.CAN.rightSlave, driveMotorType);

		elevatorMotor = new CANTalon(IDs.CAN.elevator);
		fourbarTop = new CANTalon(IDs.CAN.fourbarTop);
		fourbarBottom = new CANTalon(IDs.CAN.fourbarBottom);

		frontJackStandMotor = new CANTalon(IDs.CAN.frontJackStand);
		backJackStandMotor = new CANTalon(IDs.CAN.backJackStand);
		jackStandDriveMotor = new CANVictor(IDs.CAN.jackStandDrive);
		jackStandDrive = new SimpleMechanism(jackStandDriveMotor);

		cargoIntakeMotor = new CANVictor(IDs.CAN.cargoIntake);
		hatchHolderMotor = new CANVictor(IDs.CAN.hatchHolder);
		// not yet added, and not CAN-safe
		// hatchGrabborMotor = new OscarCANTalon(IDs.CAN.hatchGrabbor);

		hatchHolderPID = new MiniPID(1, 0, 0);
		hatchHolderSmartMotor = new SimplySmartMotor(hatchHolderMotor, new RemoteEncoder(canifier));


		fourbarTop.resetConfig();
		fourbarBottom.resetConfig();
		// print out all CAN devices
		if (!printCANDeviceStatus()) {
			System.out.println("Missing CAN Device(s)!");
		}
		/** Configuration and Mechanism Creation **/

		leftSlave.setFollowType(com.revrobotics.CANSparkMax.ExternalFollower.kFollowerSparkMax);
		rightSlave.setFollowType(com.revrobotics.CANSparkMax.ExternalFollower.kFollowerSparkMax);
		leftSlave.getInstance().follow(leftMaster.getInstance());
		rightSlave.getInstance().follow(rightMaster.getInstance());

		int stallCurrent = 60;
		int freeCurrent = 40;

		leftMaster.getInstance().setSmartCurrentLimit(stallCurrent, freeCurrent);
		leftSlave.getInstance().setSmartCurrentLimit(stallCurrent, freeCurrent);
		rightMaster.getInstance().setSmartCurrentLimit(stallCurrent, freeCurrent);
		rightSlave.getInstance().setSmartCurrentLimit(stallCurrent, freeCurrent);




		leftMaster.setOutputRange(-1.0, 1.0);
		leftSlave.setOutputRange(-1.0, 1.0);
		rightMaster.setOutputRange(-1.0, 1.0);
		rightSlave.setOutputRange(-1.0, 1.0);

		leftMaster.setkP(.00025);
		rightMaster.setkP(.00025);

		leftDrive = new CANSmartMotorGroup(leftMaster, leftSlave);
		rightDrive = new CANSmartMotorGroup(rightMaster, rightSlave);

		leftDrive.setNeutralMode(NeutralMode.Brake);
		rightDrive.setNeutralMode(NeutralMode.Brake);

		diffDrive = new SmartDifferentialDrive(leftDrive, rightDrive, 5500);

		hatchHolder = new SimpleMechanism(hatchHolderMotor);

		fourbarBottom.follow(fourbarTop);

		fourbarTop.setInverted(false);
		fourbarBottom.setInverted(true);

		fourbarTop.setSensorPhase(isComp ? true : true);
		fourbarBottom.setSensorPhase(false);

		fourbarTop.configureContinuousCurrent(40);
		fourbarBottom.configureContinuousCurrent(40);

		fourbarTop.setNeutralMode(NeutralMode.Brake);
		fourbarBottom.setNeutralMode(NeutralMode.Brake);

		fourbarTop.setSensorType(FeedbackDevice.CTRE_MagEncoder_Relative);
		fourbarTop.setClosedLoopRamp(.3);
		fourbarTop.configMotionMagic(1000, 3500);
		fourbarTop.setForwardSoftLimit(isComp ? (int) Fourbar.Constants.COMP_TOP_MAX_VAL : (int) Fourbar.Constants.TOP_MAX_VAL);
		fourbarTop.setReverseSoftLimit(isComp ? (int) Fourbar.Constants.COMP_TOP_MIN_VAL : (int) Fourbar.Constants.TOP_MIN_VAL);

		frontJackStandMotor.setNeutralMode(NeutralMode.Brake);
		backJackStandMotor.setNeutralMode(NeutralMode.Brake);
		jackStandDriveMotor.setNeutralMode(NeutralMode.Coast);

		frontJackStandMotor.setSensorType(FeedbackDevice.CTRE_MagEncoder_Relative);
		backJackStandMotor.setSensorType(FeedbackDevice.CTRE_MagEncoder_Relative);

		frontJackStandMotor.setInverted(false);
		backJackStandMotor.setInverted(true);

		frontJackStandMotor.setPeakOutputForward(1);
		backJackStandMotor.setPeakOutputForward(1);

		frontJackStandMotor.setPeakOutputReverse(-1);
		backJackStandMotor.setPeakOutputReverse(-1);

		frontJackStandMotor.setForwardSoftLimit(0);
		backJackStandMotor.setForwardSoftLimit(0);

		frontJackStandMotor.setReverseSoftLimit(RobotMap.isComp ? -75000 : -82000);
		backJackStandMotor.setReverseSoftLimit(RobotMap.isComp ? -75000 : -78000);

		frontJackStandMotor.setSensorPhase(true);
		backJackStandMotor.setSensorPhase(true);
		frontJackStandMotor.configMotionMagic(12000, 12000);
		backJackStandMotor.configMotionMagic(12000, 12000);

		elevatorMotor.setSensorType(FeedbackDevice.Analog);
		elevatorMotor.setNeutralMode(NeutralMode.Brake);
		elevatorMotor.setInverted(isComp ? false : true);
		elevatorMotor.setSensorPhase(isComp ? false : false);
		elevatorMotor.configMotionMagic(500, 1500);

		fourbarTopMech = new GeniusMechanism(fourbarTop, Fourbar.Constants.Positions);
		fourbarTopMech.setPIDF(.5, 0.0, 0.0, .02);

		elevatorMech = new GeniusMechanism(elevatorMotor, Elevator.Constants.Positions);
		elevatorMech.setPIDF(8, 0, 0, 0);//was 16
		// TODO: Actually tune this heckin thing!!

		elevatorMotor.setForwardSoftLimit(isComp ? 430 : -365);
		elevatorMotor.setReverseSoftLimit(isComp ? 30 : -720);

		cargoIntakeMotor.setNeutralMode(NeutralMode.Brake);

		cargoIntake = new SimpleMechanism(cargoIntakeMotor);

		complexLiftMech = new GeniusComplexMechanism(elevatorMech, fourbarTopMech, ComplexLift.Constants.Positions);
		frontJackStand = new LinearMechanism(frontJackStandMotor, JackStands.Constants.Positions);
		backJackStand = new LinearMechanism(backJackStandMotor, JackStands.Constants.Positions);
		jackStandDrive = new SimpleMechanism(jackStandDriveMotor);

		frontJackStand.setPIDF(.1, 0, 0, 0);
		backJackStand.setPIDF(.1, 0, 0, 0);

		navX = new NavXMicro(NavXMicro.NavXPort.USB_onboard);
		navX.init();
		System.out.println("Finish INIT");
		// If we got this far, we're doing pretty good
		return true;
	}

	private static boolean printCANDeviceStatus () {
		StringBuilder deviceList = new StringBuilder("CAN Device Statuses:\n");
		for (CANDevice canDevice : CANDevice.getDevices()) {
			String str = "\t" + canDevice.toString() + "\n";
			deviceList.append(str);
		}
		System.out.println(deviceList.toString());
		return CANDevice.hasMissingDevices();
	}
}
