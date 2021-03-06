package frc.team832.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol.vendor.CANTalon;
import frc.team832.lib.motorcontrol.vendor.CANVictor;
import frc.team832.robot.Constants;

public class Jackstand extends SubsystemBase {

	private CANTalon frontJack, backJack;
	private CANVictor driveMotor;

	public Jackstand() {
		super();
		SmartDashboard.putData("Jack Subsys", this);
	}

	@Override
	public void periodic(){

	}

	public boolean initialize() {
		boolean successful = true;
		frontJack = new CANTalon(Constants.FRONTJACK_CAN_ID);
		backJack = new CANTalon(Constants.BACKJACK_CAN_ID);
		driveMotor = new CANVictor(Constants.JACK_DRIVE_CAN_ID);

		if (!(frontJack.getInputVoltage() > 0)) successful = false;
		if (!(backJack.getInputVoltage() > 0)) successful = false;
		if (!(driveMotor.getInputVoltage() > 0)) successful = false;

		frontJack.resetSettings();
		backJack.resetSettings();

		frontJack.rezeroSensor();
		backJack.rezeroSensor();

		frontJack.setInverted(false);
		frontJack.setSensorPhase(true);

		backJack.setInverted(false);
		backJack.setSensorPhase(false);

		frontJack.setSensorType(FeedbackDevice.CTRE_MagEncoder_Relative);
		backJack.setSensorType(FeedbackDevice.CTRE_MagEncoder_Relative);

		NeutralMode allIdleMode = NeutralMode.kBrake;
		frontJack.setNeutralMode(allIdleMode);
		backJack.setNeutralMode(allIdleMode);
		driveMotor.setNeutralMode(NeutralMode.kCoast);

		frontJack.setkP(Constants.FRONT_JACKSTAND_PIDF[0]);
		frontJack.setkI(Constants.FRONT_JACKSTAND_PIDF[1]);
		frontJack.setkF(Constants.FRONT_JACKSTAND_PIDF[2]);
		frontJack.setkF(Constants.FRONT_JACKSTAND_PIDF[3]);

		backJack.setkP(Constants.BACK_JACKSTAND_PIDF[0]);
		backJack.setkI(Constants.BACK_JACKSTAND_PIDF[1]);
		backJack.setkD(Constants.BACK_JACKSTAND_PIDF[2]);
		backJack.setkF(Constants.BACK_JACKSTAND_PIDF[3]);

		frontJack.setForwardSoftLimit((int)Constants.FRONTJACK_SOFT_FORWARD);
		frontJack.setReverseSoftLimit((int)Constants.FRONTJACK_SOFT_REVERSE);

		backJack.setForwardSoftLimit((int)Constants.BACKJACK_SOFT_FORWARD);
		backJack.setReverseSoftLimit((int)Constants.BACKJACK_SOFT_REVERSE);

		frontJack.configMotionMagic(Constants.FRONT_JACKSTAND_VELOCITY, Constants.FRONT_JACKSTAND_ACCELERATION);
		backJack.configMotionMagic(Constants.BACK_JACKSTAND_VELOCITY, Constants.BACK_JACKSTAND_ACCELERATION);

		return successful;
	}

	public void setPosition (JackstandPosition position) {
		frontJack.setMotionMagic(position.frontValue);
		backJack.setMotionMagic(position.backValue);
	}

	public void setDrivePower(double power) {
		driveMotor.set(power);
	}

	public void stopDrive() {
		driveMotor.set(0.0);
	}

	public void setFrontJack(FrontJackPosition position) {
		frontJack.setMotionMagic(position.value);
	}

	public boolean isExtending() {
		return frontJack.getClosedLoopTarget() < -25000 && backJack.getClosedLoopTarget() < -25000;
	}

	public void setBackJack(BackJackPosition position) {
		backJack.setMotionMagic(position.value);
	}

	public double getFrontTarget() { return frontJack.getClosedLoopTarget(); }

	public double getBackTarget() { return backJack.getClosedLoopTarget(); }

	public double getFrontPos() { return frontJack.getSensorPosition(); }

	public double getBackPos() { return backJack.getSensorPosition(); }

	public boolean frontAtTarget(int range) {
		return Math.abs(frontJack.getClosedLoopTarget() - frontJack.getSensorPosition()) < range;
	}

	public boolean backAtTarget(int range) {
		return Math.abs(backJack.getClosedLoopTarget() - backJack.getSensorPosition()) < range;
	}

	public boolean atTarget() {
		return frontJack.atTarget() && backJack.atTarget();
	}

	public enum JackstandType {
		FRONT,
		BACK
	}

	public enum JackstandPosition {
		RETRACTED(FrontJackPosition.RETRACTED.value, BackJackPosition.RETRACTED.value),
		LVL2_UP(FrontJackPosition.LVL2_UP.value, BackJackPosition.LVL2_UP.value),
		LVL3_UP(FrontJackPosition.LVL3_UP.value, BackJackPosition.LVL3_UP.value),
		EXTENDED(FrontJackPosition.EXTENDED.value, BackJackPosition.EXTENDED.value);

		public final int frontValue, backValue;

		JackstandPosition(int frontValue, int backValue) {
			this.frontValue = frontValue;
			this.backValue = backValue;
		}
	}

	public enum FrontJackPosition {
		RETRACTED(-500),
		LVL2_UP(-33000),
		LVL3_UP(-77000),
		EXTENDED(-79000);

		public final int value;

		FrontJackPosition(int value) {
			this.value = value;
		}
	}

	public enum BackJackPosition {
		RETRACTED(-500),
		LVL2_UP(-32000),
		LVL3_UP(-76000),
		EXTENDED(-78500);

		public final int value;

		BackJackPosition(int value) {
			this.value = value;
		}
	}
}
