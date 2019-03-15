package frc.team832.robot.Commands.AutoJackStand;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.JackStands;

public class JackstandHoldPosition extends InstantCommand {
    private JackStands.JackStand _stand;

    public JackstandHoldPosition(JackStands.JackStand stand) {
        requires(Robot.jackStands);
        _stand = stand;
    }

    @Override
    protected void initialize() {
        switch ( _stand) {
            case FRONT:
                Robot.jackStands.setFrontPosition(Robot.jackStands.getFrontCurrentPosition());
                break;
            case BACK:
                Robot.jackStands.setBackPosition(Robot.jackStands.getBackCurrentPosition());
                break;
            case BOTH:
                Robot.jackStands.setFrontPosition(Robot.jackStands.getFrontCurrentPosition());
                Robot.jackStands.setBackPosition(Robot.jackStands.getBackCurrentPosition());
                break;
        }
    }
}
