package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.GrouchLib.motorcontrol.CANTalon;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;
import frc.team832.robot.Constants;

public class Jackstand extends SubsystemBase {

	private CANTalon frontJack, backJack;
	private boolean isHolding;

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

	public void setJackstandTarget(JackstandPosition position) {
		isHolding = false;
		frontJack.setPosition(position.frontValue);
		backJack.setPosition(position.backValue);
	}

	public boolean frontAtTarget(int range) {
		return Math.abs(frontJack.getTargetPosition() - frontJack.getSensorPosition()) < range;
	}

	public boolean backAtTarget(int range) {
		return Math.abs(backJack.getTargetPosition() - backJack.getSensorPosition()) < range;
	}

	public static enum JackstandPosition {
		STARTING(0),
		RETRACTED(-1000),
		LVL2_UP(-30000),
		LVL2_FRONT_ON(RETRACTED.frontValue, LVL2_UP.backValue),
		LVL3_UP(-60000),
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
