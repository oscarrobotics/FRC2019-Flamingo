package frc.team832.robot.Subsystems;

import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;
import frc.team832.GrouchLib.Mechanisms.OscarRotaryMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPositionList;

public class Fourbar {
    private OscarRotaryMechanism _fourbar;

    public Fourbar(OscarRotaryMechanism fourbar){
        _fourbar = fourbar;
    }

    public double getTargetPosition(){ return _fourbar.getTargetPosition(); }

    public double getCurrentPosition(){ return _fourbar.getCurrentPosition(); }

    public void stop(){
        _fourbar.stop();
    }

    public void initDefaultCommand() {
        // TODO: Set the default command, if any, for a subsystem here. Example:
        //    setDefaultCommand(new MySpecialCommand());
    }

	public static class Constants {
        public static final int POT_MIN_VAL = 60;
        public static final int POT_MAX_VAL = 963;
        public static final int POT_RANGE = (POT_MAX_VAL - 1023) + 1023 - POT_MIN_VAL;
        public static final double POT_TO_INCHES = 30.0/(double)POT_RANGE;
        public static final double INCHES_TO_POT = 1/POT_TO_INCHES;

        private static OscarMechanismPosition[] _positions = new OscarMechanismPosition[]{
                new OscarMechanismPosition("BOTTOM", (int)(0*INCHES_TO_POT)),
                new OscarMechanismPosition("MIDDLE", (int)(15*INCHES_TO_POT)),
                new OscarMechanismPosition("TOP", (int)(30*INCHES_TO_POT)),
                new OscarMechanismPosition("HATCH_BOTTOM", (int)(3*INCHES_TO_POT)),
                new OscarMechanismPosition("CARGO_BOTTOM", (int)(0*INCHES_TO_POT)),
                new OscarMechanismPosition("CARGO_SHIP", (int)(4*INCHES_TO_POT)),
                new OscarMechanismPosition("HATCH_MIDDLE", (int)(12*INCHES_TO_POT)),
                new OscarMechanismPosition("CARGO_MIDDLE", (int)(16*INCHES_TO_POT)),
                new OscarMechanismPosition("HATCH_HIGH", (int)(25*INCHES_TO_POT)),
                new OscarMechanismPosition("CARGO_HIGH", (int)(29*INCHES_TO_POT)),
        };

        public static final OscarMechanismPositionList Positions = new OscarMechanismPositionList(_positions);
    }
}
