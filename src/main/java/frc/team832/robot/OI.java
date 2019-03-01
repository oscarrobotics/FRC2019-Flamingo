package frc.team832.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Commands.*;
import frc.team832.robot.Commands.HatchFunctions.GrabHatch;
import frc.team832.robot.Commands.HatchFunctions.MoveGrabbor;
import frc.team832.robot.Commands.HatchFunctions.ReleaseHatch;
import frc.team832.robot.Commands.TheBigOne.BigOneToStartConfig;
import frc.team832.robot.Commands.TheBigOne.InitializeBigOne;
import frc.team832.robot.Commands.TheBigOne.MoveTheBigOne;
import frc.team832.robot.Commands.TheBigOne.TeleopBigOneMotionProfiling;
import frc.team832.robot.Subsystems.Elevator;
import frc.team832.robot.Subsystems.Fourbar;
import frc.team832.robot.Subsystems.JackStands;
import frc.team832.robot.Subsystems.TheBigOne;

import static frc.team832.robot.Subsystems.Fourbar.Constants.*;
import static frc.team832.robot.Subsystems.Elevator.Constants.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	// Driver
	public static XboxController driverPad;
	public static JoystickButton leftStickPress, rightStickPress;

	// Operator
	public static Joystick operatorBox;
	private static JoystickButton op1, op2, op3, op4, op5, op6;
	private static JoystickButton incr, decr;
	private static JoystickButton modeButton1, modeButton2, modeButton3, frontStandUp, backStandUp, standDown;

	public static OperatorMode operatorMode;

	public OI(){
    	driverPad = new XboxController(0);
		leftStickPress = new JoystickButton(driverPad, 9);
		rightStickPress = new JoystickButton(driverPad, 10);

		operatorBox = new Joystick(1);

		frontStandUp = new JoystickButton(driverPad, 6);
		backStandUp = new JoystickButton(driverPad, 5);
		standDown = new JoystickButton(driverPad, 3);

		op1 = new JoystickButton(operatorBox, 1);
		op2 = new JoystickButton(operatorBox, 2);
		op3 = new JoystickButton(operatorBox, 3);
		op4 = new JoystickButton(operatorBox, 4);
		op5 = new JoystickButton(operatorBox, 5);
		op6 = new JoystickButton(operatorBox, 6);
		incr = new JoystickButton(operatorBox, 7);
		decr = new JoystickButton(operatorBox, 8);
		modeButton1 = new JoystickButton(operatorBox, 9);
		modeButton2 = new JoystickButton(operatorBox, 10);
		modeButton3 = new JoystickButton(operatorBox, 11);

		System.out.println("Buttons initialized");


//		standDown.whenPressed(new MoveComplexLift(FourbarPosition.Middle.getIndex(), ElevatorPosition.Middle.getIndex()));
//		backStandUp.whenPressed(new MoveComplexLift(FourbarPosition.Top.getIndex(), ElevatorPosition.Top.getIndex()));
//		frontStandUp.whenPressed(new MoveComplexLift(FourbarPosition.Top.getIndex(), ElevatorPosition.Bottom.getIndex()));

//		modeButton1.whenPressed(new MoveComplexLift(FourbarPosition.RocketCargo_High.getIndex(), ElevatorPosition.RocketCargo_High.getIndex()));
//		modeButton2.whenPressed(new MoveComplexLift(FourbarPosition.RocketCargo_Middle.getIndex(), ElevatorPosition.RocketCargo_Middle.getIndex()));
//		modeButton2.whenPressed(new MoveComplexLift(FourbarPosition.RocketCargo_Low.getIndex(), ElevatorPosition.RocketCargo_Low.getIndex()));
//		op3.whenPressed(new MoveComplexLift(FourbarPosition.StorageConfig.getIndex(), ElevatorPosition.StorageConfig.getIndex()));
//		op6.whenPressed(new MoveComplexLift(FourbarPosition.IntakeCargo_Floor.getIndex(), ElevatorPosition.IntakeCargo_Floor.getIndex()));
//
//		op1.whenPressed(new MoveCargo(.75));
//		op1.whenReleased(new MoveCargo(0.0));
//
//		op4.whenPressed(new MoveCargo(-.75));
//		op4.whenReleased(new MoveCargo(0.0));
//
//		op2.whenPressed(new GrabHatch());
//		op5.whenPressed(new ReleaseHatch());

//		standDown.whenPressed(new MoveJackStands("Bottom"));
//		backStandUp.whenPressed(new MoveSingleJackStand(JackStands.JackStand.BACK, "Top"));
//		frontStandUp.whenPressed(new MoveSingleJackStand(JackStands.JackStand.FRONT, "Top"));
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
				opCommands[0] = new MoveTheBigOne(TheBigOne.Action.INTAKE_FLOOR_CARGO); // button 1 command
			    opCommands[1] = new MoveTheBigOne(TheBigOne.Action.INTAKE_HP_HATCH); // button 2 command
			    opCommands[2] = new DoNothing(); // button 3 command
			    opCommands[3] = new MoveTheBigOne(TheBigOne.Action.INTAKE_HP_CARGO); // button 4 command
			    opCommands[4] = new DoNothing(); // button 5 command
			    opCommands[5] = new DoNothing(); // button 6 command
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

	    // modifier on OP1
	    if (op1.get()) {
	    	if(op4.get()) {

		    } else {

		    }
	    }
	    // modifier on OP2
	    if (op2.get() && op5.get()) {

	    }
	    // modifier on OP3
	    if (op3.get() && op6.get()) {

	    }

	    op1.whenPressed(opCommands[0]);
		op2.whenPressed(opCommands[1]);
		op3.whenPressed(opCommands[2]);
		op4.whenPressed(opCommands[3]);
		op5.whenPressed(opCommands[4]);
		op6.whenPressed(opCommands[5]);
    }
}
