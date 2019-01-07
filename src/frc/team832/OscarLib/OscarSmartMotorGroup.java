package frc.team832.OscarLib;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class OscarSmartMotorGroup implements IOscarSmartMotor {

    private boolean m_isInverted = false;
    private final IOscarSmartMotor m_masterMotor;
    private final IOscarSmartMotor[] m_slaveMotors;

    public OscarSmartMotorGroup(IOscarSmartMotor masterMotor, IOscarSmartMotor... slaveMotors) {
        m_masterMotor = masterMotor;
        m_slaveMotors = new IOscarSmartMotor[slaveMotors.length];
        for (int i = 0; i < slaveMotors.length; i++) {
            m_slaveMotors[i + 1] = slaveMotors[i];
        }
    }

    @Override
    public void set(double value) {
        m_masterMotor.set( m_isInverted ? -value : value);
    }

    @Override
    public double get() {
        return m_masterMotor.get();
    }


    //this needs to be implemented
    @Override
    public double getPosition(){
        return 0;
    }

    @Override
    public void setInverted(boolean isInverted) { m_isInverted = isInverted; }

    @Override
    public boolean getInverted() { return m_isInverted; }

    @Override
    public void disable() {
        m_masterMotor.disable();
        for (IOscarSmartMotor smartMotor : m_slaveMotors) {
            smartMotor.disable();
        }
    }

    @Override
    public void stopMotor() {
        m_masterMotor.stopMotor();
    }

    @Override
    public void setMode(ControlMode mode) {
        m_masterMotor.setMode(mode);
    }

    @Override
    public void follow(int canID) {
        for (IOscarSmartMotor slaveMotor : m_slaveMotors) {
            slaveMotor.follow(canID);
        }
    }

    @Override
    public double getInputVoltage() {
        double curTotal = 0.0;
        curTotal += m_masterMotor.getInputVoltage();
        for(IOscarSmartMotor slaveMotor : m_slaveMotors ) {
            curTotal += slaveMotor.getInputVoltage();
        }
        return curTotal / (m_slaveMotors.length + 1);
    }

    @Override
    public double getOutputVoltage() {
        return 0;
    }

    @Override
    public double getOutputCurrent() {
        return 0;
    }
}