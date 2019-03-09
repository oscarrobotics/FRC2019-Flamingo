package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ButtonTest extends InstantCommand {
    @Override
    protected void initialize() {
        System.out.println("button pressed");
    }
}
