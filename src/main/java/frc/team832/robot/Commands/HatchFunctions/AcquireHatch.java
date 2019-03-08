package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team832.robot.Commands.DriveForTime;
import frc.team832.robot.Robot;


public class AcquireHatch extends CommandGroup {

    public AcquireHatch() {
        addSequential(new AutoHatchGrab());
        addSequential(new AutoHatchElevator(Robot.elevator.getCurrentPosition() + 20));
        addSequential(new DriveForTime(500, 100));
    }
}
