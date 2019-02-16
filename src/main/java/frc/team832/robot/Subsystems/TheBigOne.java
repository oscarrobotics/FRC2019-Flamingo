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

	private OscarMechanismComplexPosition targetPosition;

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
				targetPosition = ComplexLift.Constants.Positions.getByIndex(
						toRunAction == Action.INTAKE_HP_CARGO ? "IntakeCargo_HP" : "IntakeCargo_Floor");

				// set ComplexLift position
				Robot.complexLift.setPosition(targetPosition);

				switch (_snowBlower.getCargoPosition()) {
					case UNKNOWN:
						// do nothing
						break;
					case BOTTOM:
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
				targetPosition = ComplexLift.Constants.Positions.getByIndex("");
				Robot.complexLift.setPosition(targetPosition);
				if(Robot.complexLift.getAtTarget()){
				    _snowBlower.setHatchHolderOpen(false);
                }
				break;
			case CARGO_SHIP_CARGO:
			    targetPosition = ComplexLift.Constants.Positions.getByIndex("");
                Robot.complexLift.setPosition(targetPosition);
                if(Robot.complexLift.getAtTarget()){
                    _snowBlower.intakeSet(.5);
                }
				break;
			case ROCKET_HATCH_LOW:
			case ROCKET_HATCH_MID:
			case ROCKET_HATCH_HIGH:
                targetPosition = ComplexLift.Constants.Positions.getByIndex(
                        toRunAction == Action.ROCKET_HATCH_HIGH ? "": toRunAction == Action.ROCKET_HATCH_MID ? "" : "");
                Robot.complexLift.setPosition(targetPosition);
                if(Robot.complexLift.getAtTarget()){
                    _snowBlower.setHatchHolderOpen(false);
                }
				break;
			case ROCKET_CARGO_LOW:
			case ROCKET_CARGO_MID:
			case ROCKET_CARGO_HIGH:
                targetPosition = ComplexLift.Constants.Positions.getByIndex(
                        toRunAction == Action.ROCKET_CARGO_HIGH ? "": toRunAction == Action.ROCKET_CARGO_MID ? "" : "");
                Robot.complexLift.setPosition(targetPosition);
                if(Robot.complexLift.getAtTarget()){
                    _snowBlower.setHatchHolderOpen(false);
                }
				break;
			case IDLE:
				// fancy LEDs?
				break;
		}
	}

	@Override
	protected void initDefaultCommand() {}
}
