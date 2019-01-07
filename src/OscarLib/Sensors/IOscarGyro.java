package OscarLib.Sensors;

public interface IOscarGyro {

    /**
     * Initialize the gyro.
     * @return boolean indicating whether initialization succeeded or not
     */
    public boolean init();

    public void calibrate();

    public void zero();

    public double getYaw();

    public double getpitch();

    public double getRoll();

}
