package frc.team832.robot.Commands.AutoJackStand;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.OI;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.SnowBlower;

import java.awt.*;

public class MoveJackStands extends Command {

    private String _index;

    public MoveJackStands(String index){
        requires(Robot.jackStands);
        _index = index;
    }

    public void initialize(){
        Robot.snowBlower.setLEDs(SnowBlower.LEDMode.CUSTOM_BREATHE, Color.RED);

        Robot.jackStands.setPosition(_index);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
