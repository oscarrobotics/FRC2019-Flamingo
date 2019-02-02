package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.OscarComplexMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismComplexPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismComplexPositionList;

public class ComplexLift extends Subsystem {

    private OscarComplexMechanism _mechanism;

    public ComplexLift(OscarComplexMechanism mechanism) {
        _mechanism = mechanism;
    }

    @Override
    protected void initDefaultCommand() { }

    // the looping logic for this mechanism
    @Override
    public void periodic() {

    }

    public void setPosition(OscarMechanismComplexPosition position) {
        _mechanism.setPosition(position);
    }

    public OscarMechanismComplexPosition getComplexPosition(String index) {
        return _mechanism.getPosition(index);
    }

    public static class Constants {
        // TODO: flesh out ALL POSSIBLE DESIRED POSITIONS across the Elevator and Fourbar. Actual tick count isn't necessary, just the endpoints.

       private static OscarMechanismComplexPosition[] _positions = new OscarMechanismComplexPosition[]{
            // constant positions that are not the same across two mechanisms
            new OscarMechanismComplexPosition("StartingPosition", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),

            // constant positions that are pre-defined and the same across two mechanisms
            new OscarMechanismComplexPosition("IntakeCargo_Low", Elevator.Constants.Positions, Fourbar.Constants.Positions),
            new OscarMechanismComplexPosition("IntakeCargo_Middle", Elevator.Constants.Positions, Fourbar.Constants.Positions),
            new OscarMechanismComplexPosition("IntakeCargo_High", Elevator.Constants.Positions, Fourbar.Constants.Positions),

            new OscarMechanismComplexPosition("IntakeHatch_HP", Elevator.Constants.Positions, Fourbar.Constants.Positions),
            new OscarMechanismComplexPosition("IntakeHatch_Floor", Elevator.Constants.Positions, Fourbar.Constants.Positions),

            new OscarMechanismComplexPosition("CargoShip_Hatch", Elevator.Constants.Positions, Fourbar.Constants.Positions),
            new OscarMechanismComplexPosition("CargoShip_Cargo", Elevator.Constants.Positions, Fourbar.Constants.Positions),

            new OscarMechanismComplexPosition("RocketHatch_Low", Elevator.Constants.Positions, Fourbar.Constants.Positions),
            new OscarMechanismComplexPosition("RocketHatch_Middle", Elevator.Constants.Positions, Fourbar.Constants.Positions),
            new OscarMechanismComplexPosition("RocketHatch_High", Elevator.Constants.Positions, Fourbar.Constants.Positions),

            new OscarMechanismComplexPosition("RocketCargo_Low", Elevator.Constants.Positions, Fourbar.Constants.Positions),
            new OscarMechanismComplexPosition("RocketCargo_Middle", Elevator.Constants.Positions, Fourbar.Constants.Positions),
            new OscarMechanismComplexPosition("RocketCargo_High", Elevator.Constants.Positions, Fourbar.Constants.Positions)
        };

        public static OscarMechanismComplexPositionList Positions = new OscarMechanismComplexPositionList(_positions);
    }
}
