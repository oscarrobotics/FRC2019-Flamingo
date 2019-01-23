package frc.team832.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

    public Joystick operatorControl;
    public Joystick driveController;


    public OI() {
        operatorControl = new Joystick(1);
        driveController = new Joystick(0);

    }
}
