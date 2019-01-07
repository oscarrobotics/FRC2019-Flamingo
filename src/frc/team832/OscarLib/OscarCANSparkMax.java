package frc.team832.OscarLib;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class OscarCANSparkMax implements IOscarSmartMotor {

    private CANSparkMax _spark;

    public OscarCANSparkMax(int canId){
        _spark = new CANSparkMax(SensorType.kHallSensor, IdleMode.k);
    }


    @Override
    public void setMode(ControlMode mode) {

    }

    @Override
    public void follow(int masterMotorID) {
        _spark.follow(, masterMotorID);
    }

    @Override
    public double getPosition() {
        return _spark.getEncoder();
    }

    @Override
    public double getInputVoltage() {
        return 0;
    }

    @Override
    public double getOutputVoltage() {
        return 0;
    }

    @Override
    public double getOutputCurrent() {
        return 0;
    }

    @Override
    public void set(double speed) {

    }

    @Override
    public double get() {
        return 0;
    }

    @Override
    public void setInverted(boolean isInverted) {
        _spark.setInverted(isInverted);
    }

    @Override
    public boolean getInverted() {
        return _spark.getInverted();
    }

    @Override
    public void disable() {

    }

    @Override
    public void stopMotor() {
        _spark.stopMotor;
    }
}
