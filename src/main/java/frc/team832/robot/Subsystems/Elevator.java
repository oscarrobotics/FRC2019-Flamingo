package frc.team832.robot.Subsystems;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Mechanisms.GeniusMechanism;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismMotionProfile;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPositionList;
import frc.team832.GrouchLib.Util.OscarMath;
import frc.team832.robot.OI;

public class Elevator extends Subsystem {

    private GeniusMechanism _elevator;

    private MotionProfileStatus elevatorStatus;

    public Elevator(GeniusMechanism elevator){
        _elevator = elevator;
    }

    public double getTargetPosition(){
        return _elevator.getTargetPosition();
    }

    public double getCurrentPosition(){
        return _elevator.getCurrentPosition();
    }

    public double getCurrentInches() { return Constants.PotToInches(getCurrentPosition()); }

    public void setPosition(String index) {
        _elevator.setPosition(index);
    }

    public void setPosition(double pos){
        _elevator.setPosition(new OscarMechanismPosition("ManualControl", pos));
    }

    @Override
    public void periodic() {
    }

    public void pushData() {
        SmartDashboard.putNumber("Elevator Position", _elevator.getCurrentPosition());
        SmartDashboard.putNumber("Elevator Velocity", _elevator.getVelocity());
        SmartDashboard.putNumber("Elevator Inches", getCurrentInches());
    }

    public void stop(){
        _elevator.stop();
    }

    public void initDefaultCommand() { }

    public void teleopControl() {
        if(OI.driverPad.getYButton()){
            setPosition("TestTop");
        }
        else if (OI.driverPad.getAButton()) {
            setPosition("TestBottom");
        }
        else {
            stop();
        }
    }

    public void bufferTrajectory(OscarMechanismMotionProfile profile) {
        _elevator.bufferTrajectory(profile);
    }

    public MotionProfileStatus getMPStatus(){
        return elevatorStatus;
    }

    public void setMPControl(SetValueMotionProfile v) {
        _elevator.setMotionProfile(v.value);
    }

    public static class Constants {
        public static final int POT_MIN_VAL = 710;
        public static final int POT_MAX_VAL = 374;
        public static final int POT_RANGE = (POT_MAX_VAL - 1023) + 1023 - POT_MIN_VAL;
        public static final double POT_TO_INCHES = 44.0 / (double)POT_RANGE;
        public static final double INCHES_TO_POT = 1 / POT_TO_INCHES;

        public static double PotToInches(double value) {
            return OscarMath.map(value, POT_MIN_VAL, POT_MAX_VAL, 0, 30);
        }
        public static double InchesToPot(double value) { return OscarMath.map(value, 0, 30, POT_MIN_VAL, POT_MAX_VAL);}


        private static final OscarMechanismPosition[] _positions = new OscarMechanismPosition[]{
                new OscarMechanismPosition("TestBottom", POT_MIN_VAL - 50),
                new OscarMechanismPosition("TestMiddle", OscarMath.mid(POT_MAX_VAL, POT_MIN_VAL)),
                new OscarMechanismPosition("TestTop", POT_MAX_VAL + 50),

                new OscarMechanismPosition("Bottom", InchesToPot(3)),
                new OscarMechanismPosition("Middle", InchesToPot(15)),
                new OscarMechanismPosition("Top", InchesToPot(28)),

                new OscarMechanismPosition("CargoShip_Hatch", InchesToPot(28)),
                new OscarMechanismPosition("CargoShip_Cargo", InchesToPot(15)),

                new OscarMechanismPosition("RocketHatch_Low", 28 * INCHES_TO_POT),
                new OscarMechanismPosition("RocketHatch_Middle", 28 * INCHES_TO_POT),
                new OscarMechanismPosition("RocketHatch_High", 28 * INCHES_TO_POT),

                new OscarMechanismPosition("RocketCargo_Low", 28 * INCHES_TO_POT),
                new OscarMechanismPosition("RocketCargo_Middle", 28 * INCHES_TO_POT),
                new OscarMechanismPosition("RocketCargo_High", 28 * INCHES_TO_POT)
        };

        public static final OscarMechanismPositionList Positions = new OscarMechanismPositionList(_positions);
    }
}
