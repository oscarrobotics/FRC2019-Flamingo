package frc.team832.OscarLib;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class OscarLinearBoi {

    public OscarSmartMotorGroup m_linearMotors;

    public OscarLinearBoi(OscarSmartMotorGroup linearMotors){
        m_linearMotors = linearMotors;
    }

    public void setPosition(double position){
        m_linearMotors.setMode(ControlMode.Position);
        m_linearMotors.set(position);
    }

    public void getPosition(){
        m_linearMotors.getPosition();
        
    }

}
