package frc.team832.robot.Commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team832.robot.Commands.HatchFunctions.*;

public class IntakePanelFloor extends CommandGroup {

    public IntakePanelFloor(){
        addSequential(new ReleaseHatch());
        addSequential(new MoveGrabbor("Floor"));
        addSequential(new MoveGrabbor("Release"));
        addSequential(new GrabHatch());
        addSequential(new MoveGrabbor("Initial"));
    }
}
