package OscarLib.Sensors;

public interface IOscarIMU extends IOscarGyro {

    public double getXAccel();
    public double getYAccel();
    public double getZAccel();
    public double getXDisplacement();
    public double getYDisplacement();
    public double getZDisplacement();
    public double getXMag();
    public double getYMag();
    public double getZMag();
}
