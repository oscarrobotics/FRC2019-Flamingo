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

@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection", "unused"})
public class SnowBlower extends Subsystem {

	private static SimpleMechanism _intake;
	private static SimpleMechanism _hatchHoldor;
	private static SmartMechanism _hatchGrabbor;
	private static CANifier _canifier;
	private static MiniPID _cargoHeightController, _holderPID;

	private CANifier.LEDRunner sbLeds;

	private double curBallDist = 0;

	private double holdorTarget;

	private CargoPosition _cargoPosition = CargoPosition.UNKNOWN;

	private boolean holdBall = false;

	public SnowBlower(SimpleMechanism intake, SimpleMechanism hatchHolder, CANifier canifier, SmartMechanism hatchGrabber) {
		_intake = intake;
		_hatchHoldor = hatchHolder;
		_canifier = canifier;
		_hatchGrabbor = hatchGrabber;

		_holderPID = new MiniPID(.01, 0,0);
		_cargoHeightController = new MiniPID(Constants.HeightController_kP, Constants.HeightController_kI, Constants.HeightController_kD, Constants.HeightController_kF);

		sbLeds = new SBLeds();
		_canifier.initLEDs(sbLeds);
		_canifier.startLEDs();
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

	}

	public void setHatchHolderPower(double pow) {
		_hatchHoldor.set(pow);
	}

	public void stopHatchHolder () {
		_hatchHoldor.stop();
	}

	public enum CargoPosition {
		UNKNOWN,
		BELOW,
		BOTTOM,
		MIDDLE,
		TOP
	}

	public boolean getHatchCoverStatus() {
		return _canifier.getPinState(com.ctre.phoenix.CANifier.GeneralPin.LIMF);
	}

	public boolean getCargoAtTop() {
		return _canifier.getPinState(com.ctre.phoenix.CANifier.GeneralPin.LIMR);
	}

	public double getCargoHeight(){
		return curBallDist;
	}

	public void setBallStatus(boolean ballStatus){
		setLEDs(LEDMode.STATIC, ballStatus ? Color.ORANGE : Color.GREEN);
		holdBall = ballStatus;
	}

	private double getCANifierPWMData(com.ctre.phoenix.CANifier.PWMChannel pwmChannel) {
		double[] pwmPulseAndPeriod = new double[2];
		_canifier.getPWMInput(pwmChannel, pwmPulseAndPeriod);
		return pwmPulseAndPeriod[0];
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
		//TODO: Find method of getting PWM data from arduino

		if (cargoBelow(curBallDist)) {
			return CargoPosition.BELOW;
		}
		else if (cargoAtBottom(curBallDist)) {
			return CargoPosition.BOTTOM;
		}
		else if (cargoAtMiddle(curBallDist)) {
			return CargoPosition.MIDDLE;
		}
		else if (getCargoAtTop()) {
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

	private boolean cargoBelow(double cargoDistInches) {
		return inRange(cargoDistInches, Constants.CargoFloor_MinInches, Constants.CargoFloor_MaxInches);
	}

	private boolean cargoAtBottom(double cargoDistInches) {
		return inRange(cargoDistInches, Constants.CargoBottom_MinInches, Constants.CargoBottom_MaxInches);
	}

	private boolean cargoAtMiddle(double cargoDistInches) {
		return inRange(cargoDistInches, Constants.CargoMiddle_MinInches, Constants.CargoMiddle_MaxInches);
	}

	private boolean cargoEntered(double cargoDistInches) {
		return inRange(cargoDistInches, Constants.CargoEnter_MinInches, Constants.CargoEnter_MaxInches);
	}

	public void setLEDs(LEDMode ledMode, Color color){
		sbLeds.setMode(ledMode);
		sbLeds.setColor(color);
	}

	public void setLEDs(LEDMode ledMode) {
		setLEDs(ledMode, null);
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
		private Color _color = Color.GREEN;
		private float holdTime = 0.0f;

		@Override
		public void setColor(Color color) {
			if (_color != color) {
				_color = color;
			}
		}

		@Override
		public void setMode(CANifier.LEDMode ledMode) {
			if (_ledMode != ledMode) {
				_ledMode = (LEDMode) ledMode;
			}
		}

		@Override
		public void setOff() {
			_ledMode = LEDMode.OFF;
		}

		@Override
		public void run() {
			switch (_ledMode) {
				case STATIC:
					_canifier.sendColor(_color);
					break;
				case RAINBOW:
					rainbow();
					break;
				case CUSTOM_FLASH:
					_canifier.sendColor(flash(_color, 0.1));
					break;
				case CUSTOM_BREATHE:
					breathe(_color);
					break;
				case BALL_INTAKE:
					breathe(Color.ORANGE, 0.01f);
					break;
				case BALL_HOLD:
					staticColor(Color.ORANGE);
					break;
				case BALL_OUTTAKE:
				    staticColor(Color.RED);
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

		private void staticColor(Color color) {
		    _canifier.sendColor(color);
        }

		private float rHue = 0f;
		private void rainbow(float speed) {
			rHue += speed;
			_canifier.sendHSB(rHue, 1.0f, 0.5f);
		}

		private void rainbow() {
			rainbow(0.02f);
		}

		private void breathe(Color color, float breatheFactor) {
            float[] hsb = getHSBFromColor(color);
            hsb[2] = (float) ((Math.exp(Math.sin((Timer.getFPGATimestamp()*1000)/2000.0*Math.PI)) - breatheFactor)*108.0);
            _canifier.sendHSB(hsb[0], hsb[1], hsb[2]);
        }

        private void breathe(Color color) {
            breathe(color, 0.36787944f);
        }

		private Color flashTempColor = Color.BLACK;
        private Color flash(Color color, double speed) {
			if (timer.hasPeriodPassed(speed)) {
				if (flashTempColor != color) flashTempColor = color;
				else flashTempColor = Color.BLACK;
				timer.reset();
				timer.start();
			}
			return flashTempColor;
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


	@SuppressWarnings("WeakerAccess")
	public static class Constants {

		public static final double HeightController_kP = 1;
		public static final double HeightController_kI = 0;
		public static final double HeightController_kD = 0;
		public static final double HeightController_kF = 0;

		public static final double CargoNominalDiameterInches = 13;

		public static final double CargoFloor_MinInches = 40;
		public static final double CargoFloor_MaxInches = 32;
		public static final double CargoBottom_MinInches = 32.1;
		public static final double CargoBottom_MaxInches = 24;
		public static final double CargoMiddle_MinInches = 24.1;
		public static final double CargoMiddle_MaxInches = 16;
		public static final double CargoTop_MinInches = 16.1;
		public static final double CargoTop_MaxInches = 8;

		public static final double CargoEnter_MinInches = 24;
		public static final double CargoEnter_MaxInches = 8;

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
