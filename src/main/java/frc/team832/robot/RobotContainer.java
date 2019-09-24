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
        stratComInterface.getArcadeBlackRight().whenHeld(new CargoUp());
        stratComInterface.getArcadeBlackRight().whenHeld(new HatchIn());
        stratComInterface.getArcadeWhiteLeft().whenHeld(new CargoDown());
        stratComInterface.getArcadeWhiteRight().whenHeld(new HatchOut());

//        arcadeWhiteLeft.whenPressed(() -> Intake.cargoDown(.25));
        

        return successful;
    }
}