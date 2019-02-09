package frc.team832.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Commands.DoNothing;
import frc.team832.robot.Commands.MoveTheBigOne;
import frc.team832.robot.Subsystems.SnowBlower;
import frc.team832.robot.Subsystems.TheBigOne;

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
	private static JoystickButton modeButton1, modeButton2, modeButton3, standUp, standDown;

	public static OperatorMode operatorMode;

    public static void init() {
	    driverPad = new XboxController(0);

	    leftStickPress = new JoystickButton(driverPad, 9);
	    rightStickPress = new JoystickButton(driverPad, 10);

	    operatorBox = new Joystick(1);

	    standUp = new JoystickButton(driverPad, 3);
	    standDown = new JoystickButton(driverPad, 0);

	    if (operatorBox.getButtonCount() > 11) {
		    op1 = new JoystickButton(operatorBox, 0);
		    op2 = new JoystickButton(operatorBox, 1);
		    op3 = new JoystickButton(operatorBox, 2);
		    op4 = new JoystickButton(operatorBox, 3);
		    op5 = new JoystickButton(operatorBox, 4);
		    op6 = new JoystickButton(operatorBox, 5);
		    incr = new JoystickButton(operatorBox, 6);
		    decr = new JoystickButton(operatorBox, 7);
		    modeButton1 = new JoystickButton(operatorBox, 8);
		    modeButton2 = new JoystickButton(operatorBox, 9);
		    modeButton3 = new JoystickButton(operatorBox, 10);
	    }
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
			    opCommands[1] = new MoveTheBigOne(TheBigOne.Action.INTAKE_HP_CARGO); // button 2 command
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
