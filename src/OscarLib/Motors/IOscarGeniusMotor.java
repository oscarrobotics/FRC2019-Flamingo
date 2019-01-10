package OscarLib.Motors;


import com.ctre.phoenix.motion.MotionProfileStatus;
import jaci.pathfinder.Trajectory;

public interface IOscarGeniusMotor extends IOscarSmartMotor{

    void setTrajectory(Trajectory traj);

    Trajectory getTrajectory();

    int getMpCount();
    
    void startFilling();
    
    MotionProfileStatus getMpStatus();

    void enableMpControl();

    void disableMpControl();
    
}
