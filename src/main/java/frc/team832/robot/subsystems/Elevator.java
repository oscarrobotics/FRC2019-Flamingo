package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj.frc2.command.SendableSubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.motorcontrol.CANTalon;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;

public class Elevator extends SendableSubsystemBase {
	private static Elevator instance;
	private static CANTalon elevator;

	public static Elevator getInstance() {
		if (instance == null) {
			//instance = new Fourbar();
		}
		return instance;
	}

	private Elevator() {
		super();
		SmartDashboard.putData("Elevator Subsys", this);
	}

	@Override
	public void periodic() {}

	public boolean initialize() {
		boolean successful = true;
		elevator = new CANTalon(6);

		if (!(elevator.getInputVoltage() > 0)) successful = false;

		NeutralMode allIdleMode = NeutralMode.kBrake;
		elevator.setNeutralMode(allIdleMode);

		return successful;
	}

	public static void setPosition(double position) {
		//bottom 30
		//top 430
		if (!(position > 430 || position < 30)) {
			elevator.setPosition(position);
		}
	}
}
