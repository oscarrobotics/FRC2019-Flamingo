package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.OscarComplexMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarMechanismComplexPosition;

public class ComplexLift extends Subsystem {

    private OscarComplexMechanism _mechanism;

    public ComplexLift(OscarComplexMechanism mechanism) {
        _mechanism = mechanism;
    }

    @Override
    protected void initDefaultCommand() { }

    public static class Constants {

        // TODO: flesh out ALL POSSIBLE DESIRED POSITIONS across the Elevator and Fourbar. Actual tick count isn't necessary, just the endpoints.
        public static OscarMechanismComplexPosition[] LiftPositions;
    }
}
