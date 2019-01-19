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




        public static OscarMechanismPosition[] ElevatorPositions = new OscarMechanismPosition[]{
                new OscarMechanismPosition("BOTTOM", 0),
                new OscarMechanismPosition("HATCH_BOTTOM", 100),
                new OscarMechanismPosition("CARGO_BOTTOM", 0),
                new OscarMechanismPosition("CARGO_SHIP", 300),
                new OscarMechanismPosition("HATCH_MIDDLE", 500),
                new OscarMechanismPosition("CARGO_MIDDLE", 600),
                new OscarMechanismPosition("HATCH_HIGH", 1000),
                new OscarMechanismPosition("CARGO_HIGH", 1100),
        };
    }


}

