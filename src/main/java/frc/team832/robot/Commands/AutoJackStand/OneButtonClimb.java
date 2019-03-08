package frc.team832.robot.Commands.AutoJackStand;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team832.robot.Subsystems.JackStands;


public class OneButtonClimb extends CommandGroup {

    public OneButtonClimb() {
        addSequential(new MoveJackStands("Bottom"));
        addSequential(new DriveToPlatform());
        addSequential(new MoveSingleJackStand(JackStands.JackStand.FRONT, "Top"));
        addSequential(new DriveOnPlatform());
        addSequential(new MoveSingleJackStand(JackStands.JackStand.BACK, "Bottom"));
    }
}
