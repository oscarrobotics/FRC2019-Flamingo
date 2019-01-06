package frc.team832.OscarLib;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class OscarCANTalon implements IOscarSmartMotor {

    private TalonSRX _talon;
    private ControlMode _ctrlMode;

    public OscarCANTalon(int canId) {
        _talon = new TalonSRX(canId);
        _ctrlMode = ControlMode.Disabled;
    }

    @Override
    public void set(double power) {
        _talon.set(_ctrlMode, power);
    }

    @Override
    public void setMode(ControlMode mode) {
        _ctrlMode = mode;
    }
}
