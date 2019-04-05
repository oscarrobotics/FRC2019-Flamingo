package frc.team832.robot.Commands.AutoJackStand;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.SnowBlower;

import java.awt.*;

public class MoveJackStands extends InstantCommand {

    private String _index;

    public MoveJackStands(String index){
        requires(Robot.jackStands);
        _index = index;
    }

    public void initialize(){
        double jackstandDiff = Math.abs(Robot.jackStands.getBackTargetPosition() - Robot.jackStands.getFrontCurrentPosition());
        if (jackstandDiff < 30000) {
            Robot.snowBlower.setLEDs(SnowBlower.LEDMode.CUSTOM_BREATHE, Color.GREEN);
            Robot.jackStands.setPosition(_index);
        }
    }
}