package frc.team832.robot.Subsystems;

import frc.team832.GrouchLib.Mechanisms.OscarLinearMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarMechanismPosition;
import frc.team832.GrouchLib.Mechanisms.OscarRotaryMechanism;

public class Fourbar {
    private OscarLinearMechanism m_fourbar;
    private static double targetPosition;

    public Fourbar(OscarRotaryMechanism fourbar){
        m_fourbar = fourbar;
    }

    public double getTargetPosition(){
        return targetPosition;
    }

    public double getCurrentPosition(){
        return m_fourbar.getPosition();
    }

    public void stop(){
        m_fourbar.stop();
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




        public static OscarMechanismPosition[] FourBarPositions = new OscarMechanismPosition[]{
                new OscarMechanismPosition("BOTTOM", (int)(0*INCHES_TO_POT)),
                new OscarMechanismPosition("HATCH_BOTTOM", (int)(3*INCHES_TO_POT)),
                new OscarMechanismPosition("CARGO_BOTTOM", (int)(0*INCHES_TO_POT)),
                new OscarMechanismPosition("CARGO_SHIP", (int)(4*INCHES_TO_POT)),
                new OscarMechanismPosition("HATCH_MIDDLE", (int)(12*INCHES_TO_POT)),
                new OscarMechanismPosition("CARGO_MIDDLE", (int)(16*INCHES_TO_POT)),
                new OscarMechanismPosition("HATCH_HIGH", (int)(25*INCHES_TO_POT)),
                new OscarMechanismPosition("CARGO_HIGH", (int)(29*INCHES_TO_POT)),
        };
    }
}
