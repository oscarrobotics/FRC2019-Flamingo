package frc.team832.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.frc2.command.CommandScheduler;
import frc.team832.robot.commands.MainDrive;
import frc.team832.robot.subsystems.Drivetrain;

public class RobotContainer {

    public static final XboxController drivePad = new XboxController(0);

    public static boolean init() {
        if (Drivetrain.getInstance().initialize()) {
            return false;
        }
        CommandScheduler.getInstance().setDefaultCommand(Drivetrain.getInstance(), new MainDrive());
        return true;
    }
}