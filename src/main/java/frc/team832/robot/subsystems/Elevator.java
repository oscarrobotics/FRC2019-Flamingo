package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.GrouchLib.motorcontrol.CANTalon;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;
import frc.team832.GrouchLib.util.OscarMath;
import frc.team832.robot.Constants;
import org.opencv.core.Mat;
import frc.team832.robot.RobotContainer;

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

	public double getSlider(){
		return RobotContainer.stratComInterface.getRightSlider();
	}

	public double getSliderTarget(double slider){
		return OscarMath.map(slider, -1.0, 1.0, ElevatorPosition.BOTTOM.value, ElevatorPosition.TOP.value);
	}


	public double getTarget(){
		return elevator.getTargetPosition();
	}

	public void setTarget(ElevatorPosition position) {
		elevator.setPosition(position.value);
	}
	public void setTarget(double position) {
		elevator.setPosition(position);
	}

	public boolean atTarget(){
		return Math.abs(elevator.getTargetPosition() - elevator.getSensorPosition()) <= 50;
	}

	public static enum ElevatorPosition{
		BOTTOM(30),
		TOP(430),
		MIDDLE(ElevatorPosition.BOTTOM.value + (Math.abs(ElevatorPosition.TOP.value - ElevatorPosition.BOTTOM.value) / 2)),
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
