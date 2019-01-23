package frc.team832.robot.Subsystems;


import frc.team832.GrouchLib.Mechanisms.OscarLinearMechanism;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.OscarMechanismPosition;

public class Elevator extends Subsystem {

    private OscarLinearMechanism m_elevator;
    private static double targetPosition;

    public Elevator(OscarLinearMechanism elevator){
        m_elevator = elevator;
    }

    public double getTargetPosition(){
        return targetPosition;
    }

    public double getCurrentPosition(){
        return m_elevator.getPosition();
    }

    public void stop(){
        m_elevator.stop();
    }

    public void initDefaultCommand() {
        // TODO: Set the default command, if any, for a subsystem here. Example:
        //    setDefaultCommand(new MySpecialCommand());
    }

    public static class Constants {
        public static final int POT_MIN_VAL = 60;
        public static final int POT_MAX_VAL = 963;
        public static final int POT_RANGE = (POT_MAX_VAL - 1023) + 1023 - POT_MIN_VAL;
        public static final double POT_TO_INCHES = 44.0/(double)POT_RANGE;
        public static final double INCHES_TO_POT = 1/POT_TO_INCHES;

        public static final OscarMechanismPosition[] ElevatorPositions = new OscarMechanismPosition[]{
                new OscarMechanismPosition("BOTTOM", (int)(0*INCHES_TO_POT)),
                new OscarMechanismPosition("MIDDLE", (int)(22*INCHES_TO_POT)),
                new OscarMechanismPosition("TOP", (int)(44*INCHES_TO_POT)),
                new OscarMechanismPosition("HATCH_BOTTOM", (int)(4*INCHES_TO_POT)),
                new OscarMechanismPosition("CARGO_BOTTOM", (int)(0*INCHES_TO_POT)),
                new OscarMechanismPosition("CARGO_SHIP", (int)(6*INCHES_TO_POT)),
                new OscarMechanismPosition("HATCH_MIDDLE", (int)(15*INCHES_TO_POT)),
                new OscarMechanismPosition("CARGO_MIDDLE", (int)(19*INCHES_TO_POT)),
                new OscarMechanismPosition("HATCH_HIGH", (int)(30*INCHES_TO_POT)),
                new OscarMechanismPosition("CARGO_HIGH", (int)(34*INCHES_TO_POT)),
        };
    }


}

