package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.team832.robot.subsystems.Drivetrain;

public class ChangeTurnSensitivity extends InstantCommand {
    Drivetrain drivetrain;

    public ChangeTurnSensitivity(Drivetrain subsystem) {
        this.drivetrain = subsystem;
    }

    @Override
    public void initialize() {
        if (!Drivetrain.sensitivityToggle) {
            drivetrain.setPreciseTurning(Drivetrain.PreciseRotMultiplier);
            Drivetrain.sensitivityToggle = true;
        }
    }
}
