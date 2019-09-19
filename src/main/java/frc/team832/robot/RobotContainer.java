package frc.team832.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.frc2.buttons.JoystickButton;
import edu.wpi.first.wpilibj.frc2.command.CommandScheduler;
import frc.team832.robot.commands.*;
import frc.team832.robot.subsystems.Drivetrain;
import frc.team832.robot.subsystems.Intake;

public class RobotContainer {

    //Creates the drivePad object of XboxController class
    public static final XboxController drivePad = new XboxController(0);
    //Creates the JoystickButton object w/ name aButton, etc...
    public static final JoystickButton aButton = new JoystickButton(drivePad, 1);
    public static final JoystickButton bButton = new JoystickButton(drivePad, 2);
    public static final JoystickButton yButton = new JoystickButton(drivePad, 4);
    public static final JoystickButton xButton = new JoystickButton(drivePad, 3);
    public static final JoystickButton startButton = new JoystickButton(drivePad, 8);

    //Creates the stratComInterface of the StratComInterface Class
    public static final StratComInterface stratComInterface = new StratComInterface(1);
    //Creates the JoystickButton object
    public static final JoystickButton SC1 = new JoystickButton(stratComInterface, 1);
    public static final JoystickButton SC2 = new JoystickButton(stratComInterface, 2);
    public static final JoystickButton SC3 = new JoystickButton(stratComInterface, 3);
    public static final JoystickButton SC4 = new JoystickButton(stratComInterface, 4);
    public static final JoystickButton SC5 = new JoystickButton(stratComInterface, 5);
    public static final JoystickButton SC6 = new JoystickButton(stratComInterface, 6);
    public static final JoystickButton SCPlus = new JoystickButton(stratComInterface, 7);
    public static final JoystickButton SCMinus = new JoystickButton(stratComInterface, 8);
    public static final JoystickButton SCSideTop = new JoystickButton(stratComInterface, 9);
    public static final JoystickButton SCSideMid = new JoystickButton(stratComInterface, 10);
    public static final JoystickButton SCSideBot = new JoystickButton(stratComInterface, 11);
    public static final JoystickButton arcadeBlackLeft = new JoystickButton(stratComInterface, 12);
    public static final JoystickButton arcadeBlackRight = new JoystickButton(stratComInterface, 13);
    public static final JoystickButton arcadeWhiteLeft = new JoystickButton(stratComInterface, 14);
    public static final JoystickButton arcadeWhiteRight = new JoystickButton(stratComInterface, 15);
    public static final JoystickButton singleToggle = new JoystickButton(stratComInterface, 16);
    public static final JoystickButton doubleToggleUp = new JoystickButton(stratComInterface, 17);
    public static final JoystickButton doubleToggleDown = new JoystickButton(stratComInterface, 18);
    public static final JoystickButton keySwitch = new JoystickButton(stratComInterface, 19);

    public static boolean init() {
        boolean successful = true;

        if (!Drivetrain.getInstance().initialize()) {
            successful = false;
            System.out.println("Drivetrain INIT - FAIL");
        } else {
            System.out.println("Drivetrain INIT - OK");
            CommandScheduler.getInstance().setDefaultCommand(Drivetrain.getInstance(), new MainDrive());
        }

        if (!Intake.getInstance().initialize()) {
            successful = false;
        }

        //Commands: drivePad
        yButton.whenPressed(new JackstandRetract());
        bButton.whenPressed(new JackstandExtendLvl3());
        startButton.whenPressed(new JackstandExtendLvl2());
        //Commands: stratComInterface
        arcadeBlackLeft.whenHeld(new CargoUp());
        arcadeBlackRight.whenHeld(new HatchIn());
        arcadeWhiteLeft.whenHeld(new CargoDown());
        arcadeWhiteRight.whenHeld(new HatchOut());

//         learning is occuring here
        SCPlus.whenHeld(new Test());



        return successful;
    }
}