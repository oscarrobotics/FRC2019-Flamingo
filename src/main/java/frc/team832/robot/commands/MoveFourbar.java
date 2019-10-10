package frc.team832.robot.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team832.robot.RobotContainer;
import frc.team832.robot.StratComInterface;
import frc.team832.robot.subsystems.Fourbar;

public class MoveFourbar extends CommandBase {
    private final Fourbar fourbar;

    public MoveFourbar(Fourbar subsystem) {
        this.fourbar = subsystem;
        addRequirements(fourbar);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {

    }
}
