package frc.team832.OscarLib;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class OscarCANTalon implements IOscarSmartMotor {

    private TalonSRX _talon;
    private ControlMode _ctrlMode;

    public OscarCANTalon(int canId) {
        _talon = new TalonSRX(canId);
        _ctrlMode = ControlMode.Disabled;
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
}
