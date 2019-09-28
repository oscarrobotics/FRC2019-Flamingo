package frc.team832.robot.commands;
 
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.subsystems.Drivetrain;
 
public class MainDrive extends CommandBase {
    private final Drivetrain subsystem;
    public MainDrive(Drivetrain subsystem) {
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void execute() {
        subsystem.drive();
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stop();
    }

}