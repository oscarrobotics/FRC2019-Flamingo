package frc.team832.OscarLib;

public class OscarArcadeDrive implements IOscarDriveBase {

    private IOscarSimpleMotor[] _left, _right;
    private IOscarSmartMotor[] _smartLeft, _smartRight;

    public OscarArcadeDrive(IOscarSimpleMotor[] left, IOscarSimpleMotor[] right){
        _left = left;
        _right = right;
    }

    public OscarArcadeDrive(IOscarSmartMotor[] left, IOscarSmartMotor[] right) {
        _left = left;
    }

    @Override
    public void drive(double x, double y) {

    }

    @Override
    public void stop() {

    }
}
