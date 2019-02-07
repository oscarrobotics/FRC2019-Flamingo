package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.OscarComplexMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismComplexPosition;
import frc.team832.robot.Commands.IntakePanelFloor;
import frc.team832.robot.Robot;

public class TheBigOne extends Subsystem {

	private ComplexLift _complexLift;
	private SnowBlower _snowBlower;

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

	@Override
	public void periodic() {
		// action non-cancellation
		if (toRunAction != Action.IDLE && runningAction == Action.IDLE) {
			runningAction = toRunAction;
			toRunAction = Action.IDLE;
		}

		switch(runningAction) {
			case INTAKE_HP_HATCH:
				_snowBlower.setHatchHolderOpen(!_snowBlower.getHatchCoverStatus());
				if(_snowBlower.getHatchCoverStatus())
					setIdle();
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

				// get desired position
				OscarMechanismComplexPosition cargoPosition = ComplexLift.Constants.Positions.getByIndex(
						toRunAction == Action.INTAKE_HP_CARGO ? "" : "");

				// set ComplexLift position
				Robot.complexLift.setPosition(cargoPosition);

				switch (_snowBlower.getCargoPosition()) {
					case UNKNOWN:
						// do nothing
						break;
					case BOTTOM:
						_snowBlower.intakeSet(0.5);
						break;
					case BOTTOM_CENTERED:
						_snowBlower.intakeSet(0.5);
						break;
					case MIDDLE:
						_snowBlower.intakeSet(0.0);
						break;
					case TOP:
						_snowBlower.intakeSet(-0.5);
						break;
				}
				break;
			case CARGO_SHIP_HATCH:
				OscarMechanismComplexPosition hatchPosition = ComplexLift.Constants.Positions.getByIndex("");
				Robot.complexLift.setPosition(hatchPosition);
				if(Robot.complexLift.getAtTarget())

				break;
			case CARGO_SHIP_CARGO:

				break;
			case ROCKET_HATCH_LOW:
			case ROCKET_HATCH_MID:
			case ROCKET_HATCH_HIGH:



				break;
			case ROCKET_CARGO_LOW:
			case ROCKET_CARGO_MID:
			case ROCKET_CARGO_HIGH:


				break;
			case IDLE:
				// fancy LEDs?
				break;
		}
	}

	@Override
	protected void initDefaultCommand() {}
}
