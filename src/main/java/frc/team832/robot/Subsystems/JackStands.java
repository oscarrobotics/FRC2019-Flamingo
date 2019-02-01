package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.OscarLinearMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarSimpleMechanism;

public class JackStands extends Subsystem {

    public OscarLinearMechanism _frontStand, _backStand;
    OscarSimpleMechanism
    public static double frontTargetPosition, backTargetPosition;

    public JackStands(OscarLinearMechanism frontStand, OscarLinearMechanism backStand){
        _backStand = backStand;
        _frontStand = frontStand;
    }

    public double getFrontTargetPosition(){
        return frontTargetPosition;
    }

    public double getBackTargetPosition(){
        return backTargetPosition;
    }

    public double getFrontCurrentPosition(){
        return _frontStand.getPosition();
    }


    public double getBackCurrentPosition(){
        return _backStand.getPosition();
    }

    public void stop(){
        _backStand.stop();
        _frontStand.stop();
    }


    @Override
    protected void initDefaultCommand() {

    }
}
