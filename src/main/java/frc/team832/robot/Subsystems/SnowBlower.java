package frc.team832.robot.Subsystems;

import com.ctre.phoenix.CANifier;
import frc.team832.GrouchLib.Mechanisms.OscarSimpleMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarSmartMechanism;
import frc.team832.GrouchLib.Sensors.OscarCANifier;

import static frc.team832.GrouchLib.Util.OscarMath.inRange;

public class SnowBlower {

    private OscarSimpleMechanism _intake;
    private OscarSmartMechanism _hatchHoldor;
    private OscarSmartMechanism _hatchGrabbor;
    private OscarCANifier _canifier;
    private OscarCANifier.Ultrasonic _heightUltrasonic;

    public SnowBlower(OscarSimpleMechanism intake, OscarSmartMechanism hatchHolder, OscarCANifier canifier, OscarSmartMechanism hatchGrabber){
        _intake = intake;
        _hatchHoldor = hatchHolder;
        _canifier = canifier;
        _hatchGrabbor = hatchGrabber;

        _heightUltrasonic = _canifier.addUltrasonic(CANifier.PWMChannel.PWMChannel0, CANifier.PWMChannel.PWMChannel1);
    }

    public enum CargoPosition {
        UNKNOWN,
        BOTTOM,
        BOTTOM_CENTERED,
        MIDDLE,
        TOP
    }

    public boolean getHatchCoverStatus() {
        // do fancy limit switch checking here
        return false;
    }

    public CargoPosition getCargoPosition() {
        // todo: determine how to differentiate the cargo being centered in the bottom, or being off-center in the bottom
        // is this even really necessary? we could just bring the ball to the middle and it'd be centered.
        // maybe some combination of reflectance/optical and a line break?
        // maybe even another ultrasonic going across the bottom?

        _heightUltrasonic.update();
        double cargoDist = _heightUltrasonic.getRangeInches();
        boolean cargoBottomSensor = true;

        if (cargoBottomSensor && cargoAtBottom(cargoDist)) {
            return CargoPosition.BOTTOM_CENTERED;
        }
        else if (cargoBottomSensor) {
            return CargoPosition.BOTTOM;
        }
        else if (cargoAtMiddle(cargoDist)) {
            return CargoPosition.MIDDLE;
        }
        else if (cargoAtTop(cargoDist)) {
            return CargoPosition.TOP;
        }
        else {
            return CargoPosition.UNKNOWN;
        }
    }

    public void intakeSet(double power) {
        _intake.set(power);
    }

    private boolean cargoAtBottom(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoBottomMinInches, Constants.CargoBottomMaxInches);
    }

    private boolean cargoAtMiddle(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoMiddleMinInches, Constants.CargoMiddleMaxInches);
    }

    private boolean cargoAtTop(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoTopMinInches, Constants.CargoTopMaxInches);
    }

    public static class Constants {
        public static final double CargoBottomMinInches = 32.1;
        public static final double CargoBottomMaxInches = 28;
        public static final double CargoMiddleMinInches = 28.1;
        public static final double CargoMiddleMaxInches = 24;
        public static final double CargoTopMinInches = 24.1;
        public static final double CargoTopMaxInches = 20;
    }
}
