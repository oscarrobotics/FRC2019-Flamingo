package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.GrouchLib.motorcontrol.CANTalon;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;
import frc.team832.robot.Constants;

public class Elevator extends SubsystemBase {
	private CANTalon elevator;

	public Elevator() {
		super();
		SmartDashboard.putData("Elevator Subsys", this);
	}

	@Override
	public void periodic() {}

	public boolean initialize() {
		boolean successful = true;
		elevator = new CANTalon(Constants.ELEVATOR_CAN_ID);

		if (!(elevator.getInputVoltage() > 0)) successful = false;

		NeutralMode allIdleMode = NeutralMode.kBrake;
		elevator.setNeutralMode(allIdleMode);

		//setDefaultCommand(new);

		return successful;
	}

	public enum ElevatorPosition{
		BOTTOM(30),
		TOP(430),
		MIDDLE(ElevatorPosition.BOTTOM.value + (Math.abs(ElevatorPosition.TOP.value - ElevatorPosition.BOTTOM.value) / 2));

		public final int value;

		ElevatorPosition(int value){
			this.value = value;
		}
	}
}
