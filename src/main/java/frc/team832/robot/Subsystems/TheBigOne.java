package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismComplexPosition;
import frc.team832.robot.Commands.MotionProfiling.TeleopBigOneMotionProfiling;
import frc.team832.robot.Robot;

import static frc.team832.robot.Subsystems.TheBigOne.Constants.*;

public class TheBigOne extends Subsystem {

	private ComplexLift _complexLift;
	private SnowBlower _snowBlower;

	private MechanismComplexPosition targetPosition;

	public static MotionProfilePosition currentPos = MotionProfilePosition.START;

	private Action toRunAction, runningAction;

	public TheBigOne(ComplexLift complexLift, SnowBlower snowBlower) {
		_complexLift = complexLift;
		_snowBlower = snowBlower;
	}

	public enum Action {
		INTAKE_HP_HATCH,
		INTAKE_FLOOR_HATCH,
		INTAKE_FLOOR_CARGO,
		INTAKE_HP_CARGO,
		CARGO_SHIP_HATCH,
		CARGO_SHIP_CARGO,
		ROCKET_HATCH_LOW,
		ROCKET_HATCH_MID,
		ROCKET_HATCH_HIGH,
		ROCKET_CARGO_LOW,
		ROCKET_CARGO_MID,
		ROCKET_CARGO_HIGH,
		IDLE
	}

	public void setAction(Action action) {
		toRunAction = action;
	}

	public void setIdle() {
		runningAction = Action.IDLE;
	}

	private boolean no = true;

	@Override
	public void periodic() {
		if (!no) {
			// action non-cancellation
			if (toRunAction != Action.IDLE && runningAction == Action.IDLE) {
				runningAction = toRunAction;
				toRunAction = Action.IDLE;
			}

			switch (runningAction) {
				case INTAKE_HP_HATCH:
					new TeleopBigOneMotionProfiling(MotionProfilePosition.INTAKE_HP_HATCH).start();
					_snowBlower.setHatchHolderPosition(_snowBlower.getHatchCoverStatus() ? "Open" : "Closed");
					if (_snowBlower.getHatchCoverStatus()) {
						currentPos = MotionProfilePosition.INTAKE_HP_HATCH;
						setIdle();
					}
					break;
				case INTAKE_FLOOR_HATCH:
				/*CommandGroup toRun = new IntakePanelFloor();
				toRun.start();
				if(toRun.isCompleted()) {
					setIdle();
				}*/
					setIdle();
					break;
				case INTAKE_HP_CARGO:
				case INTAKE_FLOOR_CARGO:
					// we case both together, and check the action later, as the code is 99% the same, just a different position
					new TeleopBigOneMotionProfiling(toRunAction == Action.INTAKE_HP_CARGO ? MotionProfilePosition.INTAKE_HP_CARGO : MotionProfilePosition.INTAKE_FLOOR_CARGO).start();
					// get desired position
					targetPosition = ComplexLift.Constants.Positions.getByIndex(
							toRunAction == Action.INTAKE_HP_CARGO ? "IntakeCargo_HP" : "IntakeCargo_Floor");

					// set ComplexLift position
					//Robot.complexLift.setPosition(targetPosition);

					switch (_snowBlower.getCargoPosition()) {
						case UNKNOWN:
							// do nothing
							break;
						case BOTTOM:
							_snowBlower.intakeSet(0.5);
							break;
						case MIDDLE:
							currentPos = toRunAction == Action.INTAKE_HP_CARGO ? MotionProfilePosition.INTAKE_HP_CARGO : MotionProfilePosition.INTAKE_FLOOR_CARGO;
							_snowBlower.intakeSet(0.0);

							break;
						case TOP:
							_snowBlower.intakeSet(-0.5);
							break;
					}
					break;
				case CARGO_SHIP_HATCH:
					new TeleopBigOneMotionProfiling(MotionProfilePosition.CARGO_SHIP_HATCH).start();

					targetPosition = ComplexLift.Constants.Positions.getByIndex("");
					//Robot.complexLift.setPosition(targetPosition);
					if (Robot.complexLift.getAtTarget()) {
						_snowBlower.setHatchHolderPosition("Closed");
						currentPos = MotionProfilePosition.CARGO_SHIP_HATCH;
						setIdle();
					}
					break;
				case CARGO_SHIP_CARGO:
					new TeleopBigOneMotionProfiling(MotionProfilePosition.CARGO_SHIP_CARGO).start();

					targetPosition = ComplexLift.Constants.Positions.getByIndex("");
					//Robot.complexLift.setPosition(targetPosition);
					if (Robot.complexLift.getAtTarget()) {
						_snowBlower.intakeSet(.5);
						currentPos = MotionProfilePosition.CARGO_SHIP_CARGO;
						setIdle();
					}
					break;
				case ROCKET_HATCH_LOW:
				case ROCKET_HATCH_MID:
				case ROCKET_HATCH_HIGH:
					targetPosition = ComplexLift.Constants.Positions.getByIndex(
							toRunAction == Action.ROCKET_HATCH_HIGH ? "" : toRunAction == Action.ROCKET_HATCH_MID ? "" : "");

					new TeleopBigOneMotionProfiling(toRunAction == Action.ROCKET_HATCH_HIGH ? MotionProfilePosition.ROCKET_HATCH_HIGH :
							toRunAction == Action.ROCKET_HATCH_MID ? MotionProfilePosition.ROCKET_HATCH_MID : MotionProfilePosition.ROCKET_HATCH_LOW).start();

					//Robot.complexLift.setPosition(targetPosition);
					if (Robot.complexLift.getAtTarget()) {
						_snowBlower.setHatchHolderPosition("Closed");
						currentPos = toRunAction == Action.ROCKET_HATCH_HIGH ? MotionProfilePosition.ROCKET_HATCH_HIGH :
								toRunAction == Action.ROCKET_HATCH_MID ? MotionProfilePosition.ROCKET_HATCH_MID :
										MotionProfilePosition.ROCKET_HATCH_LOW;
						setIdle();
					}
					break;
				case ROCKET_CARGO_LOW:
				case ROCKET_CARGO_MID:
				case ROCKET_CARGO_HIGH:
					targetPosition = ComplexLift.Constants.Positions.getByIndex(
							toRunAction == Action.ROCKET_CARGO_HIGH ? "" : toRunAction == Action.ROCKET_CARGO_MID ? "" : "");

					new TeleopBigOneMotionProfiling(toRunAction == Action.ROCKET_CARGO_HIGH ? MotionProfilePosition.ROCKET_CARGO_HIGH :
							toRunAction == Action.ROCKET_CARGO_MID ? MotionProfilePosition.ROCKET_CARGO_MID : MotionProfilePosition.ROCKET_CARGO_LOW).start();

					//                Robot.complexLift.setPosition(targetPosition);
					if (Robot.complexLift.getAtTarget()) {
						_snowBlower.setHatchHolderPosition("Closed");
						currentPos = toRunAction == Action.ROCKET_CARGO_HIGH ? MotionProfilePosition.ROCKET_CARGO_HIGH :
								toRunAction == Action.ROCKET_CARGO_MID ? MotionProfilePosition.ROCKET_CARGO_MID :
										MotionProfilePosition.ROCKET_CARGO_LOW;
						setIdle();
					}
					break;
				case IDLE:
					// fancy LEDs?
					break;
			}
		}
	}

	@Override
	protected void initDefaultCommand() {}

	public static class Constants{
		public enum MotionProfilePosition {
			INTAKE_HP_HATCH("BottomHatch"),
			INTAKE_FLOOR_HATCH("IntakeFloorHatch"),
			INTAKE_FLOOR_CARGO("IntakeCargo"),
			INTAKE_HP_CARGO("HPCargo"),
			CARGO_SHIP_HATCH("BottomHatch"),
			CARGO_SHIP_CARGO("ShipCargo"),
			ROCKET_HATCH_LOW("BottomHatch"),
			ROCKET_HATCH_MID("MiddleHatch"),
			ROCKET_HATCH_HIGH("TopHatch"),
			ROCKET_CARGO_LOW("BottomCargo"),
			ROCKET_CARGO_MID("MiddleCargo"),
			ROCKET_CARGO_HIGH("TopCargo"),
			START("StartConfig");

			private String _value;

			public String index() {
				return _value;
			}

			MotionProfilePosition(String value) {
				_value = value;
			}
		}
	}
}
