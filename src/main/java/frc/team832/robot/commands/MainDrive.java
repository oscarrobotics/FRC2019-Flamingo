package frc.team832.robot.commands;
 
import edu.wpi.first.wpilibj.frc2.command.SendableCommandBase;

import frc.team832.robot.subsystems.Drivetrain;
 
public class MainDrive extends SendableCommandBase {
    public MainDrive() {
        addRequirements(Drivetrain.getInstance());
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        Drivetrain.drive();
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {}
        Drivetrain.stop();
    }

    @Override
    public boolean isFinished() {
        // This return value will specify whether the command has finished!  The default is "false," which will make the
        // command never end.
        return false;
    }
}