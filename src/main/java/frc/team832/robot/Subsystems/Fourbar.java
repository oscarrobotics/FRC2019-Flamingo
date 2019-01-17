package frc.team832.robot.Subsystems;

import frc.team832.GrouchLib.Mechanisms.OscarLinearMechanism;

public class Fourbar {
    private OscarLinearMechanism m_fourbar;
    private static double targetPosition;

    public Fourbar(OscarLinearMechanism fourbar){
        m_fourbar = fourbar;
    }

    public double getTargetPosition(){
        return targetPosition;
    }

    public double getCurrentPosition(){
        return m_fourbar.getPosition();
    }

    public void stop(){
        m_fourbar.stop();
    }

    public void initDefaultCommand() {
        // TODO: Set the default command, if any, for a subsystem here. Example:
        //    setDefaultCommand(new MySpecialCommand());
    }
}
