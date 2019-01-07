package frc.team832.OscarLib;

import com.ctre.phoenix.motorcontrol.ControlMode;

public interface IOscarSmartMotor extends IOscarSimpleMotor {

    void setMode(ControlMode mode);

    void follow(int masterMotorID);

    double getPosition();

    double getInputVoltage();
    double getOutputVoltage();

    double getOutputCurrent();
}
