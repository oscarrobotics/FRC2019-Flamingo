package frc.team832.OscarLib;

import com.ctre.phoenix.motorcontrol.ControlMode;

public interface IOscarSmartMotor extends IOscarSimpleMotor {

    void setMode(ControlMode mode);

}
