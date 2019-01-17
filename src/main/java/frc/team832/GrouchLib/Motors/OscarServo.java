package frc.team832.GrouchLib.Motors;

import edu.wpi.first.wpilibj.Servo;

public class OscarServo {

    private Servo m_servo;

    public OscarServo(int channel){
        m_servo = new Servo(channel);
    }

    public void set(double target){
        m_servo.set(target);
    }

    public double get(){
        return m_servo.get();
    }

}
