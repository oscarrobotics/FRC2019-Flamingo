package frc.team832.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Commands.*;
import frc.team832.robot.Commands.AutoJackStand.*;
import frc.team832.robot.Commands.Drivetrain.TurnToHeading;
import frc.team832.robot.Commands.Elevator.ManualMoveElevator;
import frc.team832.robot.Commands.FourBar.ManualMoveFourbar;
import frc.team832.robot.Commands.HatchFunctions.*;
import frc.team832.robot.Commands.TheBigOne.MoveTheBigOne;
import frc.team832.robot.Subsystems.Elevator;
import frc.team832.robot.Subsystems.Elevator.Constants.ElevatorPosition;
import frc.team832.robot.Subsystems.Fourbar;
import frc.team832.robot.Subsystems.Fourbar.Constants.FourbarPosition;
import frc.team832.robot.Subsystems.JackStands;
import frc.team832.robot.Subsystems.TheBigOne;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal", "unused"})
public class OI {
	public static int angle = 0;

	// Driver
	public static XboxController driverPad;
	private static JoystickButton leftStickPress, rightStickPress;
	private static JoystickButton frontStandUp, backStandUp, standDown, standUp, frontStandDown, backStandDown;

	// Operator
	public static Joystick operatorBox;
	public static JoystickButton op1, op2, op3, op4, op5, op6, black1, black2, white1, white2, threeSwitchUp, threeSwitchDown, singleSwitch;
	public static JoystickButton incr, decr;
	public static JoystickButton modeButton1, modeButton2, modeButton3, hatchGrab;

	public static Trigger mToggle;

	public static OperatorMode operatorMode;

	static void init() {
		driverPad = new XboxController(0);
		leftStickPress = new JoystickButton(driverPad, 9);
		rightStickPress = new JoystickButton(driverPad, 10);
		frontStandUp = new JoystickButton(driverPad, 6);
		backStandUp = new JoystickButton(driverPad, 5);
		standDown = new JoystickButton(driverPad, 1);
		standUp = new JoystickButton(driverPad, 4);
		frontStandDown = new JoystickButton(driverPad, 3);
		backStandDown = new JoystickButton(driverPad, 2);

		operatorBox = new Joystick(1);


		op1 = new JoystickButton(operatorBox, 1);
		op2 = new JoystickButton(operatorBox, 2);
		op3 = new JoystickButton(operatorBox, 3);
		op4 = new JoystickButton(operatorBox, 4);
		op5 = new JoystickButton(operatorBox, 5);
		op6 = new JoystickButton(operatorBox, 6);
		mToggle = new JoystickButton(operatorBox, 19);
		incr = new JoystickButton(operatorBox, 7);
		decr = new JoystickButton(operatorBox, 8);
		modeButton1 = new JoystickButton(operatorBox, 9);
		modeButton2 = new JoystickButton(operatorBox, 10);
		modeButton3 = new JoystickButton(operatorBox, 11);

		black1 = new JoystickButton(operatorBox, 12);
		black2 = new JoystickButton(operatorBox, 13);
		white1 = new JoystickButton(operatorBox, 14);
		white2 = new JoystickButton(operatorBox, 15);
		singleSwitch = new JoystickButton(operatorBox, 16);
		threeSwitchUp = new JoystickButton(operatorBox, 18);
		threeSwitchDown = new JoystickButton(operatorBox, 17);

		System.out.println("Buttons initialized");
	}

	static void setupCommands() {
		mToggle.whenActive(new ManualToggle(new ManualMoveFourbar(), new DoNothing()));
		mToggle.whenActive(new ManualToggle(new ManualMoveElevator(), new DoNothing()));

//		black2.whenPressed(new ManualToggle(new DoNothing(), new AcquireHatch()));
//		black2.whenReleased(new ManualToggle(new DoNothing(), new InterruptAcquire()));

//		black2.toggleWhenPressed(new TurnToHeading(angle));

		black1.whenPressed(new MoveCargo(-1.0));
		black1.whenReleased(new MoveCargo(0.0));

		white1.whenPressed(new MoveCargo(.75));
		white1.whenReleased(new MoveCargo(0.0));

		op1.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.Middle.getIndex(), ElevatorPosition.Bottom.getIndex())));
		op2.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.Middle.getIndex(), ElevatorPosition.Middle.getIndex())));
		op3.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.Middle.getIndex(), ElevatorPosition.Top.getIndex())));
		op4.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.Top.getIndex(), ElevatorPosition.Middle.getIndex())));
		op5.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.Top.getIndex(), ElevatorPosition.Top.getIndex())));

		modeButton1.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketCargo_High.getIndex(), ElevatorPosition.RocketCargo_High.getIndex())));
		modeButton2.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketCargo_Middle.getIndex(), ElevatorPosition.RocketCargo_Middle.getIndex())));
		modeButton3.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketCargo_Low.getIndex(), ElevatorPosition.RocketCargo_Low.getIndex())));

		white2.whileHeld(new GrabHatch());
		black2.whileHeld(new ReleaseHatch());

		standDown.whenPressed(new MoveJackStands("Bottom"));
		standUp.whenPressed(new MoveJackStands("Top"));

		backStandUp.whenPressed(new MoveSingleJackStand(JackStands.JackStand.BACK, "Top"));
		backStandUp.whenReleased(new JackstandHoldPosition(JackStands.JackStand.BACK));

		frontStandUp.whenPressed(new MoveSingleJackStand(JackStands.JackStand.FRONT, "Top"));
		frontStandUp.whenReleased(new JackstandHoldPosition(JackStands.JackStand.FRONT));

		backStandDown.whenPressed(new MoveSingleJackStand(JackStands.JackStand.BACK, "Bottom"));
		backStandDown.whenReleased(new JackstandHoldPosition(JackStands.JackStand.BACK));

		frontStandDown.whenPressed(new MoveSingleJackStand(JackStands.JackStand.FRONT, "Bottom"));
		frontStandDown.whenReleased(new JackstandHoldPosition(JackStands.JackStand.FRONT));
	}

    public enum OperatorMode {
    	Intake,
	    CargoShip,
	    Rocket_Hatch,
	    Rocket_Cargo
    }

    private static OperatorMode getOperatorMode() {
    	if (modeButton1.get()) return OperatorMode.CargoShip;
    	else if (modeButton2.get()) return OperatorMode.Rocket_Hatch;
    	else if (modeButton3.get()) return OperatorMode.Rocket_Cargo;
    	else return OperatorMode.Intake;
    }

	/**
	 * Manages which commands get run based on different modifier buttons.
	 */
	public static void handleControls() {
    	Command[] opCommands = new Command[6];

    	switch (operatorMode = getOperatorMode()) {
		    case Intake:
				opCommands[0] = new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.Middle.getIndex(), ElevatorPosition.Bottom.getIndex()));
				opCommands[1] = new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.Middle.getIndex(), ElevatorPosition.Middle.getIndex()));
				opCommands[2] = new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.Middle.getIndex(), ElevatorPosition.Top.getIndex()));
				opCommands[3] = new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.Top.getIndex(), ElevatorPosition.Middle.getIndex()));
				opCommands[4] = new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.Top.getIndex(), ElevatorPosition.Top.getIndex()));

				/*
				opCommands[0] = new MoveTheBigOne(TheBigOne.Action.INTAKE_FLOOR_CARGO); // button 1 command
			    opCommands[1] = new MoveTheBigOne(TheBigOne.Action.INTAKE_HP_HATCH); // button 2 command
			    opCommands[2] = new DoNothing(); // button 3 command
			    opCommands[3] = new MoveTheBigOne(TheBigOne.Action.INTAKE_HP_CARGO); // button 4 command
			    opCommands[4] = new DoNothing(); // button 5 command
			    opCommands[5] = new DoNothing(); // button 6 command
			    */
			    break;
		    case CargoShip:
			    opCommands[0] = new MoveTheBigOne(TheBigOne.Action.CARGO_SHIP_CARGO); // button 1 command
			    opCommands[1] = new DoNothing(); // button 2 command
			    opCommands[2] = new MoveTheBigOne(TheBigOne.Action.CARGO_SHIP_HATCH); // button 3 command
			    opCommands[3] = new DoNothing(); // button 4 command
			    opCommands[4] = new DoNothing(); // button 5 command
			    opCommands[5] = new DoNothing(); // button 6 command
			    break;
		    case Rocket_Hatch:
			    opCommands[0] = new MoveTheBigOne(TheBigOne.Action.ROCKET_HATCH_HIGH); // button 1 command
			    opCommands[1] = new MoveTheBigOne(TheBigOne.Action.ROCKET_HATCH_MID); // button 2 command
			    opCommands[2] = new MoveTheBigOne(TheBigOne.Action.ROCKET_HATCH_LOW); // button 3 command
			    opCommands[3] = new DoNothing(); // button 4 command
			    opCommands[4] = new DoNothing(); // button 5 command
			    opCommands[5] = new DoNothing(); // button 6 command
			    break;
		    case Rocket_Cargo:
			    opCommands[0] = new MoveTheBigOne(TheBigOne.Action.ROCKET_CARGO_HIGH); // button 1 command
			    opCommands[1] = new MoveTheBigOne(TheBigOne.Action.ROCKET_CARGO_MID); // button 2 command
			    opCommands[2] = new MoveTheBigOne(TheBigOne.Action.ROCKET_CARGO_LOW); // button 3 command
			    opCommands[3] = new DoNothing(); // button 4 command
			    opCommands[4] = new DoNothing(); // button 5 command
			    opCommands[5] = new DoNothing(); // button 6 command
			    break;
	    }

	    op1.whenPressed(opCommands[0]);
		op2.whenPressed(opCommands[1]);
		op3.whenPressed(opCommands[2]);
		op4.whenPressed(opCommands[3]);
		op5.whenPressed(opCommands[4]);
		op6.whenPressed(opCommands[5]);
		incr.whenPressed(new DoNothing());
		decr.whenPressed(new DoNothing());
    }
}
