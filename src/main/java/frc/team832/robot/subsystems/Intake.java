package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.frc2.command.SendableSubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.motorcontrol.CANVictor;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;

public class Intake extends SendableSubsystemBase {

	private static Intake instance;
	private static CANVictor hatchIntake, cargoIntake;

	public static Intake getInstance() {
		if (instance == null) {
			instance = new Intake();
		}
		return instance;
	}

	private Intake() {
		super();
		SmartDashboard.putData("Intake Subsys", this);
	}

	@Override
	public void periodic() {}

	public boolean initialize() {
		boolean successful = true;
		hatchIntake = new CANVictor(13);
		cargoIntake = new CANVictor(12);
		if (!(hatchIntake.getInputVoltage() > 0)) successful = false;
		if (!(cargoIntake.getInputVoltage() > 0)) successful = false;

		NeutralMode allIdleMode = NeutralMode.kCoast;
		hatchIntake.setNeutralMode(allIdleMode);
		cargoIntake.setNeutralMode(allIdleMode);

		return successful;
	}

	public static void cargoUp(double power) {
		cargoIntake.set(power);
	}

	public static void cargoDown(double power) {
		cargoIntake.set(power);
	}

	public static void stopCargo() {
		cargoIntake.set(0);
	}

	public static void hatchIn(double power) {
		hatchIntake.set(power);
	}

	public static void hatchOut(double power) {
		hatchIntake.set(power);
	}

	public static void stopHatch(double power) {
		hatchIntake.set(power);
	}
}