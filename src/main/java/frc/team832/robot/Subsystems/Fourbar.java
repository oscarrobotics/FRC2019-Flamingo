package frc.team832.robot.Subsystems;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Mechanisms.GeniusMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismMotionProfile;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPositionList;
import frc.team832.robot.OI;
import frc.team832.robot.Robot;
import frc.team832.robot.RobotMap;

import static frc.team832.robot.RobotMap.isComp;


public class Fourbar extends Subsystem {

    private GeniusMechanism _top, _bottom;
    private MotionProfileStatus topStatus = new MotionProfileStatus();
    private MotionProfileStatus botStatus = new MotionProfileStatus();

    public Fourbar(GeniusMechanism top){
//        _bottom = bottom;
        _top = top;
    }

    public double getTopTargetPosition(){ return _top.getTargetPosition(); }

    public double getTopCurrentPosition(){ return _top.getCurrentPosition(); }

    public double getBottomTargetPosition(){ return _bottom.getTargetPosition(); }

//    public double getBottomCurrentPosition(){ return _bottom.getCurrentPosition(); }

    public boolean atTargetPosition() {
        return (Math.abs(getTopCurrentPosition() - getTopTargetPosition()) < 20);
    }

    public void setTopLimits(int lowerLimit, int upperLimit) {
        _top.setLowerLimit(lowerLimit);
        _top.setUpperLimit(upperLimit);
    }

    public void setBottomLimits(int lowerLimit, int upperLimit) {
        _bottom.setLowerLimit(lowerLimit);
        _bottom.setUpperLimit(upperLimit);
    }

    public void setTopPIDF(double kP, double kI, double kD, double kF) {
        _top.setPIDF(kP, kI, kD, kF);
    }

    public void setBottomPIDF(double kP, double kI, double kD, double kF) {
        _bottom.setPIDF(kP, kI, kD, kF);
    }

    public void teleopControl(){

    }

    @Override
    public void periodic() {
//        if(Robot.elevator.getTargetPosition() > -350 && getTopTargetPosition() < Constants.Positions.getByIndex("Middle").getTarget()){
//            setPosition("Middle");
//        }

    }

    public void pushData() {
        SmartDashboard.putNumber("Top Fourbar", getTopCurrentPosition());
//        SmartDashboard.putNumber("Bottom Fourbar", getBottomCurrentPosition());
        SmartDashboard.putNumber("Bottom Adj", Constants.convertUpperToLower(getTopCurrentPosition()));
    }

    public void stop(){
        _top.stop();
//        _bottom.stop();
    }

    public void setPosition(String index) {
        MechanismPosition upperPos = Constants.Positions.getByIndex(index);
//        MechanismPosition lowerPos = new MechanismPosition(index, Constants.convertUpperToLower(upperPos.getTarget()));
        _top.setPosition(upperPos);
//        _bottom.setPosition(lowerPos);
    }

    public void setPosition(double pos){
        MechanismPosition upperPos = new MechanismPosition("ManualControl", pos);
//        MechanismPosition lowerPos = new MechanismPosition("ManualControl", Constants.convertUpperToLower(upperPos.getTarget()));
        _top.setPosition(upperPos);
//        _bottom.setPosition(lowerPos);
    }

    public void testAdjustment(double adjVal){
        _top.setPosition(new MechanismPosition("AdjControl", getTopTargetPosition()+adjVal));
//        _bottom.setPosition(new MechanismPosition("AdjControl", Constants.convertUpperToLower(getTopTargetPosition()+adjVal)));
    }

    @Override
    public void initDefaultCommand() { }

    public void bufferTrajectories(MechanismMotionProfile topTraj, MechanismMotionProfile botTraj){
        _top.bufferTrajectory(topTraj);
        _bottom.bufferTrajectory(botTraj);
    }

    public MotionProfileStatus getBotMpStatus() {
        return botStatus;
    }

    public MotionProfileStatus getTopMpStatus() {
        return topStatus;
    }

    public void setMPControl(SetValueMotionProfile v) {
        _top.setMotionProfile( v.value);
        _bottom.setMotionProfile(v.value);
    }

    public void bufferAndSendMP() {
        _bottom.bufferAndSendMP();
        _top.bufferAndSendMP();
    }

    public boolean isMPFinished() {
        return _bottom.isMPFinished() && _top.isMPFinished();
    }

    public static class Constants {
        public static final double COMP_TOP_MAX_VAL = 5030;
        public static final double COMP_TOP_MIN_VAL = 0;

        public static final double TOP_MIN_VAL = 200;
        public static final double TOP_MAX_VAL = 661;
        public static final double ARMLENGTH = 30.75;
        public static final double UPPERPOTTOANGLE = .262;
        public static final double UPPERPOTOFFSET = 112.66;
        public static final double HEIGHTOFFSET = 2;

        public static final double MAXINCHES = 27;
        public static final double MININCHES = -29;

        private static MechanismPosition[] _positions = new MechanismPosition[]{
                new MechanismPosition("StartConfig", RobotMap.isComp? 115 : TOP_MIN_VAL),
                new MechanismPosition("TestTop", 630),
                new MechanismPosition("TestBottom", 250),

                new MechanismPosition("StorageConfig", RobotMap.isComp? 675 : 630),

                new MechanismPosition("Bottom", RobotMap.isComp? 300 : TOP_MIN_VAL),
                new MechanismPosition("Middle", RobotMap.isComp? 2455 : 430),
                new MechanismPosition("Top", RobotMap.isComp? 4950 : TOP_MAX_VAL-15),

                new MechanismPosition("IntakeHatch_HP", RobotMap.isComp? 361 : 0),
                new MechanismPosition("IntakeCargo_Floor", RobotMap.isComp? 361 : 420),

                new MechanismPosition("RocketHatch_Low", RobotMap.isComp? 361 : 420),
                new MechanismPosition("RocketHatch_Middle", RobotMap.isComp? 361 : 460),
                new MechanismPosition("RocketHatch_High", RobotMap.isComp? 361 : 640),

                new MechanismPosition("RocketCargo_Low", RobotMap.isComp? 361 : 420),
                new MechanismPosition("RocketCargo_Middle", RobotMap.isComp? 361 : 460),
                new MechanismPosition("RocketCargo_High", RobotMap.isComp? 361 : 640),
        };

        public enum FourbarPosition {
            StartConfig("StartConfig"),
            StorageConfig("StorageConfig"),
            Bottom("Bottom"),
            Middle("Middle"),
            Top("Top"),
            IntakeCargo_Floor("IntakeCargo_Floor"),
            CargoShip_Hatch("CargoShip_Hatch"),
            CargoShip_Cargo("CargoShip_Cargo"),
            RocketHatch_Low("RocketHatch_Low"),
            RocketHatch_Middle("RocketHatch_Middle"),
            RocketHatch_High("RocketHatch_High"),
            RocketCargo_Low ("RocketCargo_Low"),
            RocketCargo_Middle("RocketCargo_Middle"),
            RocketCargo_High("RocketCargo_High");

            String _index;

            FourbarPosition(String index) { _index = index; }

            public String getIndex() { return _index; }
        }

        public static final MechanismPositionList Positions = new MechanismPositionList(_positions);

        public static double inchToPotTick(double inches){
            return (Math.toDegrees(Math.asin(inches/ARMLENGTH)) + UPPERPOTOFFSET)/UPPERPOTTOANGLE;
        }

        public static double potTickToInchTop(double potTick){
            return Math.sin(Math.toRadians((potTick*UPPERPOTTOANGLE)-UPPERPOTOFFSET))*ARMLENGTH;
        }

        public static double convertUpperToLower(double upperVal) {
            return (-1.39 * upperVal) + 1044;
        }

        public static double convertCompUpperToLower(double upperVal) {return (-.452 * upperVal) + 332.38;}
    }
}
