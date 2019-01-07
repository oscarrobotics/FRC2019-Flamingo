package OscarLib.Motion;

import com.ctre.phoenix.motorcontrol.ControlMode;
import OscarLib.Motors.IOscarSmartMotor;

public class OscarLinearMechanism {

    private IOscarSmartMotor m_linearMotor;

    public OscarLinearMechanism(IOscarSmartMotor linearMotors){
        m_linearMotor = linearMotors;
    }

    public void setPosition(double position){
        m_linearMotor.setMode(ControlMode.Position);
        m_linearMotor.set(position);
    }

    public void getPosition(){
        m_linearMotor.getPosition();
    }
}
