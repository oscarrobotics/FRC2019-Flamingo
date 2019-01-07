package OscarLib.Motors;

public class OscarSimpleMotorGroup implements IOscarSimpleMotor {

    private boolean m_isInverted = false;
    private final IOscarSimpleMotor[] m_simpleMotors;

    public OscarSimpleMotorGroup(IOscarSimpleMotor simpleMotor, IOscarSimpleMotor... simpleMotors) {
        m_simpleMotors = new IOscarSimpleMotor[simpleMotors.length + 1];
        m_simpleMotors[0] = simpleMotor;
    }

    @Override
    public void set(double speed) {
        for (IOscarSimpleMotor simpleMotor : m_simpleMotors) {
            simpleMotor.set(m_isInverted ? -speed : speed);
        }
    }

    @Override
    public double get() {
        if (m_simpleMotors.length > 0) {
            return m_simpleMotors[0].get() * (m_isInverted ? -1 : 1);
        }
        return 0.0;
    }

    @Override
    public void setInverted(boolean isInverted) { m_isInverted = isInverted; }

    @Override
    public boolean getInverted() { return m_isInverted; }

    @Override
    public void disable() {
        for (IOscarSimpleMotor simpleMotor : m_simpleMotors) {
            simpleMotor.disable();
        }
    }

    @Override
    public void stopMotor() {
        for (IOscarSimpleMotor simpleMotor : m_simpleMotors) {
            simpleMotor.stopMotor();
        }
    }
}
