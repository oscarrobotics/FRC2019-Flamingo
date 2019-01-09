package frc.team832.Subsystems;


import OscarLib.Motion.OscarLinearMechanism;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem {

    private OscarLinearMechanism m_elevator;
    private static double targetPosition;

    public Elevator(OscarLinearMechanism elevator){
        m_elevator = elevator;
    }

    public void setPosition(double position){
        m_elevator.setPosition(position);
        targetPosition = position;
    }

    public double getTargetPosition(){
        return targetPosition;
    }

    public double getCurrentPosition(){
        return m_elevator.getPosition();
    }

    public void stop(){
        m_elevator.stop();
    }

    public void initDefaultCommand() {
        // TODO: Set the default command, if any, for a subsystem here. Example:
        //    setDefaultCommand(new MySpecialCommand());
    }
}

