package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol.vendor.CANTalon;
import frc.team832.robot.Constants;

public class Jackstand extends SubsystemBase {

	private CANTalon frontJack, backJack;

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

		if (!(frontJack.getInputVoltage() > 0)) successful = false;
		if (!(backJack.getInputVoltage() > 0)) successful = false;

		NeutralMode allIdleMode = NeutralMode.kBrake;
		frontJack.setNeutralMode(allIdleMode);
		backJack.setNeutralMode(allIdleMode);

		frontJack.setkP(Constants.FRONT_JACKSTAND_PIDF[0]);
		frontJack.setkI(Constants.FRONT_JACKSTAND_PIDF[1]);
		frontJack.setkF(Constants.FRONT_JACKSTAND_PIDF[2]);
		frontJack.setkF(Constants.FRONT_JACKSTAND_PIDF[3]);

		backJack.setkP(Constants.BACK_JACKSTAND_PIDF[0]);
		backJack.setkI(Constants.BACK_JACKSTAND_PIDF[1]);
		backJack.setkD(Constants.BACK_JACKSTAND_PIDF[2]);
		backJack.setkF(Constants.BACK_JACKSTAND_PIDF[3]);

		frontJack.setForwardSoftLimit((int)Constants.FRONTJACK_SOFT_MIN);
		frontJack.setReverseSoftLimit((int)Constants.FRONTJACK_SOFT_MAX);

		backJack.setForwardSoftLimit((int)Constants.BACKJACK_SOFT_MIN);
		backJack.setReverseSoftLimit((int)Constants.BACKJACK_SOFT_MAX);

		frontJack.configMotionMagic(Constants.FRONT_JACKSTAND_VELOCITY, Constants.FRONT_JACKSTAND_ACCELERATION);
		backJack.configMotionMagic(Constants.BACK_JACKSTAND_VELOCITY, Constants.BACK_JACKSTAND_ACCELERATION);

		return successful;
	}

	public void setPosition (JackstandPosition position) {
		frontJack.setPosition(position.frontValue);
		backJack.setPosition(position.backValue);
	}

	public boolean frontAtTarget(int range) {
		return Math.abs(frontJack.getClosedLoopTarget() - frontJack.getSensorPosition()) < range;
	}

	public boolean backAtTarget(int range) {
		return Math.abs(backJack.getClosedLoopTarget() - backJack.getSensorPosition()) < range;
	}

	public enum JackstandPosition {
		STARTING(0),
		RETRACTED(-1000),
		LVL2_UP(-30000),
		LVL3_UP(-60000),
		LVL2_FRONT_ON(RETRACTED.frontValue, LVL2_UP.backValue),
		LVL3_FRONT_ON(RETRACTED.frontValue, LVL3_UP.backValue);

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
}