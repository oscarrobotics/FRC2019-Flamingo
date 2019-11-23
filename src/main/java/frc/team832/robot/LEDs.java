package frc.team832.robot;

import edu.wpi.first.wpilibj.Timer;
import frc.team832.lib.driverstation.dashboard.DashboardUpdatable;
import frc.team832.lib.sensors.CANifier;
import frc.team832.lib.util.OscarMath;

import java.awt.*;

@SuppressWarnings({"unused", "WeakerAccess"})
public class LEDs implements DashboardUpdatable {

    private LEDs() {}

    private static CANifier _canifier;
    private static CANifier.LEDRunner ledRunner;

    public static boolean initialize() {

        _canifier = new CANifier(0);
        ledRunner = new IntakeLeds();
        _canifier.initLEDs(ledRunner);
        _canifier.startLEDs();

        return true;
    }

    public static void setLEDs(LEDMode ledMode, Color color) {
        ledRunner.setMode(ledMode);
        ledRunner.setColor(color);
    }

    public static void setLEDs(LEDMode ledMode) {
        setLEDs(ledMode, null);
    }

    public static LEDMode getLEDMode() {
        return IntakeLeds._ledMode;
    }

    public enum LEDMode implements CANifier.LEDMode {
        STATIC,
        RAINBOW,
        DEFAULT,
        CUSTOM_FLASH,
        CUSTOM_BREATHE,
        PREPARE_INTAKE,
        BALL_INTAKE,
        BALL_HOLD,
        BALL_OUTTAKE,
        HATCH_INTAKE,
        HATCH_ACQUIRED,
        HATCH_HOLD,
        HATCH_RELEASE,
        ARM_MOVING,
        JACKSTANDS_MOVING,
        FRONT_JACK_MOVING,
        BACK_JACK_MOVING,
        ALTERNATE_ALLIANCE_GREEN,
        OFF
    }

    private static class Colors {
        static final Color CARGO = new Color(155, 10, 0);
        static final Color HATCH = Color.YELLOW;
        static final Color DEFAULT = Color.GREEN;
    }

    @SuppressWarnings({"unused", "SameParameterValue"})
    public static class IntakeLeds extends CANifier.LEDRunner {
        public static LEDMode _ledMode = LEDMode.STATIC;
        private Color _color = Color.GREEN;
        private double lastMillis;

        @Override
        public void setColor (Color color) {
            if (_color != color) {
                _color = color;
            }
        }

        @Override
        public void setMode (CANifier.LEDMode ledMode) {
            if (_ledMode != ledMode) {
                _ledMode = (LEDMode) ledMode;
            }
            lastMillis = Timer.getFPGATimestamp() * 1000;
        }

        @Override
        public void setOff () {
            _ledMode = LEDMode.OFF;
        }

        @Override
        public void run () {
            double millis = Timer.getFPGATimestamp() * 1000;
            switch (_ledMode) {
                case STATIC:
                    staticColor(_color);
                    break;
                case RAINBOW:
                    rainbow();
                    break;
                case DEFAULT:
                    staticColor(Colors.DEFAULT);
                    break;
                case CUSTOM_FLASH:
                    blink(_color, 0.1f);
                    break;
                case CUSTOM_BREATHE:
                    goodBreathe(_color, 0.01f);
                    break;
                case PREPARE_INTAKE:
                    if (millis - lastMillis < 3000) {
                        staticColor(Color.MAGENTA);
                    } else {
                        staticColor(Colors.DEFAULT);
                    }
                    break;
                case BALL_INTAKE:
                    goodBreathe(Colors.CARGO, 0.01f);
                    break;
                case BALL_HOLD:
                    staticColor(Colors.CARGO);
                    break;
                case BALL_OUTTAKE:
                    goodBreathe(Colors.CARGO, 0.02f);
                    break;
                case HATCH_ACQUIRED:
                    goodBreathe(Color.BLUE, 0.03f);
                    break;
                case HATCH_INTAKE:
                    goodBreathe(Colors.HATCH, 0.01f);
                    break;
                case HATCH_HOLD:
                    staticColor(Color.YELLOW);
                    break;
                case HATCH_RELEASE:
                    goodBreathe(Colors.HATCH, 0.02f);
                    break;
                case ARM_MOVING:
                    if (millis - lastMillis < 1500) {
                        rainbow();
                    } else {
                        staticColor(Colors.DEFAULT);
                    }
                    break;
                case JACKSTANDS_MOVING:
                    goodBreathe(Colors.DEFAULT, 0.02f);
                    break;
                case FRONT_JACK_MOVING:
                    goodBreathe(Color.CYAN, 0.02f);
                    break;
                case BACK_JACK_MOVING:
                    goodBreathe(Color.PINK, 0.02f);
                case ALTERNATE_ALLIANCE_GREEN:
                    break;
                case OFF:
                    _canifier.sendColor(null);
                    break;
            }
        }

        private void staticColor (Color color) {
            _canifier.setLedMaxOutput(1);
            _canifier.sendColor(color);
        }

        private float rHue = 0f;

        private void rainbow(float speed) {
            rHue += speed;
            _canifier.sendHSB(rHue, 1.0f, 0.5f);
        }

        private void rainbow () {
            _canifier.setLedMaxOutput(1);
            rainbow(0.005f);
        }

        private float breatheBrightness = 0f;

        private void breathe(Color color, float breatheFactor) {
            boolean changeDir = breatheBrightness >= 1.0f || breatheBrightness <= 0.0f;
            float[] hsb = getHSBFromColor(color);
            breatheBrightness *= changeDir ? -1 : 1;
            hsb[2] = breatheBrightness;
            _canifier.sendHSB(hsb);
        }

        private void goodBreathe(Color color, float speed) {
            float brightness = (float) ((Math.sin(speed * (Timer.getFPGATimestamp() * 1000)) + 1) / 2);
//            SmartDashboard.putNumber("brightness", brightness);
            _canifier.setLedMaxOutput(OscarMath.clip(brightness, 0, 1));
            _canifier.sendColor(color);
        }

        private void breathe(Color color) {
            breathe(color, 0.02f);
        }

        private Color flashTempColor = Color.BLACK;
        private long lastFlashMillis;
        private void flash (Color color, double speed) {
            double millis = Timer.getFPGATimestamp() * 1000;
            boolean goBlack = (millis - lastFlashMillis) >= speed;
            float[] hsb = getHSBFromColor(color);
            hsb[2] = goBlack ? 0 : hsb[2];
            _canifier.sendHSB(hsb);
        }

        private void blink (Color color, float speed) {
            float[] hsb = getHSBFromColor(color);
            int brightness = (int) (Math.sin(speed * (Timer.getFPGATimestamp() * 1000)) + 1) / 2;
            brightness = OscarMath.clip(brightness, 0, 1);
            hsb[2] = brightness;
            _canifier.sendHSB(hsb);
        }

        private Color hsbToColor(float[] hsbVals) {
            if (hsbVals.length != 3) return Color.BLACK;
            return hsbToColor(hsbVals[0], hsbVals[1], hsbVals[2]);
        }

        private Color hsbToColor(float hue, float sat, float bri) {
            return Color.getHSBColor(hue, sat, bri);
        }

        private float[] getHSBFromColor(Color color) {
            float[] hsb = new float[3];
            Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
            return hsb;
        }

        private Color colorBrightness(Color c, int brightness) {
            double scale = OscarMath.clipMap(brightness, 0, 255, 0, 1);
            int r = Math.min(255, (int) (c.getRed() * scale));
            int g = Math.min(255, (int) (c.getGreen() * scale));
            int b = Math.min(255, (int) (c.getBlue() * scale));
            return new Color(r, g, b);
        }
    }

    @Override
    public String getDashboardTabName() {
        return "LEDs";
    }

    @Override
    public void updateDashboardData() {

    }
}
