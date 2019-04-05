package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Mechanisms.LinearMechanism;
import frc.team832.GrouchLib.Mechanisms.SimpleMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPositionList;
import frc.team832.robot.OI;
import frc.team832.robot.RobotMap;

import static frc.team832.robot.RobotMap.frontJackStandMotor;

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
        MechanismPosition backPos = Constants.Positions.getByIndex("Back"+index);
        MechanismPosition frontPos = Constants.Positions.getByIndex("Front"+index);
        _frontStand.getMotor().setMotionMagc(frontPos.getTarget());
        _backStand.getMotor().setMotionMagc(backPos.getTarget());
    }
    //WILL CODE

   // public void setFrontPosition(String index) { _frontStand.setPosition(index+"Front"); }
    public void setFrontPosition(double pos) { _frontStand.setPosition(pos); }
    public void setFrontPosition(String index){_frontStand.getMotor().setMotionMagc(_frontStand.getPresetPosition("Front"+index).getTarget());}

    //public void setBackPosition(String index) { _backStand.setPosition(index+"Back"); }
    public void setBackPosition(double pos) { _backStand.setPosition(pos); }
    public void setBackPosition(String index){_backStand.getMotor().setMotionMagc(_backStand.getPresetPosition("Back"+index).getTarget());}

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

    public void jackstandGyroCorrection(){
        double kP = 1000;
        double pitch = RobotMap.navX.getPitch();
        int velocity = (int)(pitch * kP);
        int frontVelocity = -velocity + Constants.FRONT_MAGIC_VEL;
        int backVelocity = velocity + Constants.BACK_MAGIC_VEL;
        frontJackStandMotor.configMotionMagic(frontVelocity, Constants.FRONT_MAGIC_ACC);
        frontJackStandMotor.configMotionMagic(backVelocity, Constants.BACK_MAGIC_ACC);
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

    //negative is forward
    public void setDrivePow(double pow){
        _drive.set(pow);
    }

    @Override
    protected void initDefaultCommand() {}

    public static class Constants {
        public static final int ENC_LVL3_FRONT_VAL = RobotMap.isComp ? -76000 : -79000;
        public static final int ENC_LVL3_BACK_VAL = RobotMap.isComp ? -76000 : -81000;
        public static final int ENC_LVL2_FRONT_VAL = RobotMap.isComp ? -32000 : -26000;
        public static final int ENC_LVL2_BACK_VAL = RobotMap.isComp ? -31000 : -28000;
        public static final int ENC_RETRACT_VAL = 0;
        public static final int ENC_RANGE = ENC_RETRACT_VAL  - ENC_LVL3_BACK_VAL;
        public static final double MAX_INCHES = 29;
        public static final double ENC_TO_INCHES = MAX_INCHES/(double)ENC_RANGE;
        public static final double INCHES_TO_ENC = 1.0 / ENC_TO_INCHES;
        public static final int FRONT_MAGIC_VEL = RobotMap.isComp ? 18000 : 12000;
        public static final int FRONT_MAGIC_ACC = RobotMap.isComp ? 26000 : 12000;
        public static final int BACK_MAGIC_VEL = RobotMap.isComp ? 14000 : 12000;
        public static final int BACK_MAGIC_ACC = RobotMap.isComp ? 17000 : 12000;

        private static MechanismPosition[] _positions = new MechanismPosition[]{
                new MechanismPosition("BackExtendLvl3", ENC_LVL3_BACK_VAL),
                new MechanismPosition("FrontExtendLvl3", ENC_LVL3_FRONT_VAL),
                new MechanismPosition("BackExtendLvl2", ENC_LVL2_BACK_VAL),
                new MechanismPosition("FrontExtendLvl2", ENC_LVL2_FRONT_VAL),
                new MechanismPosition("BackRetract", ENC_RETRACT_VAL),
                new MechanismPosition("FrontRetract", ENC_RETRACT_VAL)

        };

        public enum JackstandPosition {
            ExtendLvl3("ExtendLvl3"),
            ExtendLvl2("ExtendLvl2"),
            Retract("Retract");

            String _index;

            JackstandPosition(String index) { _index = index; }

            public String getIndex() { return _index; }
        }

        public static final MechanismPositionList Positions = new MechanismPositionList(_positions);

        public static double convertBackToFront(double back){
            return back;
        }
    }
}
