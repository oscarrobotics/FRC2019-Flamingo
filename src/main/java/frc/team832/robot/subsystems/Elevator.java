package frc.team832.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.lib.driverstation.dashboard.DashboardManager;
import frc.team832.lib.driverstation.dashboard.DashboardUpdatable;
import frc.team832.lib.motorcontrol.vendor.CANTalon;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.util.OscarMath;
import frc.team832.robot.Constants;
import frc.team832.robot.RobotContainer;

public class Elevator extends SubsystemBase implements DashboardUpdatable {
	private CANTalon elevatorMotor;

	private NetworkTableEntry dashboard_RawPos;

	public Elevator() {
		super();
		DashboardManager.addTab(this);
		DashboardManager.addTabSubsystem(this, this);
	}

	@Override
	public void periodic() {}

	public boolean initialize() {
		boolean successful = true;
		elevatorMotor = new CANTalon(Constants.ELEVATOR_CAN_ID);

		if (!(elevatorMotor.getInputVoltage() > 0)) successful = false;

		elevatorMotor.setSensorType(FeedbackDevice.Analog);

		NeutralMode allIdleMode = NeutralMode.kBrake;
		elevatorMotor.setNeutralMode(allIdleMode);

		elevatorMotor.setForwardSoftLimit((int)Constants.ELEVATOR_SOFT_MAX);
		elevatorMotor.setReverseSoftLimit((int)Constants.ELEVATOR_SOFT_MIN);

		dashboard_RawPos = DashboardManager.addTabItem(this, "Raw Pos", 0.0);

		elevatorMotor.setAllowableClosedLoopError(1);

		elevatorMotor.configMotionMagic(Constants.ELEVATOR_VELOCITY, Constants.ELEVATOR_ACCELERATION);

		setPosition(ElevatorPosition.STARTING_CONFIG);

		return successful;
	}


	public double getTarget(){
		return elevatorMotor.getClosedLoopTarget();
	}

	public void setPosition(ElevatorPosition position) {
		elevatorMotor.setPosition(position.value);
	}

	public void moveManual(int targetPos) {
		targetPos = OscarMath.clip(targetPos, ElevatorPosition.BOTTOM.value, ElevatorPosition.TOP.value);
		elevatorMotor.setMotionMagicArbFF(targetPos, Constants.ELEVATOR_ARBFF);
	}

	public boolean atTarget() {
		return elevatorMotor.atTarget();
	}

	public double ticksToInches() {
		return OscarMath.map(elevatorMotor.getSensorPosition(), ElevatorPosition.BOTTOM.value, ElevatorPosition.TOP.value, 0, Constants.ELEVATOR_RANGE_INCHES);
	}

	public boolean isMoving(int deadzone) {
		return elevatorMotor.getClosedLoopError() > deadzone;
	}

	@Override
	public String getDashboardTabName () {
		return m_name;
	}

	@Override
	public void updateDashboardData () {
		dashboard_RawPos.setDouble(elevatorMotor.getSensorPosition());
	}

	public void setIdleMode(NeutralMode idleMode) {
		elevatorMotor.setNeutralMode(idleMode);
	}

	public static enum ElevatorPosition{
		BOTTOM(70),//30
		TOP(470),//430
		MIDDLE(OscarMath.mid(BOTTOM.value, TOP.value)),
		STARTING_CONFIG(TOP.value),
		INTAKEHATCH(BOTTOM.value),
		INTAKECARGO(BOTTOM.value),
		CARGOSHIP_HATCH(TOP.value),
		CARGOSHIP_CARGO(BOTTOM.value),
		ROCKETHATCH_LOW(TOP.value),
		ROCKETHATCH_MID(BOTTOM.value),
		ROCKETHATCH_HIGH(TOP.value),
		ROCKETCARGO_LOW(BOTTOM.value),
		ROCKETCARGO_MIDDLE(BOTTOM.value),
		ROCKETCARGO_HIGH(TOP.value);

		public final int value;

		ElevatorPosition(int value){
			this.value = value;
		}
	}

	public double PotToInches(double value) { return OscarMath.map(value, ElevatorPosition.BOTTOM.value, ElevatorPosition.TOP.value, 0, 30);}
	public double InchesToPot(double value) { return OscarMath.map(value, 0, 30, ElevatorPosition.BOTTOM.value, ElevatorPosition.TOP.value);}
}
