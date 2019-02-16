package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Mechanisms.OscarLinearMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPositionList;
import frc.team832.robot.OI;


public class Fourbar extends Subsystem {
    private OscarLinearMechanism _top, _bottom;

    public Fourbar(OscarLinearMechanism top, OscarLinearMechanism bottom){
        _bottom = bottom;
        _top = top;
    }

    public double getTopTargetPosition(){ return _top.getTargetPosition(); }

    public double getTopCurrentPosition(){ return _top.getCurrentPosition(); }

    public double getBottomTargetPosition(){ return _bottom.getTargetPosition(); }

    public double getBottomCurrentPosition(){ return _bottom.getCurrentPosition(); }

    public void setTopUpperLimit(int limit){
        _top.setUpperLimit(limit);
    }

    public void setTopLowerLimit(int limit){
        _top.setLowerLimit(limit);
    }

    public void setBottomUpperLimit(int limit){
        _bottom.setUpperLimit(limit);
    }

    public void setBottomLowerLimit(int limit){
        _bottom.setLowerLimit(limit);
    }

    public void setPID(double kP, double kI, double kD){
        _top.setPID(kP, kI, kD);
        _bottom.setPID(kP, kI, kD);
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

    @Override
    public void initDefaultCommand() { }

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
