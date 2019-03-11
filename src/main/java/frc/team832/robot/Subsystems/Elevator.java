package frc.team832.robot.Subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Mechanisms.GeniusMechanism;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismMotionProfile;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPositionList;
import frc.team832.GrouchLib.Util.OscarMath;
import frc.team832.robot.OI;
import frc.team832.robot.RobotMap;

import static frc.team832.robot.RobotMap.isComp;

public class Elevator extends Subsystem {

    private GeniusMechanism _elevator;

    private MotionProfileStatus elevatorStatus = new MotionProfileStatus();

    public Elevator(GeniusMechanism elevator){
        _elevator = elevator;
    }

    public double getTargetPosition(){
        return _elevator.getTargetPosition();
    }

    public double getCurrentPosition(){
        return _elevator.getCurrentPosition();
    }

    public boolean atTargetPosition() {
        return (Math.abs(getCurrentPosition() - getTargetPosition()) < 20);
    }

    public double getCurrentInches() { return Constants.PotToInches(getCurrentPosition()); }

    public void setPosition(String index) {
        _elevator.setPosition(index);
    }

    public void setPosition(double pos){
        _elevator.setPosition(new MechanismPosition("ManualControl", pos));
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
        if(OI.operatorBox.getRawButton(4)){
            setPosition("Bottom");
        }else if(OI.operatorBox.getRawButton(5)){
            setPosition("Middle");
        }else if(OI.operatorBox.getRawButton(6)) {
            setPosition("Top");
        }else{

        }
    }

    public void setMotionPosition(double position){
        position++;
        position /= 2;
        position *= Constants.POT_RANGE;
        position += -680;
        _elevator.getMotor().setMotionMagc(position);
    }

    public void bufferTrajectory(MechanismMotionProfile profile) {
        _elevator.bufferTrajectory(profile);
    }

    public ErrorCode getMPStatus(){
        return _elevator.getMotionProfileStatus(elevatorStatus);
    }

    public void setMPControl(SetValueMotionProfile v) {
        _elevator.setMotionProfile(v.value);
    }

    public void bufferAndSendMP() {
        _elevator.bufferAndSendMP();
    }

    public boolean isMPFinished() {
        return _elevator.isMPFinished();
    }

    public void testAdjustment(int adjVal) {
        _elevator.setPosition(new MechanismPosition("AdjControl", getTargetPosition()+adjVal));

    }

    public static class Constants {

        public static final int COMP_POT_MIN_VAL = 5;
        public static final int COMP_POT_MAX_VAL = 395;

        public static final int POT_MIN_VAL = -710;
        public static final int POT_MAX_VAL = -370;
        public static final int POT_RANGE = POT_MAX_VAL - POT_MIN_VAL;
        public static final double POT_TO_INCHES = 44.0 / (double)POT_RANGE;
        public static final double INCHES_TO_POT = 1 / POT_TO_INCHES;

        public static double PotToInches(double value) {
            return OscarMath.map(value, POT_MIN_VAL, POT_MAX_VAL, 0, 30);
        }
        public static double InchesToPot(double value) { return OscarMath.map(value, 0, 30, POT_MIN_VAL, POT_MAX_VAL);}

        private static final MechanismPosition[] _positions = new MechanismPosition[]{
                new MechanismPosition("StartConfig", RobotMap.isComp? 0 : -380),
                new MechanismPosition("TestMiddle", OscarMath.mid(POT_MAX_VAL, POT_MIN_VAL)),
                new MechanismPosition("TestTop", POT_MAX_VAL + 50),

                new MechanismPosition("StorageConfig", RobotMap.isComp? 0 : -630),

                new MechanismPosition("Bottom", RobotMap.isComp? -30: -700),
                new MechanismPosition("Middle", RobotMap.isComp? -150 : -500),
                new MechanismPosition("Top", RobotMap.isComp? -390 : -380),

                new MechanismPosition("IntakeCargo_Floor", RobotMap.isComp? 0 : -630),

                new MechanismPosition("RocketHatch_Low", RobotMap.isComp? 0 : -670),
                new MechanismPosition("RocketHatch_Middle", RobotMap.isComp? 0 : -400),
                new MechanismPosition("RocketHatch_High", RobotMap.isComp? 0 : -380),

                new MechanismPosition("RocketCargo_Low", RobotMap.isComp? 0 : -690),
                new MechanismPosition("RocketCargo_Middle", RobotMap.isComp? 0 : -420),
                new MechanismPosition("RocketCargo_High", RobotMap.isComp? 0 : -400)
        };

        public enum ElevatorPosition {
            StartConfig("StartConfig"),
            StorageConfig("StorageConfig"),
            Bottom("Bottom"),
            Middle("Middle"),
            Top("Top"),
            IntakeCargo_Floor("IntakeCargo_Floor"),
            CargoShip_Hatch("CargoShip_Hatch"),
            CargoShip_Cargo("CargoShip_Cargo"),
            RocketHatch_Low("RocketHatch_Low"),
            RocketHatch_Middle("RocketHatch_Middle"),
            RocketHatch_High("RocketHatch_High"),
            RocketCargo_Low ("RocketCargo_Low"),
            RocketCargo_Middle("RocketCargo_Middle"),
            RocketCargo_High("RocketCargo_High");

            String _index;

            ElevatorPosition(String index) { _index = index; }

            public String getIndex() { return _index; }
        }

        public static final MechanismPositionList Positions = new MechanismPositionList(_positions);
    }
}
