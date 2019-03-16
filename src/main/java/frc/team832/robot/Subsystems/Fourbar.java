package frc.team832.robot.Subsystems;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Mechanisms.GeniusMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismMotionProfile;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPositionList;
import frc.team832.GrouchLib.Util.OscarMath;
import frc.team832.robot.Robot;
import frc.team832.robot.RobotMap;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Fourbar extends Subsystem {

    private GeniusMechanism _top;
    private MotionProfileStatus topStatus = new MotionProfileStatus();
    private int targetPos = 0;

    public Fourbar(GeniusMechanism top){
        _top = top;
    }

    public double getTopTargetPosition(){ return _top.getTargetPosition(); }

    public double getTopCurrentPosition(){ return _top.getCurrentPosition(); }

    public boolean atTargetPosition() {
        return (Math.abs(getTopCurrentPosition() - getTopTargetPosition()) < 20);
    }

    public void setTopLimits(int lowerLimit, int upperLimit) {
        _top.setLowerLimit(lowerLimit);
        _top.setUpperLimit(upperLimit);
    }

    public void setTopPIDF(double kP, double kI, double kD, double kF) {
        _top.setPIDF(kP, kI, kD, kF);
    }

    @Override
    public void periodic() {
        System.out.println(getMinSafePos());
        if (!isSafe()) {
            _top.getMotor().setMotionMagcArbFF(getMinSafePos(), armFF());
        }else{
            _top.getMotor().setMotionMagcArbFF(targetPos, armFF());
        }
    }

    public void pushData() {
        SmartDashboard.putNumber("Top Fourbar", getTopCurrentPosition());
        SmartDashboard.putNumber("Fourbar error: ", _top.getMotor().getClosedLoopError());
        SmartDashboard.putNumber("Bottom Adj", Constants.convertUpperToLower(getTopCurrentPosition()));
        SmartDashboard.putNumber("ArmDeg", armDeg());
    }

    public void stop(){
        _top.stop();
    }

    public void setPosition(String index) {
        MechanismPosition upperPos = Constants.Positions.getByIndex(index);
        _top.setPosition(upperPos);
    }

    public void setPosition(double pos){
        MechanismPosition upperPos = new MechanismPosition("ManualControl", pos);
        _top.setPosition(upperPos);
    }

    public void testAdjustment(double adjVal){
        _top.setPosition(new MechanismPosition("AdjControl", getTopTargetPosition()+adjVal));
    }

    public void setMotionPosition(double position, double arbFF){
        //mid = 2725      -65.5 min to 68.5 max
        targetPos = (int)position;
       // _top.getMotor().setMotionMagcArbFF(position, arbFF);
    }

    public double armDeg() {
        return OscarMath.map(getTopCurrentPosition(), 0, 4900, -65.5, 61  );
    }

    public double armFF (){
        final double gravFF = .09;
        return gravFF * Math.cos(Math.toRadians(armDeg()));
    }

    @Override
    public void initDefaultCommand() { }

    public void bufferTrajectories(MechanismMotionProfile topTraj, MechanismMotionProfile botTraj){
        _top.bufferTrajectory(topTraj);
    }

    public MotionProfileStatus getTopMpStatus() {
        return topStatus;
    }

    public void setMPControl(SetValueMotionProfile v) {
        _top.setMotionProfile(v.value);
    }

    public void bufferAndSendMP() {
        _top.bufferAndSendMP();
    }

    public boolean isMPFinished() {
        return _top.isMPFinished();
    }

    public int getMinSafePos(){
        double fourbarMinPos = (-(-0.0146 * Math.pow(Robot.elevator.getTargetPosition(), 2)) - (16.5 * Robot.elevator.getTargetPosition() - 6000))/2 + 100;//5800 ish
        fourbarMinPos = OscarMath.clip(fourbarMinPos, 0, 2650);
        return (int)fourbarMinPos;
    }

    public boolean isSafe(){
        boolean isSafe;
        int fourbarMinPos = getMinSafePos();
        SmartDashboard.putNumber("Fourbar Safe Min: ", fourbarMinPos);

        isSafe = !(targetPos < fourbarMinPos);
        SmartDashboard.putBoolean("Is Safe: ", isSafe);

        return isSafe;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public static class Constants {
        public static final double COMP_TOP_MAX_VAL = 5100;
        public static final double COMP_TOP_MIN_VAL = 0;

        public static final double TOP_MIN_VAL = 0;
        public static final double TOP_MAX_VAL = 5100;
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

                new MechanismPosition("Bottom", RobotMap.isComp? 300 : 300),
                new MechanismPosition("Middle", RobotMap.isComp? 2455 : 2450),
                new MechanismPosition("Top", RobotMap.isComp? 5300 : 4950),

                new MechanismPosition("IntakeHatch_HP", RobotMap.isComp? 361 : 0),
                new MechanismPosition("IntakeCargo_Floor", RobotMap.isComp? 361 : 420),

                new MechanismPosition("RocketHatch_Low", RobotMap.isComp? 361 : 420),
                new MechanismPosition("RocketHatch_Middle", RobotMap.isComp? 361 : 460),
                new MechanismPosition("RocketHatch_High", RobotMap.isComp? 361 : 640),

                new MechanismPosition("RocketCargo_Low", RobotMap.isComp? 2500 : 420),
                new MechanismPosition("RocketCargo_Middle", RobotMap.isComp? 2350 : 460),
                new MechanismPosition("RocketCargo_High", RobotMap.isComp? 4675 : 640),
        };

        @SuppressWarnings("unused")
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
