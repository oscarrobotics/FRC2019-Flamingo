package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.GeniusComplexMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismComplexPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismComplexPositionList;

public class ComplexLift extends Subsystem {

    private GeniusComplexMechanism _mechanism;

    public ComplexLift(GeniusComplexMechanism mechanism) {
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

    public void setPosition(MechanismComplexPosition position) {
        _mechanism.getSecondaryMechanism().setPosition(position.getSecondaryPosition().getIndex());
        if(_mechanism.getSecondaryMechanism().getAtTarget()){
            _mechanism.getPrimaryMechanism().setPosition(position.getPrimaryPosition().getIndex());

        }
    }

    public MechanismComplexPosition getComplexPosition(String index) {
        return _mechanism.getPosition(index);
    }

    public static class Constants {
        // TODO: flesh out ALL POSSIBLE DESIRED POSITIONS across the Elevator and Fourbar. Actual tick count isn't necessary, just the endpoints.

       private static MechanismComplexPosition[] _positions = new MechanismComplexPosition[]{
            // constant positions that are not the same across two mechanisms
            new MechanismComplexPosition("StartingPosition", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),

            // constant positions that are pre-defined and the same across two mechanisms
            new MechanismComplexPosition("IntakeCargo_Floor", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),
            new MechanismComplexPosition("IntakeCargo_HP", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Middle")),

            new MechanismComplexPosition("IntakeHatch_HP", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),

            new MechanismComplexPosition("CargoShip_Hatch", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),
            new MechanismComplexPosition("CargoShip_Cargo", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),

            new MechanismComplexPosition("RocketHatch_Low", Elevator.Constants.Positions.getByIndex("RocketHatch_Low"), Fourbar.Constants.Positions.getByIndex("RocketHatch_Low")),
            new MechanismComplexPosition("RocketHatch_Middle", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),
            new MechanismComplexPosition("RocketHatch_High", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),

            new MechanismComplexPosition("RocketCargo_Low", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),
            new MechanismComplexPosition("RocketCargo_Middle", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom")),
            new MechanismComplexPosition("RocketCargo_High", Elevator.Constants.Positions.getByIndex("Top"), Fourbar.Constants.Positions.getByIndex("Bottom"))
        };

        public static MechanismComplexPositionList Positions = new MechanismComplexPositionList(_positions);
    }
}
