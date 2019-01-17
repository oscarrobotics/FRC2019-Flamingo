package frc.team832.GrouchLib.Motors;

import com.ctre.phoenix.motorcontrol.ControlMode;

public interface IOscarSmartMotor extends IOscarSimpleMotor {

    /**
     * Sets the {@Link ControlMode} of the controller.
     * @param mode ControlMode to set.
     */
    void setMode(ControlMode mode);

    /**
     * Sets this motor controller to follow the output of another controller.
     * @param masterMotorID CAN ID of the controller to follow.
     */
    void follow(int masterMotorID);

    /**
     * Gets the current encoder position in native ticks.
     * @return Position.
     */
    int getPosition();

    /**
     * Gets the current input voltage of the controller.
     * @return Input in Volts.
     */
    double getInputVoltage();

    /**
     * Gets the current output voltage of the controller.
     * @return Output in Volts.
     */
    double getOutputVoltage();

    /**
     * Gets the current output current of the controller.
     * @return Output in Amps.
     */
    double getOutputCurrent();

    void follow(IOscarSmartMotor masterMotor);

    int getDeviceID();
}
