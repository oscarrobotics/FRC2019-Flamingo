package frc.team832.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.LEDs;
import frc.team832.robot.subsystems.Fourbar;
import frc.team832.robot.subsystems.Jackstand;
import frc.team832.robot.subsystems.SuperStructure;

public class FourbarClimbCorrection extends CommandBase {
    Fourbar fourbar;
    SuperStructure superStructure;
    Jackstand jackstand;

    public FourbarClimbCorrection(SuperStructure superStructure, Fourbar subsystem, Jackstand jackstand) {
        this.fourbar = subsystem;
        this.superStructure = superStructure;
        this.jackstand = jackstand;

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
        jackstand.atTarget();
    }
}
