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
		frontJack.setMotionMagic(position.frontValue);
	}

	public void setBackJack(BackJackPosition position) {
		backJack.setMotionMagic(position.backValue);
	}

	public boolean frontAtTarget(int range) {
		return Math.abs(frontJack.getClosedLoopTarget() - frontJack.getSensorPosition()) < range;
	}

	public boolean backAtTarget(int range) {
		return Math.abs(backJack.getClosedLoopTarget() - backJack.getSensorPosition()) < range;
	}

	public enum JackstandType {
		FRONT,
		BACK
	}

	public enum JackstandPosition {
		STARTING(0),
		RETRACTED(-500, -500),
		LVL2_UP(-30000, -29000),
		LVL3_UP(-77000, -76000),
		EXTENDED(-79000, -78500);

		public final int frontValue, backValue;

		JackstandPosition(int value) {
			this.frontValue = value;
			this.backValue = value;
		}

		JackstandPosition(int frontValue, int backValue) {
			this.frontValue = frontValue;
			this.backValue = backValue;
		}
	}

	public enum FrontJackPosition {
		RETRACTED(JackstandPosition.RETRACTED.frontValue),
		LVL2_UP(JackstandPosition.LVL2_UP.frontValue),
		LVL3_UP(JackstandPosition.LVL3_UP.frontValue);

		public final int frontValue;

		FrontJackPosition(int value) {
			this.frontValue = value;
		}
	}

	public enum BackJackPosition {
		RETRACTED(JackstandPosition.RETRACTED.backValue),
		LVL2_UP(JackstandPosition.LVL2_UP.backValue),
		LVL3_UP(JackstandPosition.LVL3_UP.backValue);

		public final int backValue;

		BackJackPosition(int value) {
			this.backValue = value;
		}
	}
}