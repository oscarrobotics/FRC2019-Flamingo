package frc.team832.GrouchLib.Motors;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import jaci.pathfinder.Trajectory;

/**
 * Implementation of IOscarSmartMotor that is specific to a CTRE Talon SRX
 */
public class OscarCANTalon implements IOscarGeniusMotor {

    private TalonSRX _talon;
    private ControlMode _ctrlMode;
    private int _curPidIdx = 0;

    private Trajectory _trajegtory;
    private final MotionProfileStatus _mpStatus;
    private int trajSize;
    
    /***
     * Create an OscarCANTalon at the specified CAN ID.
     * @param canId CAN ID of controller to attach.
     */
    public OscarCANTalon(int canId) {
        _talon = new TalonSRX(canId);
        _ctrlMode = ControlMode.Disabled;
        _mpStatus = new MotionProfileStatus();
        set(0);
    }

    @Override
    public void set(double value) {
        _talon.set(_ctrlMode, value);
    }

    @Override
    public double get() {
        return _talon.getMotorOutputPercent();
    }

    @Override
    public int getPosition(){
        return _talon.getSelectedSensorPosition(_curPidIdx);
    }

    @Override
    public void setInverted(boolean isInverted) {
        _talon.setInverted(isInverted);
    }

    @Override
    public boolean getInverted() {
        return _talon.getInverted();
    }

    @Override
    public void disable() {
        _ctrlMode = ControlMode.Disabled;
        set(0);
    }

    @Override
    public void stopMotor() {
        _ctrlMode = ControlMode.PercentOutput;
        set(0);
    }

    @Override
    public void setMode(ControlMode mode) {
        _ctrlMode = mode;
    }

    @Override
    public void follow(int masterMotorID) {
        _ctrlMode = ControlMode.Follower;
        set(masterMotorID);
    }

    @Override
    public double getInputVoltage() {
        return _talon.getBusVoltage();
    }

    @Override
    public double getOutputVoltage() {
        return _talon.getMotorOutputVoltage();
    }

    @Override
    public double getOutputCurrent() {
        return _talon.getOutputCurrent();
    }

    @Override
    public void follow(IOscarSmartMotor masterMotor) {

    }

    @Override
    public int getDeviceID() {
        return _talon.getDeviceID();
    }

    @Override
    public void setTrajectory(Trajectory traj) {
        _trajegtory = traj;
    }

    @Override
    public Trajectory getTrajectory() {
        return _trajegtory;
    }

    @Override
    public int getMpCount() {
        return _mpStatus.btmBufferCnt;
    }

    @Override
    public void startFilling() {
        _talon.clearMotionProfileTrajectories();
        TrajectoryPoint point = new TrajectoryPoint();

        _talon.changeMotionControlFramePeriod(2);
        _talon.configMotionProfileTrajectoryPeriod(25, 10);

        for (int i = 0; i < trajSize; i++) {
            double positionRot = pathfinderFormatToTalon(_trajegtory)[i][0] * 12.0 * (1 / (6 * Math.PI)) * (1 / 1);//TODO: this last 1 needs to be Wheel turns per enconder turn
            double velocityRPM = pathfinderFormatToTalon(_trajegtory)[i][1] * 12.0 * (1 / (6 * Math.PI)) * (1 / 1);//TODO: this last 1 needs to be Wheel turns per enconder turn
            /* for each point, fill our structure and pass it to API */
            point.position = -positionRot * (256.0 * 4.0); // Convert Revolutions to Units
            point.velocity = velocityRPM * (256.0 * 4.0) / 10.0; // Convert RPS to Units/100ms
            point.headingDeg = 0; /* future feature - not used in this example*/
            point.profileSlotSelect0 = 0; /* which set of gains would you like to use [0,3]? */
            point.profileSlotSelect1 = 0; /* future feature  - not used in this example - cascaded PID [0,1], leave zero */
            point.timeDur = (int) pathfinderFormatToTalon(_trajegtory)[i][2];
            point.zeroPos = i == 0;
            point.isLastPoint = false; // HACK: isLastPoint points seem to not play nice with MP

            _talon.pushMotionProfileTrajectory(point);
        }

    }

    @Override
    public MotionProfileStatus getMpStatus() {
        return _mpStatus;
    }

    @Override
    public void enableMpControl() {
        _ctrlMode = ControlMode.MotionProfile;
        _talon.set(_ctrlMode, SetValueMotionProfile.Enable.value);
    }

    @Override
    public void disableMpControl() {
        _ctrlMode = ControlMode.MotionProfile;
        _talon.set(_ctrlMode, SetValueMotionProfile.Disable.value);
    }


    public static double[][] pathfinderFormatToTalon(Trajectory t) {
        int i = 0;
        double[][] list = new double[t.length()][3];
        for (Trajectory.Segment s : t.segments) {
            list[i][0] = s.position;
            list[i][1] = s.velocity;
            list[i][2] = s.dt;
            i++;
        }
        return list;
    }
}
