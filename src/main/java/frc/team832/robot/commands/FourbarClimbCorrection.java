package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.subsystems.Fourbar;
import frc.team832.robot.subsystems.SuperStructure;

public class FourbarClimbCorrection extends CommandBase {
    Fourbar fourbar;
    SuperStructure superStructure;
    public FourbarClimbCorrection(SuperStructure superStructure, Fourbar subsystem) {
        this.fourbar = subsystem;
        this.superStructure = superStructure;

        addRequirements(fourbar);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        superStructure.handleFourbarClimbCorrection();
    }

    @Override
    public void end(boolean interrupted){

    }
}
