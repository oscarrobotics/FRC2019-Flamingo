package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.robot.commands.AutoMoveSuperStructure;
import frc.team832.robot.subsystems.Elevator.ElevatorPosition;
import frc.team832.robot.subsystems.Fourbar.FourbarPosition;

import java.io.FileOutputStream;

public class SuperStructure extends SubsystemBase {
	
	private final Intake intake;
	private final Fourbar fourbar;
	private final Elevator elevator;

	public SuperStructure(Fourbar fourbar, Elevator elevator, Intake intake) {
		super();
		this.intake = intake;
		this.fourbar = fourbar;
		this.elevator = elevator;
		SmartDashboard.putData("SuperStructure Subsys", this);
	}

	@Override
	public void periodic() {}

	public boolean initialize() {
		return fourbar.initialize() && elevator.initialize() && intake.initialize();
	}

	public void setPosition(SuperStructurePosition position) {
		elevator.setPosition(position.elevatorPosition);
		fourbar.setPosition(position.fourbarPosition);
	}

	public enum SuperStructurePosition {
		INTAKEHATCH(ElevatorPosition.INTAKEHATCH, FourbarPosition.MIDDLE),
		INTAKECARGO(ElevatorPosition.BOTTOM, FourbarPosition.INTAKECARGO),
		CARGOSHIP_HATCH(ElevatorPosition.TOP, FourbarPosition.CARGOSHIP_HATCH),
		CARGOSHIP_CARGO(ElevatorPosition.BOTTOM, FourbarPosition.CARGOSHIP_CARGO),
		ROCKETHATCH_LOW(ElevatorPosition.TOP, FourbarPosition.ROCKETHATCH_LOW),
		ROCKETHATCH_MID(ElevatorPosition.BOTTOM, FourbarPosition.ROCKETHATCH_MID),
		ROCKETHATCH_HIGH(ElevatorPosition.TOP, FourbarPosition.ROCKETHATCH_HIGH),
		ROCKETCARGO_LOW(ElevatorPosition.BOTTOM, FourbarPosition.ROCKETCARGO_LOW),
		ROCKETCARGO_MIDDLE(ElevatorPosition.BOTTOM, FourbarPosition.ROCKETCARGO_MID),
		ROCKETCARGO_HIGH(ElevatorPosition.TOP, FourbarPosition.ROCKETCARGO_HIGH);

		public final ElevatorPosition elevatorPosition;
		public final FourbarPosition fourbarPosition;

		SuperStructurePosition(ElevatorPosition elevatorPosition, FourbarPosition fourbarPosition) {
			this.elevatorPosition = elevatorPosition;
			this.fourbarPosition = fourbarPosition;
		}
	}
}
