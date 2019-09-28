package frc.team832.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.*;
import frc.team832.robot.commands.*;
import frc.team832.robot.subsystems.Drivetrain;
import frc.team832.robot.subsystems.Intake;
import frc.team832.robot.subsystems.Jackstand;
import frc.team832.robot.subsystems.Jackstand.JackstandPosition;

public class RobotContainer {

    //Creates the drivePad object of XboxController class
    public static final XboxController drivePad = new XboxController(0);
    //Creates the JoystickButton object for drivPad w/ name aButton, etc...
    public static final JoystickButton aButton = new JoystickButton(drivePad, 1);
    public static final JoystickButton bButton = new JoystickButton(drivePad, 2);
    public static final JoystickButton yButton = new JoystickButton(drivePad, 4);
    public static final JoystickButton xButton = new JoystickButton(drivePad, 3);
    public static final JoystickButton startButton = new JoystickButton(drivePad, 8);

    //Creates the stratComInterface of the StratComInterface Class
    public static final StratComInterface stratComInterface = new StratComInterface(1);
    //Creates the JoystickButton object

    public static final Drivetrain drivetrain = new Drivetrain();
    public static final Intake intake = new Intake();
    public static final Jackstand jackstand = new Jackstand();

    public static boolean init() {
        boolean successful = true;

        if (!drivetrain.initialize()) {
            successful = false;
            System.out.println("Drivetrain INIT - FAIL");
        } else {
            System.out.println("Drivetrain INIT - OK");
        }

        if (!intake.initialize()) {
            successful = false;
            System.out.println("Intake INIT - FAIL");
        }

        if (!jackstand.initialize()) {
            successful = false;
            System.out.println("Jackstand INIT - FAIL");
        }

        //Commands: drivePad
        yButton.whenPressed(new MoveJackstands(jackstand, JackstandPosition.RETRACTED));
        bButton.whenPressed(new MoveJackstands(jackstand, JackstandPosition.LVL3_UP));
        startButton.whenPressed(new MoveJackstands(jackstand, JackstandPosition.LVL2_UP));
        //Commands: stratComInterface
        stratComInterface.getArcadeBlackRight().whenHeld(new CargoUp(intake));
        stratComInterface.getArcadeBlackRight().whenHeld(new HatchIn(intake));
        stratComInterface.getArcadeWhiteLeft().whenHeld(new CargoDown(intake));
        stratComInterface.getArcadeWhiteRight().whenHeld(new HatchOut(intake));

//        arcadeWhiteLeft.whenPressed(() -> Intake.cargoDown(.25));


        return successful;
    }
}