package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Mechanisms.OscarLinearMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarSimpleMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPositionList;
import frc.team832.GrouchLib.Motors.IOscarSimpleMotor;
import frc.team832.GrouchLib.Motors.IOscarSmartMotor;
import frc.team832.robot.OI;

public class JackStands extends Subsystem {

    public OscarLinearMechanism _frontStand, _backStand;
    public OscarSimpleMechanism _drive;

    public static double frontTargetPosition, backTargetPosition;

    public JackStands(OscarLinearMechanism frontStand, OscarLinearMechanism backStand, OscarSimpleMechanism drive){
        _frontStand = frontStand;
        _backStand = backStand;
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

    public void setFrontPosition(String index) {
        _frontStand.setPosition(index);
    }

    public void setBackPosition(String index) {
        _backStand.setPosition(index);
    }

    public void resetEncoders(){
        _backStand.resetSensor();
        _frontStand.resetSensor();
    }

    public void stop(){
        _backStand.stop();
        _frontStand.stop();
    }

    @Override
    public void periodic() {
    }

    public void teleopControl(){
        if(OI.driverPad.getYButton()){
            setFrontPosition("TEST1");
            setBackPosition("TEST1");
        }else if(OI.driverPad.getAButton()){
            setFrontPosition("TEST2");
            setBackPosition("TEST2");
        }

        if(OI.driverPad.getPOV() == 0){
            _drive.set(.5);
        }else{
            _drive.set(0.0);
        }
    }

    public void pushData() {
        SmartDashboard.putNumber("Front Jack Stand", getFrontCurrentPosition());
        SmartDashboard.putNumber("Back Jack Stand", getBackCurrentPosition());
    }

    public boolean getAtTarget(){
        return _backStand.getAtTarget() && _frontStand.getAtTarget();
    }

    @Override
    protected void initDefaultCommand() {}

    public static class Constants {
        public static final int ENC_MIN_VAL = -78000;
        public static final int ENC_MAX_VAL = -500;
        public static final int ENC_RANGE = ENC_MAX_VAL  - ENC_MIN_VAL;
        public static final double MAX_INCHES = 29;
        public static final double ENC_TO_INCHES = MAX_INCHES/(double)ENC_RANGE;
        public static final double INCHES_TO_ENC = 1.0 / ENC_TO_INCHES;

        private static OscarMechanismPosition[] _positions = new OscarMechanismPosition[]{
                new OscarMechanismPosition("TEST1", -500),
                new OscarMechanismPosition("TEST2", -78000),
                new OscarMechanismPosition("Bottom", 0.0 * INCHES_TO_ENC),
                new OscarMechanismPosition("Middle", 10 * INCHES_TO_ENC),
                new OscarMechanismPosition("Top", MAX_INCHES * INCHES_TO_ENC),
        };

        public static final OscarMechanismPositionList Positions = new OscarMechanismPositionList(_positions);
    }
}
