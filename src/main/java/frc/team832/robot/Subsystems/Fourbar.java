package frc.team832.robot.Subsystems;

import frc.team832.GrouchLib.Mechanisms.OscarLinearMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarRotaryMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPositionList;
import frc.team832.GrouchLib.Motors.IOscarCANSmartMotor;


public class Fourbar {
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


    public void stop(){
        _top.stop();
        _bottom.stop();
    }

    public void initDefaultCommand() { }

	public static class Constants {
        public static final double ARMLENGTH = 30.75;
        public static final double UPPERPOTTOANGLE = .262;
        public static final double LOWERPOTTOANGLE = -.183;
        public static final double UPPERPOTOFFSET = 112.66;
        public static final double LOWERPOTOFFSET = 102.128;
        public static final double HEIGHTOFFSET = 2;

        private static OscarMechanismPosition[] _positions = new OscarMechanismPosition[]{
                new OscarMechanismPosition("Bottom", inchToPotTick(0.0, UPPERPOTOFFSET, UPPERPOTTOANGLE)),
                new OscarMechanismPosition("Middle", inchToPotTick(0.0, UPPERPOTOFFSET, UPPERPOTTOANGLE)),
                new OscarMechanismPosition("Top", inchToPotTick(0.0, UPPERPOTOFFSET, UPPERPOTTOANGLE)),

                new OscarMechanismPosition("IntakeHatch_HP", inchToPotTick(0.0, UPPERPOTOFFSET, UPPERPOTTOANGLE)),
                new OscarMechanismPosition("IntakeHatch_Floor", inchToPotTick(0.0, UPPERPOTOFFSET, UPPERPOTTOANGLE)),

                new OscarMechanismPosition("CargoShip_Hatch", inchToPotTick(0.0, UPPERPOTOFFSET, UPPERPOTTOANGLE)),
                new OscarMechanismPosition("CargoShip_Cargo", inchToPotTick(0.0, UPPERPOTOFFSET, UPPERPOTTOANGLE)),

                new OscarMechanismPosition("RocketHatch_Low", inchToPotTick(0.0, UPPERPOTOFFSET, UPPERPOTTOANGLE)),
                new OscarMechanismPosition("RocketHatch_Middle", inchToPotTick(0.0, UPPERPOTOFFSET, UPPERPOTTOANGLE)),
                new OscarMechanismPosition("RocketHatch_High", inchToPotTick(0.0, UPPERPOTOFFSET, UPPERPOTTOANGLE)),

                new OscarMechanismPosition("RocketCargo_Low", inchToPotTick(0.0, UPPERPOTOFFSET, UPPERPOTTOANGLE)),
                new OscarMechanismPosition("RocketCargo_Middle", inchToPotTick(0.0, UPPERPOTOFFSET, UPPERPOTTOANGLE)),
                new OscarMechanismPosition("RocketCargo_High", inchToPotTick(0.0, UPPERPOTOFFSET, UPPERPOTTOANGLE)),
        };

        public static final OscarMechanismPositionList Positions = new OscarMechanismPositionList(_positions);

        public static double inchToPotTick(double inches, double potOffset, double potToAngle){
            return (Math.asin((inches + HEIGHTOFFSET)/ARMLENGTH) + potOffset)/potToAngle;
        }


    }
}
