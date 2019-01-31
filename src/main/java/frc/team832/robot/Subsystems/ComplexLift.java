package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.OscarComplexMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismComplexPosition;
import frc.team832.robot.Robot;

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
        public static OscarMechanismComplexPosition[] LiftPositions = new OscarMechanismComplexPosition[]{
            new OscarMechanismComplexPosition("Bottom", Elevator.Constants.Positions.getByIndex(""), Fourbar.Constants.Positions.getByIndex("")),


            new OscarMechanismComplexPosition("CargoRocket", Elevator.Constants.Positions.getByIndex(""), Fourbar.Constants.Positions.getByIndex("")),
            new OscarMechanismComplexPosition("CargoBall", Elevator.Constants.Positions.getByIndex(""), Fourbar.Constants.Positions.getByIndex("")),

            new OscarMechanismComplexPosition("IntakeCargoLow", Elevator.Constants.Positions.getByIndex(""), Fourbar.Constants.Positions.getByIndex("")),
            new OscarMechanismComplexPosition("IntakeCargoMiddle", Elevator.Constants.Positions.getByIndex(""), Fourbar.Constants.Positions.getByIndex("")),
            new OscarMechanismComplexPosition("IntakeCargoHigh", Elevator.Constants.Positions.getByIndex(""), Fourbar.Constants.Positions.getByIndex("")),


            new OscarMechanismComplexPosition("RocketHatchBottom", Elevator.Constants.Positions.getByIndex(""), Fourbar.Constants.Positions.getByIndex("")),
            new OscarMechanismComplexPosition("RocketHatchMiddle", Elevator.Constants.Positions.getByIndex(""), Fourbar.Constants.Positions.getByIndex("")),
            new OscarMechanismComplexPosition("RocketHatchTop", Elevator.Constants.Positions.getByIndex(""), Fourbar.Constants.Positions.getByIndex("")),

            new OscarMechanismComplexPosition("RocketCargoBottom", Elevator.Constants.Positions.getByIndex(""), Fourbar.Constants.Positions.getByIndex("")),
            new OscarMechanismComplexPosition("RocketCargoMiddle", Elevator.Constants.Positions.getByIndex(""), Fourbar.Constants.Positions.getByIndex("")),
            new OscarMechanismComplexPosition("RocketCargoTop", Elevator.Constants.Positions.getByIndex(""), Fourbar.Constants.Positions.getByIndex(""))
        };
    }
}
