package frc.team832.robot.commands;
 
import edu.wpi.first.wpilibj.frc2.command.SendableCommandBase;

import frc.team832.robot.subsystems.Drivetrain;
 
public class MainDrive extends SendableCommandBase {
    public MainDrive() {
        addRequirements(Drivetrain.getInstance());
    }

    @Override
    public void execute() {
        Drivetrain.drive();
    }

    @Override
    public void end(boolean interrupted) {
        Drivetrain.stop();
    }
}