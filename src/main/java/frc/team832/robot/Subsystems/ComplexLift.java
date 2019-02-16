package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.OscarComplexMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismComplexPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismComplexPositionList;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;

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

    public boolean getAtTarget() {
        return _mechanism.primaryAtTarget() && _mechanism.secondaryAtTarget();
    }

    public void setPosition(OscarMechanismComplexPosition position) {
        _mechanism.getSecondaryMechanism().setPosition(position.getSecondaryPosition().getIndex());
        if(_mechanism.getSecondaryMechanism().getAtTarget()){
            _mechanism.getPrimaryMechanism().setPosition(position.getPrimaryPosition().getIndex());

        }
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
            new OscarMechanismComplexPosition("IntakeCargo_Floor", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),
            new OscarMechanismComplexPosition("IntakeCargo_HP", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Middle")),

            new OscarMechanismComplexPosition("IntakeHatch_HP", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),
//            new OscarMechanismComplexPosition("IntakeHatch_Floor", Elevator.Constants.Positions, Fourbar.Constants.Positions),

            new OscarMechanismComplexPosition("CargoShip_Hatch", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),
            new OscarMechanismComplexPosition("CargoShip_Cargo", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),

            new OscarMechanismComplexPosition("RocketHatch_Low", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),
            new OscarMechanismComplexPosition("RocketHatch_Middle", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),
            new OscarMechanismComplexPosition("RocketHatch_High", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),

            new OscarMechanismComplexPosition("RocketCargo_Low", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),
            new OscarMechanismComplexPosition("RocketCargo_Middle", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),
            new OscarMechanismComplexPosition("RocketCargo_High", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom"))
        };

        public static OscarMechanismComplexPositionList Positions = new OscarMechanismComplexPositionList(_positions);
    }
}
