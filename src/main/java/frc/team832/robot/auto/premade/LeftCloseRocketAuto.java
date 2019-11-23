package frc.team832.robot.auto.premade;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team832.robot.Paths;
import frc.team832.robot.commands.AutoMoveSuperStructure;
import frc.team832.robot.commands.automaticDriving.DriveOnPath;
import frc.team832.robot.subsystems.*;

import static frc.team832.robot.subsystems.SuperStructure.SuperStructurePosition.*;

public class LeftCloseRocketAuto extends SequentialCommandGroup {

    public LeftCloseRocketAuto(Drivetrain drivetrain, Intake intake, Elevator elevator, Fourbar fourbar, SuperStructure superStructure) {
        addRequirements(drivetrain, intake, elevator, fourbar, superStructure);
        addCommands(
                new AutoMoveSuperStructure(ROCKETHATCH_LOW, superStructure, fourbar, elevator),
                new DriveOnPath(Paths.LEFT_HAB_LEFT_ROCKET_CLOSE_PATH, drivetrain)
        );
    }
}
