package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismComplexPosition;
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
		IDLE
	}

	public void setAction(Action action) {
		toRunAction = action;
	}

	public void setIdle() {
		toRunAction = Action.IDLE;
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
				break;
			case INTAKE_FLOOR_HATCH:
//				switch (_snowBlower._currentHatchState){
//					case INITIAL:
//						_snowBlower._hatchGrabbor.setPosition("Initial");
//						_snow_hatchHoldor.setPosition("Closed");
//						if(Math.abs(_hatchGrabbor.getPresetPosition("").getTarget() - Constants.grabberPositions[0].getTarget())<= 20) {
//							_hatchGrabbor.setPosition("Floor");
//							newHatchState(HatchGrabFloorState.LOWER_ARMS);
//						}
//						break;
//					case LOWER_ARMS:
//						if(Math.abs(_hatchGrabbor.getPresetPosition("").getTarget() - Constants.grabberPositions[2].getTarget())<= 20) {
//							_hatchGrabbor.setPosition("Release");
//							newHatchState(HatchGrabFloorState.RAISE_ARMS);
//						}
//						break;
//					case RAISE_ARMS:
//						if(Math.abs(_hatchGrabbor.getPresetPosition("").getTarget() - Constants.grabberPositions[1].getTarget())<= 20) {
//							_hatchHoldor.setPosition("Open");
//							newHatchState(HatchGrabFloorState.OPEN_HOLDER);
//						}
//						break;
//					case OPEN_HOLDER:
//						if(Math.abs(_hatchHoldor.getPresetPosition("").getTarget() - Constants.holderPositions[1].getTarget())<= 20) {
//							_hatchGrabbor.setPosition("Initial");
//							newHatchState(HatchGrabFloorState.LOWER_ARMS);
//						}
//						break;
//					case RETRACT_ARMS:
//						getHatchFloor = false;
//						break;
//				}
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
			case IDLE:
				// fancy LEDs?
				break;
		}
	}

	@Override
	protected void initDefaultCommand() {}
}
