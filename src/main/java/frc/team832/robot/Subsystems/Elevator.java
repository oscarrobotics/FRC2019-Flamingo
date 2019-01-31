package frc.team832.robot.Subsystems;


import frc.team832.GrouchLib.Mechanisms.OscarLinearMechanism;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPositionList;

public class Elevator extends Subsystem {

    private OscarLinearMechanism _elevator;
    private static double targetPosition;

    public Elevator(OscarLinearMechanism elevator){
        _elevator = elevator;
    }

    public double getTargetPosition(){
        return _elevator.getTargetPosition();
    }

    public double getCurrentPosition(){
        return _elevator.getCurrentPosition();
    }

    public void stop(){
        _elevator.stop();
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

        private static final OscarMechanismPosition[] _positions = new OscarMechanismPosition[]{
                new OscarMechanismPosition("BOTTOM", 0.0*INCHES_TO_POT),
                new OscarMechanismPosition("MIDDLE", 22.0*INCHES_TO_POT),
                new OscarMechanismPosition("TOP", 44*INCHES_TO_POT),
                new OscarMechanismPosition("HATCH_BOTTOM", 4*INCHES_TO_POT),
                new OscarMechanismPosition("CARGO_BOTTOM", 0*INCHES_TO_POT),
                new OscarMechanismPosition("CARGO_SHIP", 6*INCHES_TO_POT),
                new OscarMechanismPosition("HATCH_MIDDLE", 15*INCHES_TO_POT),
                new OscarMechanismPosition("CARGO_MIDDLE", 19*INCHES_TO_POT),
                new OscarMechanismPosition("HATCH_HIGH", 30*INCHES_TO_POT),
                new OscarMechanismPosition("CARGO_HIGH", 34*INCHES_TO_POT)
        };

        public static final OscarMechanismPositionList Positions = new OscarMechanismPositionList(_positions);
    }
}

