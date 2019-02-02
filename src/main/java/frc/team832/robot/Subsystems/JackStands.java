package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.OscarLinearMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarSimpleMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;
import frc.team832.GrouchLib.Motors.IOscarSimpleMotor;
import frc.team832.GrouchLib.Motors.IOscarSmartMotor;

public class JackStands extends Subsystem {

    public OscarLinearMechanism _frontStand, _backStand;
    public IOscarSimpleMotor _drive;

    public static double frontTargetPosition, backTargetPosition;

    public JackStands(OscarLinearMechanism frontStand, OscarLinearMechanism backStand, IOscarSimpleMotor drive){
        _backStand = backStand;
        _frontStand = frontStand;
        _drive = drive;
    }

    public double getFrontTargetPosition(){
        return frontTargetPosition;
    }

    public double getBackTargetPosition(){
        return backTargetPosition;
    }

    public double getFrontCurrentPosition(){
        return _frontStand.getCurrentPosition();
    }

    public double getBackCurrentPosition(){
        return _backStand.getCurrentPosition();
    }

    public void setFrontPosition(OscarMechanismPosition position) {
        _frontStand.setPosition(position);
    }

    public void setBackPosition(OscarMechanismPosition position) {
        _backStand.setPosition(position);
    }

    public void stop(){
        _backStand.stop();
        _frontStand.stop();
    }


    @Override
    protected void initDefaultCommand() {

    }
}
