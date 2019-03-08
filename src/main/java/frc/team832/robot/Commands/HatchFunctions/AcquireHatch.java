package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.CommandGroup;

import frc.team832.robot.Robot;


public class AcquireHatch extends CommandGroup {

    public AcquireHatch() {
        addSequential(new AutoHatchGrab());
        addSequential(new AutoHatchElevator(Robot.elevator.getCurrentPosition() + 100));
        addSequential(new AutoHatchDrive(500, 100));
    }
}
