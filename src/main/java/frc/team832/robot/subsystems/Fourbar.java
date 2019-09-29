package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.motorcontrol.CANTalon;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;
import frc.team832.GrouchLib.util.OscarMath;
import frc.team832.robot.Constants;
import frc.team832.robot.Robot;

import static frc.team832.robot.RobotContainer.elevator;

public class Fourbar extends SubsystemBase
{
	private CANTalon fourbarTop, fourbarBottom;

	public Fourbar() {
		super();
		SmartDashboard.putData("Fourbar Subsys", this);
	}

	@Override
	public void periodic(){}

	public boolean initialize(){
		boolean successful = true;
		fourbarTop = new CANTalon(Constants.FOURBARTOP_CAN_ID);
		fourbarBottom = new CANTalon(Constants.FOURBARBOTTOM_CAN_ID);

		if (!(fourbarTop.getInputVoltage() > 0)) successful = false;
		if (!(fourbarBottom.getInputVoltage() > 0)) successful = false;

		NeutralMode allIdleMode = NeutralMode.kBrake;
		fourbarTop.setNeutralMode(allIdleMode);
		fourbarBottom.setNeutralMode(allIdleMode);

		fourbarBottom.follow(fourbarTop);
		fourbarBottom.setInverted(true);//does one need to be inverted?

		//setDefaultCommand(new)

		return successful;
	}

	public int getMinSafePos () {
		double offset;

		double fourbarMinAngle = (65.5 * (Math.cos((elevator.getTarget() - 35)/(248))) - 65.5);
		SmartDashboard.putNumber("Safe Degrees", fourbarMinAngle);

//		if (fourbarTop.getTopCurrentPosition() < 1000){
//			offset = 700;
//		} else if (Robot.fourbar.getTopCurrentPosition() < 1500){
//			offset = 400;
//		}
		if (fourbarTop.getSensorPosition() < 2000){
			offset = (700000 / fourbarTop.getSensorPosition()) - 100;
		} else {
			offset = -100;
		}

		//RobotMap.isComp ? (-(-0.0146 * Math.pow(Robot.elevator.getTargetPosition(), 2)) - (16.5 * Robot.elevator.getTargetPosition() - 6000)) / 2 + 100 : (-0.015 * Math.pow(Robot.elevator.getTargetPosition(), 2)) - (25.0 * Robot.elevator.getTargetPosition()) - 6950;//5800 ish

		double fourbarMinPos = OscarMath.map(fourbarMinAngle, -65.5, 0, 0, Elevator.ElevatorPosition.MIDDLE.value) + offset;
		SmartDashboard.putNumber("Min Safe Val: ", fourbarMinPos);
		return (int) fourbarMinPos;
	}


	public void setTarget(FourbarPosition position) {
		fourbarTop.setPosition(position.value);
	}

	public double posToDeg(double pos){
		//bottom = -70
		//top = 55
		return OscarMath.map(pos, FourbarPosition.BOTTOM.value, FourbarPosition.TOP.value,-70, 55);
	}

	public static enum FourbarPosition{
		BOTTOM(0),
		MIDDLE(2600),
		TOP(5000),
		INTAKEHATCH(MIDDLE.value),
		INTAKECARGO(3100),
		CARGOSHIP_HATCH(MIDDLE.value),
		CARGOSHIP_CARGO(3600),
		ROCKETHATCH_LOW(MIDDLE.value),
		ROCKETHATCH_MID(TOP.value),
		ROCKETHATCH_HIGH(TOP.value),
		ROCKETCARGO_LOW(MIDDLE.value),
		ROCKETCARGO_MID(4500),
		ROCKETCARGO_HIGH(4500)
		;

		public final int value;

		FourbarPosition(int value){
			this.value = value;
		}
	}
}
