package frc.team832.robot.Subsystems;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Mechanisms.GeniusMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismMotionProfile;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPositionList;


public class Fourbar extends Subsystem {

    private GeniusMechanism _top, _bottom;
    private MotionProfileStatus topStatus = new MotionProfileStatus();
    private MotionProfileStatus botStatus = new MotionProfileStatus();

    public Fourbar(GeniusMechanism top, GeniusMechanism bottom){
        _bottom = bottom;
        _top = top;
    }

    public double getTopTargetPosition(){ return _top.getTargetPosition(); }

    public double getTopCurrentPosition(){ return _top.getCurrentPosition(); }

    public double getBottomTargetPosition(){ return _bottom.getTargetPosition(); }

    public double getBottomCurrentPosition(){ return _bottom.getCurrentPosition(); }

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
    }

    public void pushData() {
        SmartDashboard.putNumber("Top Fourbar", getTopCurrentPosition());
        SmartDashboard.putNumber("Bottom Fourbar", getBottomCurrentPosition());
        SmartDashboard.putNumber("Approximate max inches as pot", Constants.inchToPotTick(27));
        SmartDashboard.putNumber("Approximate min inches as pot", Constants.inchToPotTick(-29));
    }

    public void stop(){
        _top.stop();
        _bottom.stop();
    }

    public void setPosition(String index) {
        OscarMechanismPosition upperPos = Constants.Positions.getByIndex(index);
        OscarMechanismPosition lowerPos = new OscarMechanismPosition(index, Constants.convertUpperToLower(upperPos.getTarget()));
        _top.setPosition(upperPos);
        _bottom.setPosition(lowerPos);
    }

    public void setPosition(double pos){
        OscarMechanismPosition upperPos = new OscarMechanismPosition("ManualControl", pos);
        OscarMechanismPosition lowerPos = new OscarMechanismPosition("ManualControl", Constants.convertUpperToLower(upperPos.getTarget()));
        _top.setPosition(upperPos);
        _bottom.setPosition(lowerPos);
    }

    @Override
    public void initDefaultCommand() { }

    public void bufferTrajectories(OscarMechanismMotionProfile topTraj, OscarMechanismMotionProfile botTraj){
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

    public static class Constants {
        public static final double ARMLENGTH = 30.75;
        public static final double UPPERPOTTOANGLE = .262;
        public static final double UPPERPOTOFFSET = 112.66;
        public static final double HEIGHTOFFSET = 2;

        public static final double MAXINCHES = 27;
        public static final double MININCHES = -29;

        private static OscarMechanismPosition[] _positions = new OscarMechanismPosition[]{
                new OscarMechanismPosition("TestMiddle", 450),
                new OscarMechanismPosition("TestTop", 575),
                new OscarMechanismPosition("TestBottom", 250),

                new OscarMechanismPosition("Bottom", inchToPotTick(0.0)),
                new OscarMechanismPosition("Middle", inchToPotTick(0.0)),
                new OscarMechanismPosition("Top", inchToPotTick(0.0)),

                new OscarMechanismPosition("IntakeHatch_HP", inchToPotTick(0.0)),
                new OscarMechanismPosition("IntakeHatch_Floor", inchToPotTick(0.0)),

                new OscarMechanismPosition("CargoShip_Hatch", inchToPotTick(0.0)),
                new OscarMechanismPosition("CargoShip_Cargo", inchToPotTick(0.0)),

                new OscarMechanismPosition("RocketHatch_Low", inchToPotTick(0.0)),
                new OscarMechanismPosition("RocketHatch_Middle", inchToPotTick(0.0)),
                new OscarMechanismPosition("RocketHatch_High", inchToPotTick(0.0)),

                new OscarMechanismPosition("RocketCargo_Low", inchToPotTick(0.0)),
                new OscarMechanismPosition("RocketCargo_Middle", inchToPotTick(0.0)),
                new OscarMechanismPosition("RocketCargo_High", inchToPotTick(0.0)),
        };

        public static final OscarMechanismPositionList Positions = new OscarMechanismPositionList(_positions);

        public static double inchToPotTick(double inches){
            return (Math.toDegrees(Math.asin(inches/ARMLENGTH)) + UPPERPOTOFFSET)/UPPERPOTTOANGLE;
        }

        public static double potTickToInchTop(double potTick){
            return Math.sin(Math.toRadians((potTick*UPPERPOTTOANGLE)-UPPERPOTOFFSET))*ARMLENGTH;
        }

        private static double convertUpperToLower(double upperVal) {
            return (-1.39 * upperVal) + 1044;
        }
    }
}
