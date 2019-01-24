package frc.team832.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Commands.DoNothing;

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
	public static JoystickButton op1, op2, op3, op4, op5, op6;
	public static JoystickButton incr, decr;
	public static JoystickButton modeButton1, modeButton2, modeButton3;

    public OI() {
	    driverPad = new XboxController(0);

	    leftStickPress = new JoystickButton(driverPad, 9);
	    rightStickPress = new JoystickButton(driverPad, 10);

	    operatorBox = new Joystick(1);

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

    private OperatorMode getOperatorMode() {
    	if (modeButton1.get()) return OperatorMode.CargoShip;
    	else if (modeButton2.get()) return OperatorMode.Rocket_Hatch;
    	else if (modeButton3.get()) return OperatorMode.Rocket_Cargo;
    	else return OperatorMode.Intake;
    }

	/**
	 * Manages which commands get run based on different modifier buttons.
	 */
	public void handleControls() {
    	Command[] opCommands = new Command[6];

    	switch (getOperatorMode()) {
		    case Intake:
				opCommands[0] = new DoNothing(); // button 1 command
			    opCommands[1] = new DoNothing(); // button 2 command
			    opCommands[2] = new DoNothing(); // button 3 command
			    opCommands[3] = new DoNothing(); // button 4 command
			    opCommands[4] = new DoNothing(); // button 5 command
			    opCommands[5] = new DoNothing(); // button 6 command
			    break;
		    case CargoShip:
			    opCommands[0] = new DoNothing(); // button 1 command
			    opCommands[1] = new DoNothing(); // button 2 command
			    opCommands[2] = new DoNothing(); // button 3 command
			    opCommands[3] = new DoNothing(); // button 4 command
			    opCommands[4] = new DoNothing(); // button 5 command
			    opCommands[5] = new DoNothing(); // button 6 command
			    break;
		    case Rocket_Hatch:
			    opCommands[0] = new DoNothing(); // button 1 command
			    opCommands[1] = new DoNothing(); // button 2 command
			    opCommands[2] = new DoNothing(); // button 3 command
			    opCommands[3] = new DoNothing(); // button 4 command
			    opCommands[4] = new DoNothing(); // button 5 command
			    opCommands[5] = new DoNothing(); // button 6 command
			    break;
		    case Rocket_Cargo:
			    opCommands[0] = new DoNothing(); // button 1 command
			    opCommands[1] = new DoNothing(); // button 2 command
			    opCommands[2] = new DoNothing(); // button 3 command
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
    }
}
