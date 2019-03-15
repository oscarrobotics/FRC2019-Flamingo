package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Mechanisms.LinearMechanism;
import frc.team832.GrouchLib.Mechanisms.SimpleMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPositionList;
import frc.team832.GrouchLib.Motors.CANTalon;
import frc.team832.robot.OI;
import frc.team832.robot.RobotMap;

import java.awt.*;

public class JackStands extends Subsystem {

    public LinearMechanism _frontStand, _backStand;
    public SimpleMechanism _drive;

    public static double frontTargetPosition, backTargetPosition;

    public JackStands(LinearMechanism frontStand, LinearMechanism backStand, SimpleMechanism drive){
        _frontStand = frontStand;
        _backStand = backStand;
        _drive = drive;

    }

    public enum JackStand {
        FRONT,
        BACK,
        BOTH,//dont use, only for commands
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
        MechanismPosition backPos = Constants.Positions.getByIndex(index+"Back");
        MechanismPosition frontPos = Constants.Positions.getByIndex(index+"Front");
        _frontStand.getMotor().setMotionMagc(frontPos.getTarget());
        _backStand.getMotor().setMotionMagc(backPos.getTarget());
    }
    //WILL CODE

   // public void setFrontPosition(String index) { _frontStand.setPosition(index+"Front"); }
    public void setFrontPosition(double pos) { _frontStand.setPosition(pos); }
    public void setFrontPosition(String index){_frontStand.getMotor().setMotionMagc(_frontStand.getPresetPosition(index+"Front").getTarget());}

    //public void setBackPosition(String index) { _backStand.setPosition(index+"Back"); }
    public void setBackPosition(double pos) { _backStand.setPosition(pos); }
    public void setBackPosition(String index){_backStand.getMotor().setMotionMagc(_backStand.getPresetPosition(index+"Front").getTarget());}

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
        if (OI.driverPad.getPOV() == 180) {
            _drive.set(-.9);
        } else if (OI.driverPad.getPOV() == 0) {
            _drive.set(.9);
        } else {
            _drive.set(0.0);
        }

    }

    public int getFrontError(){
        return _frontStand.getClosedLoopError();
    }

    public int getBackError(){
        return _backStand.getClosedLoopError();
    }

    public double getFrontCorrectionPower() {
        double multiplier = 5;
        double power = 0;
        int error = (int) (getBackCurrentPosition() - getFrontCurrentPosition());
        if (Math.abs(error) > 2000){
            power = error * multiplier;
        }

        return -power;
    }

    public void runToMax(){
        //idk how to do it properly but i try

    }

    public void runToMin(){
        //idk how to do it properly but i try
    }

    public void pushData() {
        SmartDashboard.putNumber("Front Jack Stand", getFrontCurrentPosition());
        SmartDashboard.putNumber("Back Jack Stand", getBackCurrentPosition());
    }

    public boolean getAtTarget(){
        return _backStand.getAtTarget() && _frontStand.getAtTarget();
    }

    public boolean getAtTarget(JackStand stand) {
        return stand.equals(JackStand.FRONT) ? _frontStand.getAtTarget() : _backStand.getAtTarget();
    }

    public boolean getFrontAtTarget() { return getAtTarget(JackStand.FRONT); }

    public boolean getBackAtTarget() { return getAtTarget(JackStand.BACK); }


    //negative is forward
    public void setDrivePow(double pow){
        _drive.set(pow);
    }

    @Override
    protected void initDefaultCommand() {}

    public static class Constants {
        public static final int ENC_MIN_VAL = -75000;
        public static final int ENC_MAX_VAL = -200;
        public static final int ENC_RANGE = ENC_MAX_VAL  - ENC_MIN_VAL;
        public static final double MAX_INCHES = 29;
        public static final double ENC_TO_INCHES = MAX_INCHES/(double)ENC_RANGE;
        public static final double INCHES_TO_ENC = 1.0 / ENC_TO_INCHES;

        private static MechanismPosition[] _positions = new MechanismPosition[]{
                new MechanismPosition("BottomBack", RobotMap.isComp ? -75000 : -78000),
                new MechanismPosition("BottomFront", RobotMap.isComp ? -75000 : -82000),
                new MechanismPosition("TopBack", -300),
                new MechanismPosition("TopFront", -300)
        };

        public static final MechanismPositionList Positions = new MechanismPositionList(_positions);

        public static double convertBackToFront(double back){
            return back;
        }
    }
}
