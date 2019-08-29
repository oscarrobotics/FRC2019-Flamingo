package frc.team832.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.frc2.buttons.JoystickButton;
import edu.wpi.first.wpilibj.frc2.command.CommandScheduler;
import frc.team832.robot.commands.CargoUp;
import frc.team832.robot.commands.MainDrive;
import frc.team832.robot.subsystems.Drivetrain;
import frc.team832.robot.subsystems.Intake;

public class RobotContainer {

    public static final XboxController drivePad = new XboxController(0);
    public static final JoystickButton aButton = new JoystickButton(drivePad, 1);

    public static boolean init() {
        boolean successful = true;

        if (!Drivetrain.getInstance().initialize()) {
            successful = false;
        } else {
            CommandScheduler.getInstance().setDefaultCommand(Drivetrain.getInstance(), new MainDrive());
        }

        if (!Intake.getInstance().initialize()) {
            successful = false;
        }

        // Commands
        aButton.whenHeld(new CargoUp());

        return successful;
    }
}