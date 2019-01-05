package frc.team832.OscarLib;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class OscarCANTalon implements IOscarSmartMotor {

    private TalonSRX _talon;

    public OscarCANTalon(int canId) {
        _talon = new TalonSRX(canId);
    }

    @Override
    public void set(double power) {
        _talon.set();
    }
}
