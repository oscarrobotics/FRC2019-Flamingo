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
        public static OscarMechanismComplexPosition[] LiftPositions = new OscarMechanismComplexPosition[]{
            new OscarMechanismComplexPosition("Bottom", Elevator.Constants.ElevatorPositions[1], Fourbar.Constants.FourBarPositions[0]),
            new OscarMechanismComplexPosition("RocketHatchLow", Elevator.Constants.ElevatorPositions[1], Fourbar.Constants.FourBarPositions[0]),
            new OscarMechanismComplexPosition("RocketHatchMiddle", Elevator.Constants.ElevatorPositions[1], Fourbar.Constants.FourBarPositions[0]),
            new OscarMechanismComplexPosition("RocketHatchTop", Elevator.Constants.ElevatorPositions[1], Fourbar.Constants.FourBarPositions[0]),
            new OscarMechanismComplexPosition("RocketCargoLow", Elevator.Constants.ElevatorPositions[1], Fourbar.Constants.FourBarPositions[0]),
            new OscarMechanismComplexPosition("RocketCargoMiddle", Elevator.Constants.ElevatorPositions[1], Fourbar.Constants.FourBarPositions[0]),
            new OscarMechanismComplexPosition("RocketCargoHigh", Elevator.Constants.ElevatorPositions[1], Fourbar.Constants.FourBarPositions[0]),
            new OscarMechanismComplexPosition("CargoRocket", Elevator.Constants.ElevatorPositions[1], Fourbar.Constants.FourBarPositions[0]),
            new OscarMechanismComplexPosition("CargoBall", Elevator.Constants.ElevatorPositions[1], Fourbar.Constants.FourBarPositions[0])
        };
    }
}
