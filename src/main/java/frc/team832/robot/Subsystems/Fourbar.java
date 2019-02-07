package frc.team832.robot.Subsystems;

import frc.team832.GrouchLib.Mechanisms.OscarLinearMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarRotaryMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPositionList;

public class Fourbar {
    private OscarLinearMechanism _fourbar;

    public Fourbar(OscarLinearMechanism fourbar){
        _fourbar = fourbar;
    }

    public double getTargetPosition(){ return _fourbar.getTargetPosition(); }

    public double getCurrentPosition(){ return _fourbar.getCurrentPosition(); }

    public void stop(){
        _fourbar.stop();
    }

    public void initDefaultCommand() { }

	public static class Constants {
        public static final int POT_MIN_VAL = 60;
        public static final int POT_MAX_VAL = 963;
        public static final int POT_RANGE = (POT_MAX_VAL - 1023) + 1023 - POT_MIN_VAL;
        public static final double MAX_INCHES = 30;
        public static final double POT_TO_INCHES = 30.0/(double)POT_RANGE;
        public static final double INCHES_TO_POT = 1 / POT_TO_INCHES;

        private static OscarMechanismPosition[] _positions = new OscarMechanismPosition[]{
                new OscarMechanismPosition("Bottom", 0.0 * INCHES_TO_POT),
                new OscarMechanismPosition("Middle", 22.0 * INCHES_TO_POT),
                new OscarMechanismPosition("Top", MAX_INCHES * INCHES_TO_POT),

                new OscarMechanismPosition("IntakeHatch_HP", 0 * INCHES_TO_POT),
                new OscarMechanismPosition("IntakeHatch_Floor", 0 * INCHES_TO_POT),

                new OscarMechanismPosition("CargoShip_Hatch", 0 * INCHES_TO_POT),
                new OscarMechanismPosition("CargoShip_Cargo", 0 * INCHES_TO_POT),

                new OscarMechanismPosition("RocketHatch_Low", 6 * INCHES_TO_POT),
                new OscarMechanismPosition("RocketHatch_Middle", 15 * INCHES_TO_POT),
                new OscarMechanismPosition("RocketHatch_High", 19 * INCHES_TO_POT),

                new OscarMechanismPosition("RocketCargo_Low", 30 * INCHES_TO_POT),
                new OscarMechanismPosition("RocketCargo_Middle", 34 * INCHES_TO_POT),
                new OscarMechanismPosition("RocketCargo_High", 34 * INCHES_TO_POT),
        };

        public static final OscarMechanismPositionList Positions = new OscarMechanismPositionList(_positions);
    }
}
