package frc.team832.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.driverstation.dashboard.DashboardManager;
import frc.team832.GrouchLib.driverstation.dashboard.DashboardUpdatable;
import frc.team832.GrouchLib.motorcontrol.CANTalon;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;
import frc.team832.GrouchLib.util.OscarMath;
import frc.team832.robot.Constants;
import frc.team832.robot.RobotContainer;

import static frc.team832.robot.RobotContainer.elevator;

public class Fourbar extends SubsystemBase implements DashboardUpdatable {
	private CANTalon fourbarTop, fourbarBottom;

	private NetworkTableEntry dashboard_RawPos;
	private NetworkTableEntry dashboard_minSafeAngle;
	private NetworkTableEntry dashboard_minSafePos;

	private double safety_minFourbarAngle;
	private double safety_minFourbarPos;

	public Fourbar () {
		super();
		setName("Fourbar Subsys");
		DashboardManager.addTab(this);
		SmartDashboard.putData("Fourbar Subsys", this);
	}

	@Override
	public void periodic () {
		getMinSafePos();
	}

	public boolean initialize () {
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

		dashboard_RawPos = DashboardManager.addTabItem(this, "Raw Pos", 0.0);
		dashboard_minSafeAngle = DashboardManager.addTabItem(this, "Min Safe Angle", 0.0);
		dashboard_minSafePos = DashboardManager.addTabItem(this, "Min Safe Pos", 0.0);

		fourbarTop.setForwardSoftLimit((int)Constants.FOURBAR_SOFT_MAX);
		fourbarTop.setReverseSoftLimit((int)Constants.FOURBAR_SOFT_MIN);

		return successful;
	}

	public double getRawPosition () {
		return fourbarTop.getSensorPosition();
	}

	public int getMinSafePos () {
		double offset = 0;

		safety_minFourbarAngle = (-Constants.FOURBAR_MIN_ANGLE * (Math.cos((elevator.getTarget() - Elevator.ElevatorPosition.BOTTOM.value) / (248))) + Constants.FOURBAR_MIN_ANGLE);

//		if (fourbarTop.getTopCurrentPosition() < 1000){
//			offset = 700;
//		} else if (Robot.fourbar.getTopCurrentPosition() < 1500){
//			offset = 400;
//		}


		//RobotMap.isComp ? (-(-0.0146 * Math.pow(Robot.elevator.getTargetPosition(), 2)) - (16.5 * Robot.elevator.getTargetPosition() - 6000)) / 2 + 100 : (-0.015 * Math.pow(Robot.elevator.getTargetPosition(), 2)) - (25.0 * Robot.elevator.getTargetPosition()) - 6950;//5800 ish

		safety_minFourbarPos = OscarMath.map(safety_minFourbarAngle, Constants.FOURBAR_MIN_ANGLE, 0, 0, Elevator.ElevatorPosition.MIDDLE.value) + offset;
		return (int) safety_minFourbarPos;
	}

	public double getSlider () {
		return RobotContainer.stratComInterface.getRightSlider();
	}

	public double getSliderTarget (double slider) {
		return OscarMath.map(slider, -1.0, 1.0, FourbarPosition.BOTTOM.value, FourbarPosition.TOP.value);
	}

	public void setPosition (FourbarPosition position) {
		fourbarTop.setPosition(position.value);
	}

	public boolean atTarget () {
		return Math.abs(fourbarTop.getTargetPosition() - fourbarTop.getSensorPosition()) <= 75;
	}

	public double posToDeg (double pos) {
		//bottom = -70
		//top = 55
		return OscarMath.map(pos, FourbarPosition.BOTTOM.value, FourbarPosition.TOP.value, Constants.FOURBAR_MIN_ANGLE, Constants.FOURBAR_MAX_ANGLE);
	}

	@Override
	public String getDashboardTabName () {
		return m_name;
	}

	@Override
	public void updateDashboardData () {
		dashboard_RawPos.setDouble(getRawPosition());
		dashboard_minSafeAngle.setDouble(safety_minFourbarAngle);
		dashboard_minSafePos.setDouble(safety_minFourbarPos);
	}

	public void zeroEncoder () {
		fourbarTop.resetSensor();
	}

	public enum FourbarPosition {
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
		ROCKETCARGO_HIGH(4500);

		public final int value;

		FourbarPosition (int value) {
			this.value = value;
		}
	}
}
