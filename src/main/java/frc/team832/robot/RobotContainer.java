package frc.team832.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.frc2.buttons.JoystickButton;
import edu.wpi.first.wpilibj.frc2.command.CommandScheduler;
import frc.team832.robot.commands.*;
import frc.team832.robot.subsystems.Drivetrain;
import frc.team832.robot.subsystems.Intake;

public class RobotContainer {

    public static final XboxController drivePad = new XboxController(0);
//    public static final JoystickButton aButton = new JoystickButton(drivePad, 1);
//    public static final JoystickButton bButton = new JoystickButton(drivePad, 2);
//    public static final JoystickButton yButton = new JoystickButton(drivePad, 4);
//    public static final JoystickButton xButton = new JoystickButton(drivePad, 3);

    public static final StratComInterface stratComInterface = new StratComInterface(1);
    public static final JoystickButton arcadeBlackLeft = new JoystickButton(stratComInterface, 12);
    public static final JoystickButton arcadeWhiteLeft = new JoystickButton(stratComInterface, 14);

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

        // Commands
//        aButton.whenHeld(new CargoDown());
//        yButton.whenHeld(new CargoUp());
//        bButton.whenHeld(new HatchIn());
//        xButton.whenHeld(new HatchOut());
        arcadeBlackLeft.whenHeld(new CargoUp());
        arcadeWhiteLeft.whenHeld(new CargoDown());





        return successful;
    }
}