package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Mechanisms.LinearMechanism;
import frc.team832.GrouchLib.Mechanisms.SimpleMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPositionList;
import frc.team832.robot.OI;

public class JackStands extends Subsystem {

    public LinearMechanism _frontStand, _backStand;
    public SimpleMechanism _drive;

    public static double frontTargetPosition, backTargetPosition;

    public JackStands(LinearMechanism frontStand, LinearMechanism backStand, SimpleMechanism drive){
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

    public void setPosition(String index) {
        MechanismPosition backPos = Fourbar.Constants.Positions.getByIndex(index);
        MechanismPosition frontPos = new MechanismPosition(index, Constants.convertBackToFront(backPos.getTarget()));
        _frontStand.setPosition(frontPos);
        _backStand.setPosition(backPos);
    }
    //WILL CODE
    public void setPosition(double pos) {
        _frontStand.setPosition(pos);
        _backStand.setPosition(pos);
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
        if(OI.driverPad.getYButtonPressed()){
            setPosition("TEST1");
        }else if(OI.driverPad.getAButtonPressed()){
            setPosition("TEST2");
        }else if (OI.driverPad.getYButtonReleased() || OI.driverPad.getAButtonReleased()){
            setPosition(_frontStand.getCurrentPosition());
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
        public static final int ENC_MIN_VAL = -78000 + 8000;
        public static final int ENC_MAX_VAL = -500 - 8000;
        public static final int ENC_RANGE = ENC_MAX_VAL  - ENC_MIN_VAL;
        public static final double MAX_INCHES = 29;
        public static final double ENC_TO_INCHES = MAX_INCHES/(double)ENC_RANGE;
        public static final double INCHES_TO_ENC = 1.0 / ENC_TO_INCHES;

        private static MechanismPosition[] _positions = new MechanismPosition[]{
                new MechanismPosition("TEST1", -8500),
                new MechanismPosition("TEST2", -78000),
                new MechanismPosition("Bottom", 0.0 * INCHES_TO_ENC),
                new MechanismPosition("Middle", 10 * INCHES_TO_ENC),
                new MechanismPosition("Top", MAX_INCHES * INCHES_TO_ENC),
        };

        public static final MechanismPositionList Positions = new MechanismPositionList(_positions);

        public static double convertBackToFront(double back){
            return back;
        }
    }
}
