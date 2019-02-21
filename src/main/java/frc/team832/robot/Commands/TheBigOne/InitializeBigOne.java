package frc.team832.robot.Commands.TheBigOne;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team832.robot.Subsystems.TheBigOne;


public class InitializeBigOne extends CommandGroup {

    public InitializeBigOne() {
        addSequential(new BigOneToStartConfig());
//        addSequential(new TeleopBigOneMotionProfiling(TheBigOne.Constants.MotionProfilePosition.ROCKET_HATCH_HIGH));
//        System.out.println("init big one");
//        addSequential(new BigOneToStartConfig());
//        System.out.println("second init big one");
    }
}
