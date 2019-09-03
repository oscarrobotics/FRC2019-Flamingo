package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj.frc2.command.SendableSubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.motorcontrol.CANTalon;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;
import frc.team832.robot.Constants;

import static frc.team832.robot.Constants.backJackstandPos;
import static frc.team832.robot.Constants.frontJackstandPos;

public class Jackstand extends SendableSubsystemBase {

	private static Jackstand instance;
	private static CANTalon frontJack, backJack;

	public static Jackstand getInstance() {
		if (instance == null) {
			instance = new Jackstand();
		}
		return instance;
	}

	private Jackstand() {
		super();
		SmartDashboard.putData("Jack Subsys", this);
	}

	@Override
	public void periodic(){

	}

	public boolean initialize() {
		boolean successful = true;
		frontJack = new CANTalon(9);
		backJack = new CANTalon(10);

		if (!(frontJack.getInputVoltage() > 0)) successful = false;
		if (!(backJack.getInputVoltage() > 0)) successful = false;

		NeutralMode allIdleMode = NeutralMode.kBrake;
		frontJack.setNeutralMode(allIdleMode);
		backJack.setNeutralMode(allIdleMode);

		frontJack.setkP(Constants.frontJackstandPIDF[0]);
		frontJack.setkI(Constants.frontJackstandPIDF[1]);
		frontJack.setkF(Constants.frontJackstandPIDF[2]);
		frontJack.setkF(Constants.frontJackstandPIDF[3]);

		backJack.setkP(Constants.backJackstandPIDF[0]);
		backJack.setkI(Constants.backJackstandPIDF[1]);
		backJack.setkD(Constants.backJackstandPIDF[2]);
		backJack.setkF(Constants.backJackstandPIDF[3]);

		return successful;
	}

	public static void jackstandExtend() {
		frontJack.setPosition(frontJackstandPos[1]);
		backJack.setPosition(backJackstandPos[1]);
	}

	public static void jackstandRetract() {
		frontJack.setPosition(frontJackstandPos[0]);
		backJack.setPosition(backJackstandPos[0]);
	}

	public static void frontJackstandHold() {
		frontJack.setPosition(frontJack.getSensorPosition());
	}

	public static void backJackstandHold() {
		backJack.setPosition(backJack.getSensorPosition());
	}

	public static boolean frontAtTarget() {
		return frontJack.atTarget();
	}

	public static boolean backAtTarget() {
		return backJack.atTarget();
	}

	public static boolean frontAtTarget(int range) {
		return Math.abs(frontJack.getTargetPosition() - frontJack.getSensorPosition()) < range;
	}

	public static boolean backAtTarget(int range) {
		return Math.abs(backJack.getTargetPosition() - backJack.getSensorPosition()) < range;
	}
}
