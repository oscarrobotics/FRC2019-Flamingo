package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.GrouchLib.motorcontrol.CANTalon;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;
import frc.team832.GrouchLib.util.OscarMath;
import frc.team832.robot.Constants;
import frc.team832.robot.RobotContainer;

public class Elevator extends SubsystemBase {
	private CANTalon elevatorMotor;

	public Elevator() {
		super();
		SmartDashboard.putData("Elevator Subsys", this);
	}

	@Override
	public void periodic() {}

	public boolean initialize() {
		boolean successful = true;
		elevatorMotor = new CANTalon(Constants.ELEVATOR_CAN_ID);

		if (!(elevatorMotor.getInputVoltage() > 0)) successful = false;

		NeutralMode allIdleMode = NeutralMode.kBrake;
		elevatorMotor.setNeutralMode(allIdleMode);

		elevatorMotor.setForwardSoftLimit((int)Constants.ELEVATOR_SOFT_MAX);
		elevatorMotor.setReverseSoftLimit((int)Constants.ELEVATOR_SOFT_MIN);

		return successful;
	}

	public double getTarget(){
		return elevatorMotor.getTargetPosition();
	}

	public void setPosition (ElevatorPosition position) {
		elevatorMotor.setPosition(position.value);
	}

	public void moveManual() {
		var sliderPos = RobotContainer.stratComInterface.getRightSlider();
		var mappedSlider = OscarMath.map(sliderPos, -1.0, 1.0, ElevatorPosition.BOTTOM.value, ElevatorPosition.TOP.value);
		elevatorMotor.setPosition(mappedSlider);
	}

	public boolean atTarget(){
		return Math.abs(elevatorMotor.getTargetPosition() - elevatorMotor.getSensorPosition()) <= 50;
	}

	public static enum ElevatorPosition{
		BOTTOM(30),
		TOP(430),
		MIDDLE(OscarMath.mid(BOTTOM.value, TOP.value)),
		INTAKEHATCH(BOTTOM.value),
		INTAKECARGO(BOTTOM.value),
		CARGOSHIP_HATCH(TOP.value),
		CARGOSHIP_CARGO(BOTTOM.value),
		ROCKETHATCH_LOW(TOP.value),
		ROCKETHATCH_MID(BOTTOM.value),
		ROCKETHATCH_HIGH(TOP.value),
		ROCKETCARGO_LOW(BOTTOM.value),
		ROCKETCARGO_MIDDLE(BOTTOM.value),
		ROCKETCARGO_HIGH(TOP.value)
		;

		public final int value;

		ElevatorPosition(int value){
			this.value = value;
		}
	}

	public double PotToInches(double value) { return OscarMath.map(value, ElevatorPosition.BOTTOM.value, ElevatorPosition.TOP.value, 0, 30);}
	public double InchesToPot(double value) { return OscarMath.map(value, 0, 30, ElevatorPosition.BOTTOM.value, ElevatorPosition.TOP.value);}
}
