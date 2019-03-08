package frc.team832.robot.Subsystems;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPositionList;
import frc.team832.GrouchLib.Mechanisms.SimpleMechanism;
import frc.team832.GrouchLib.Mechanisms.SmartMechanism;
import frc.team832.GrouchLib.Sensors.CANifier;
import frc.team832.GrouchLib.Util.MiniPID;
import frc.team832.GrouchLib.Util.OscarMath;
import frc.team832.robot.OI;

import java.awt.*;

import static frc.team832.GrouchLib.Util.OscarMath.inRange;

@SuppressWarnings("WeakerAccess")
public class SnowBlower extends Subsystem {

    private SimpleMechanism _intake;
    private SimpleMechanism _hatchHoldor;
    private SmartMechanism _hatchGrabbor;
    private CANifier _canifier;
    private CANifier.Ultrasonic _heightUltrasonic;
    private MiniPID _cargoHeightController, _holderPID;

    private CANifier.LEDRunner sbLeds;

    private double curBallDist = 0;

    private double holdorTarget;

    private CargoPosition _cargoPosition = CargoPosition.UNKNOWN;

    private boolean holdBall = false;

    public SnowBlower(SimpleMechanism intake, SimpleMechanism hatchHolder, CANifier canifier, SmartMechanism hatchGrabber){
        _intake = intake;
        _hatchHoldor = hatchHolder;
        _canifier = canifier;
        _hatchGrabbor = hatchGrabber;

        sbLeds = new SBLeds();

        _heightUltrasonic = _canifier.getUltrasonic(Constants.UltrasonicTriggerChannel, com.ctre.phoenix.CANifier.PWMChannel.PWMChannel1);

        _holderPID = new MiniPID(.01, 0,0);
        _cargoHeightController = new MiniPID(Constants.HeightController_kP, Constants.HeightController_kI, Constants.HeightController_kD, Constants.HeightController_kF);
    }

    public void setGrabborPosition(String index){
        _hatchGrabbor.setPosition(index);
    }

    public double getGrabborTargetPosition(String index){
        return _hatchGrabbor.getPresetPosition(index).getTarget();
    }

    public double getGrabborCurrentPosition(){
        return _hatchGrabbor.getCurrentPosition();
    }

    public double getHoldorTargetPosition(){
        return holdorTarget;
    }

    public double getHoldorCurrentPosition(){
        return _canifier.getQuadPosition();
    }

    public void update() {
        _cargoPosition = updateCargoPosition();
    }

    @Override
    public void periodic() {

    }

    public void pushData() {
        SmartDashboard.putNumber("BallDist", curBallDist);
        SmartDashboard.putString("BallPosition", _cargoPosition.toString());
    }

    public void teleopControl() {
        if (OI.driverPad.getAButton()) {
            intakeSet(.5);
        } else if (OI.driverPad.getXButton()) {
            intakeSet(-.5);
        } else if (OI.driverPad.getYButton()) {
            intakeSet(-1);
        } else {
            intakeSet(ballPIDPow());
        }

        if(OI.driverPad.getBumper(GenericHID.Hand.kRight)){
            setBallStatus(true);
        }else if(OI.driverPad.getBumper(GenericHID.Hand.kLeft)){
            setBallStatus(false);
        }
    }

    public void setHatchHolderPower(double pow) {
        _hatchHoldor.set(pow);
    }

    public enum CargoPosition {
        UNKNOWN,
        BOTTOM,
        MIDDLE,
        TOP
    }

    public boolean getHatchCoverStatus() {
        // do fancy limit switch checking here
        return false;
    }

    public double getCargoHeight(){
        return curBallDist;
    }

    public void setBallStatus(boolean ballStatus){
        sbLeds.setColor(ballStatus? Color.ORANGE : Color.GREEN);
        holdBall = ballStatus;
    }


    public double ballPIDPow(){
        if (curBallDist < Constants.CargoBottom_MinInches &&
                curBallDist != -1 && holdBall) {
           return _cargoHeightController.getOutput(curBallDist, Constants.CargoMiddle_MinInches - Constants.CargoMiddle_MaxInches);
        } else {
            return 0.0;
        }
    }

    public CargoPosition getCargoPosition() {
        return _cargoPosition;
    }

    private CargoPosition updateCargoPosition() {
        _heightUltrasonic.update();
        curBallDist = _heightUltrasonic.getRangeInches();

        if (cargoAtBottom(curBallDist)) {
            return CargoPosition.BOTTOM;
        }
        else if (cargoAtMiddle(curBallDist)) {
            return CargoPosition.MIDDLE;
        }
        else if (cargoAtTop(curBallDist)) {
            return CargoPosition.TOP;
        }
        else {
            return CargoPosition.UNKNOWN;
        }
    }

    public void intakeSet(double power) {
        _intake.set(power);
    }

    public void setHatchHolderPosition(String index){
        holdorTarget = Constants.HolderPositions.getByIndex(index).getTarget();
        _holderPID.setSetpoint(holdorTarget);
        _hatchHoldor.set(_holderPID.getOutput(_canifier.getQuadPosition()));
    }

    public void setHatchHolderPosition(double pos){
        holdorTarget = pos;
        _holderPID.setSetpoint(holdorTarget);
        _hatchHoldor.set(_holderPID.getOutput(_canifier.getQuadPosition()));
    }

    private boolean cargoAtBottom(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoBottom_MinInches, Constants.CargoBottom_MaxInches);
    }

    private boolean cargoAtMiddle(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoMiddle_MinInches, Constants.CargoMiddle_MaxInches);
    }

    private boolean cargoAtTop(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoTop_MinInches, Constants.CargoTop_MaxInches);
    }

    private boolean cargoEntered(double cargoDistInches) {
        return inRange(cargoDistInches, Constants.CargoEnter_LeftInches, Constants.CargoEnter_RightInches);
    }

    public void setLEDs(LEDMode ledMode, Color color){
        sbLeds.setMode(ledMode);
        sbLeds.setColor(color);
    }

    public void setLEDs(LEDMode ledMode) {
        setLEDs(ledMode, null);
    }

    public void sendHSB(float hue, float sat, float bri){
        _canifier.sendHSB(hue, sat, bri);
    }

    public enum LEDMode implements CANifier.LEDMode {
        STATIC,
        RAINBOW,
        CUSTOM_FLASH,
        CUSTOM_BREATHE,
        BALL_INTAKE,
        BALL_HOLD,
        BALL_OUTTAKE,
        HATCH_INTAKE,
        HATCH_HOLD,
        HATCH_RELEASE,
        ALTERNATE_ALLIANCE_GREEN,
        OFF;
    }

    public class SBLeds extends CANifier.LEDRunner {
        private LEDMode _ledMode = LEDMode.STATIC;
        private Timer timer = new Timer();
        private Color _color;
        private boolean hold = false;
        private float holdTime = 0.0f;

        @Override
        public void setColor(Color color) { _color = color; }

        @Override
        public void setMode(CANifier.LEDMode ledMode) {
            _ledMode = (LEDMode) ledMode;
        }

        @Override
        public void setOff() {
            _ledMode = LEDMode.OFF;
        }

        @Override
        public void run() {
            if (hold && timer.hasPeriodPassed(holdTime)) {
                switch (_ledMode) {
                    case STATIC:
                        _canifier.sendColor(_color);
                        break;
                    case RAINBOW:
                        rHue += 0.002f;
                        _canifier.sendHSB(rHue, 1.0f, 0.5f);
                        break;
                    case BALL_INTAKE:
                        _canifier.sendColor(breathe(Color.ORANGE, 0.01f));
                        break;
                    case BALL_HOLD:
                        _canifier.sendColor(Color.ORANGE);
                        break;
                    case BALL_OUTTAKE:
                        break;
                    case HATCH_INTAKE:
                        break;
                    case HATCH_HOLD:
                        break;
                    case HATCH_RELEASE:
                        _canifier.sendColor(flash(Color.YELLOW, 0.05));
                        break;
                    case ALTERNATE_ALLIANCE_GREEN:
                        break;
                    case OFF:
                        _canifier.sendColor(null);
                        break;
                }
            }
        }

        private float rHue = 0f;
        private Color rainbow(float speed) {
            rHue += speed;
            return new Color(Color.HSBtoRGB(rHue, 1.0f, 0.5f));
        }

        private Color rainbow() {
            return rainbow(0.02f);
        }

        private Color breathe(Color color, float fadeAmount) {
            float[] hsb = getHSBFromColor(color);
            if (hsb[2] <= 0.0f) { hsb[2] += fadeAmount; }
            else if (hsb[2] >= 1.0f) { hsb[2] -= fadeAmount; }
            return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
        }

        private Color breathe(Color color) {
            return breathe(color, 0.01f);
        }

        private Color flashTempColor;
        private Color flash(Color color, double speed) {
            if (timer.hasPeriodPassed(speed)) {
                if (flashTempColor == null) flashTempColor = color;
                else flashTempColor = null;
                timer.reset();
            }
            return flashTempColor;
        }

        private float[] getHSBFromColor(Color color) {
            return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        }

        private Color colorBrightness(Color c, int brightness) {
            double scale = OscarMath.clipMap(brightness, 0, 255, 0, 1);
            int r = Math.min(255, (int) (c.getRed() * scale));
            int g = Math.min(255, (int) (c.getGreen() * scale));
            int b = Math.min(255, (int) (c.getBlue() * scale));
            return new Color(r, g, b);
        }

    }

    @SuppressWarnings("WeakerAccess")
    public static class Constants {

        public static final double HeightController_kP = 1;
        public static final double HeightController_kI = 0;
        public static final double HeightController_kD = 0;
        public static final double HeightController_kF = 0;

        public static final double CargoNominalDiameterInches = 13;

        public static final double CargoBottom_MinInches = 32.1;
        public static final double CargoBottom_MaxInches = 24;
        public static final double CargoMiddle_MinInches = 24.1;
        public static final double CargoMiddle_MaxInches = 16;
        public static final double CargoTop_MinInches = 16.1;
        public static final double CargoTop_MaxInches = 8;

        public static final double CargoEnter_LeftInches = 8;
        public static final double CargoEnter_RightInches = 12;

        public static final com.ctre.phoenix.CANifier.PWMChannel UltrasonicTriggerChannel = com.ctre.phoenix.CANifier.PWMChannel.PWMChannel0;

        public static final MechanismPosition[] holderPositions = new MechanismPosition[]{
                //TODO: put actual numbers here
                new MechanismPosition("Open", 220),
                new MechanismPosition("Closed", 200)
        };

        public static final MechanismPositionList HolderPositions = new MechanismPositionList(holderPositions);

        public static final MechanismPosition[] grabberPositions = new MechanismPosition[]{
                //TODO: put actual numbers here
                new MechanismPosition("Initial", 100),
                new MechanismPosition("Release", 200),
                new MechanismPosition("Floor", 700)
        };
    }

    @Override
    protected void initDefaultCommand() { }
}
