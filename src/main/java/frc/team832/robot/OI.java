package frc.team832.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.OperatorModeCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.robot.Commands.AutoJackStand.JackstandHoldPosition;
import frc.team832.robot.Commands.AutoJackStand.MoveJackStands;
import frc.team832.robot.Commands.AutoJackStand.MoveSingleJackStand;
import frc.team832.robot.Commands.DoNothing;
import frc.team832.robot.Commands.Elevator.ManualMoveElevator;
import frc.team832.robot.Commands.FourBar.ManualMoveFourbar;
import frc.team832.robot.Commands.HatchFunctions.GrabHatch;
import frc.team832.robot.Commands.HatchFunctions.ReleaseHatch;
import frc.team832.robot.Commands.ManualToggle;
import frc.team832.robot.Commands.MoveCargo;
import frc.team832.robot.Commands.MoveComplexLiftWithAdjust;
import frc.team832.robot.Commands.TheBigOne.DefaultPosition;
import frc.team832.robot.Subsystems.Elevator.Constants.ElevatorPosition;
import frc.team832.robot.Subsystems.Fourbar.Constants.FourbarPosition;
import frc.team832.robot.Subsystems.JackStands;
import frc.team832.robot.Subsystems.SnowBlower;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal", "unused"})
public class OI {

	// Driver
	public static XboxController driverPad;
	private static JoystickButton frontStandUp, backStandUp, standDown, standUp, frontStandDown, backStandDown;
	private static JoystickButton leftStickPress, rightStickPress;

	// Operator
	public static Joystick operatorBox;

	public static JoystickButton op1, op2, op3, op4, op5, op6;
	public static JoystickButton modeButton1, modeButton2, modeButton3;
	public static JoystickButton incr, decr;

	public static JoystickButton black1, black2, white1, white2;
	public static JoystickButton threeSwitchUp, threeSwitchDown, singleSwitch;
	public static JoystickButton hatchGrab;

	public static Trigger mToggle;
	public static OperatorMode operatorMode = OperatorMode.Intake;

	static void init () {
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

	static void setupCommands () {
		mToggle.whenActive(new ManualToggle(new ManualMoveFourbar(), new DoNothing()));
		mToggle.whenActive(new ManualToggle(new ManualMoveElevator(), new DoNothing()));

//		black2.whenPressed(new ManualToggle(new DoNothing(), new AcquireHatch()));
//		black2.whenReleased(new ManualToggle(new DoNothing(), new InterruptAcquire()));

		black1.whenPressed(new MoveCargo(-1.0));
		black1.whenReleased(new MoveCargo(0.0));

		white1.whenPressed(new MoveCargo(.75));
		white1.whenReleased(new MoveCargo(0.0));

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
		handleControls();
	}

	public static ThreeSwitchPos getThreeSwitch () {
		if (threeSwitchUp.get())
			return ThreeSwitchPos.UP;
		else if (threeSwitchDown.get())
			return ThreeSwitchPos.DOWN;
		else
			return ThreeSwitchPos.OFF;
	}

	private static OperatorMode getOperatorMode () {
		if (modeButton1.get()) return OperatorMode.CargoShip;
		else if (modeButton2.get()) return OperatorMode.Rocket_Hatch;
		else if (modeButton3.get()) return OperatorMode.Rocket_Cargo;
		else return OperatorMode.Intake;
	}

	private static void runLED(){
		if (modeButton1.get() || modeButton2.get() || modeButton3.get())
			Robot.snowBlower.setLEDs(SnowBlower.LEDMode.PREPARE_INTAKE);
		else if (op1.get() || op2.get() || op3.get() || incr.get())
			Robot.snowBlower.setLEDs(SnowBlower.LEDMode.HATCH_HOLD);
		else if (op4.get() || op5.get() || op6.get() || decr.get())
			Robot.snowBlower.setLEDs(SnowBlower.LEDMode.BALL_HOLD);

	}

	/**
	 * Manages which commands get run based on different modifier buttons.
	 */
	public static void handleControls () {
		/*
		op1.whenPressed(new ModeCommand(
				new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.IntakeCargo_Floor.getIndex(), ElevatorPosition.IntakeCargo_Floor.getIndex())),
				new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketCargo_Low.getIndex(), ElevatorPosition.RocketCargo_Low.getIndex())),
				new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketHatch_Low.getIndex(), ElevatorPosition.RocketHatch_Low.getIndex()))
		));

		op2.whenPressed(new ModeCommand(
				new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.IntakeHatch_HP.getIndex(), ElevatorPosition.IntakeHatch_HP.getIndex())),
				new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketCargo_Middle.getIndex(), ElevatorPosition.RocketCargo_Middle.getIndex())),
				new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketHatch_Middle.getIndex(), ElevatorPosition.RocketHatch_Middle.getIndex()))
		));

		op3.whenPressed(new ModeCommand(
				new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.Top.getIndex(), ElevatorPosition.Bottom.getIndex())),
				new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketCargo_High.getIndex(), ElevatorPosition.RocketCargo_High.getIndex())),
				new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketHatch_High.getIndex(), ElevatorPosition.RocketHatch_High.getIndex()))
		));

		op4.whenPressed(new ModeCommand(
				new DoNothing(),
				new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.CargoShip_Cargo.getIndex(), ElevatorPosition.CargoShip_Cargo.getIndex())),
				new DoNothing()
		));

		op5.whenPressed(new ModeCommand(
				new DoNothing(),
				new DoNothing(),
				new DoNothing()
		));

		op6.whenPressed(new ModeCommand(
				new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.Bottom.getIndex(), ElevatorPosition.Top.getIndex())),
				new DoNothing(),
				new DoNothing()
		));
		*/

		modeButton1.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.IntakeCargo_Floor.getIndex(), ElevatorPosition.IntakeCargo_Floor.getIndex())));
		modeButton2.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.IntakeHatch_HP.getIndex(), ElevatorPosition.IntakeHatch_HP.getIndex())));
		modeButton2.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.Top.getIndex(), ElevatorPosition.Bottom.getIndex())));

		op1.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketHatch_Low.getIndex(), ElevatorPosition.RocketHatch_Low.getIndex())));
		op2.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketHatch_Middle.getIndex(), ElevatorPosition.RocketHatch_Middle.getIndex())));
		op3.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketHatch_High.getIndex(), ElevatorPosition.RocketHatch_High.getIndex())));
		op4.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketCargo_Low.getIndex(), ElevatorPosition.RocketCargo_Low.getIndex())));
		op5.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketCargo_Middle.getIndex(), ElevatorPosition.RocketCargo_Middle.getIndex())));
		op6.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.RocketCargo_High.getIndex(), ElevatorPosition.RocketCargo_High.getIndex())));
		incr.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.CargoShip_Hatch.getIndex(), ElevatorPosition.CargoShip_Hatch.getIndex())));
		decr.whenPressed(new ManualToggle(new DoNothing(), new MoveComplexLiftWithAdjust(FourbarPosition.CargoShip_Cargo.getIndex(), ElevatorPosition.CargoShip_Cargo.getIndex())));

		op1.whenReleased(new DefaultPosition(true));
		op2.whenReleased(new DefaultPosition(false));
		op3.whenReleased(new DefaultPosition(false));
		op4.whenReleased(new DefaultPosition(false));
		op5.whenReleased(new DefaultPosition(false));
		op6.whenReleased(new DefaultPosition(false));
		incr.whenReleased(new DefaultPosition(true));
		decr.whenReleased(new DefaultPosition(false));

	}

	public enum OperatorMode {
		Intake,
		CargoShip,
		Rocket_Hatch,
		Rocket_Cargo
	}

	public enum ThreeSwitchPos {
		UP,
		DOWN,
		OFF,//dont use, only for commands
	}

	private static  class ModeCommand extends OperatorModeCommand {

		public ModeCommand(Command onDefault, Command onState1, Command onState2) {
			super(onDefault, onState1, onState2, new DoNothing());
		}

		public ModeCommand (Command onDefault, Command onState1, Command onState2, Command onState3) {
			super(onDefault, onState1, onState2, onState3);
		}

		@Override
		protected OperatorMode condition () {
			OperatorMode mode = getOperatorMode();
			SmartDashboard.putString("OperatorMode", mode.toString());
			return mode;
		}
	}
}
