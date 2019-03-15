package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.CommandGroup;

import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Elevator;


public class AcquireHatch extends CommandGroup {

    public AcquireHatch() {
        addSequential(new AutoHatchGrab());
        addSequential(new AutoHatchElevator(Elevator.Constants.Positions.getByIndex("Bottom").getTarget() + 50));//TODO: make this a constant!!!
        addSequential(new AutoHatchDrive(-500, 300));
    }
}
