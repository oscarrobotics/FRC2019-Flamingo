package frc.team832.robot.Subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.team832.GrouchLib.Util.OscarMath;

public class Vision extends Subsystem {

	private static final double InchesFromFront = 24;

	private static final double Turn_kP = 0.012;
	private static final double Turn_MaxOutput = 0.2;

	private static final double Dist_kP = 0.05;
	private static final double Dist_MaxOutput = 0.4;

	private static NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");

	private static LimelightData latestData;

//	private static final JevoisTracker jevois = RobotMap.jevois;
//	private static JevoisData latestData;
	private static ShuffleboardTab visionTab = Shuffleboard.getTab("Vision");

//	private static NetworkTableEntry hasTargetNTE;
//	private static NetworkTableEntry distanceNTE;
//	private static NetworkTableEntry adjustedDistanceNTE;
//	private static NetworkTableEntry x_offsetNTE;
//	private static NetworkTableEntry yawNTE;
	private static NetworkTableEntry turnAdjustOutputNTE;
	private static NetworkTableEntry turnAdjustkPNTE;
	private static NetworkTableEntry distAdjustOutputNTE;
	private static NetworkTableEntry distAdjustkPNTE;

	private static NetworkTableEntry tx = limelight.getEntry("tx");
	private static NetworkTableEntry ty = limelight.getEntry("ty");
	private static NetworkTableEntry ta = limelight.getEntry("ta");
	private static NetworkTableEntry ts = limelight.getEntry("ts");
	private static NetworkTableEntry tv = limelight.getEntry("tv");

	private static NetworkTableEntry v_tx;
	private static NetworkTableEntry v_ty;
	private static NetworkTableEntry v_ta;
	private static NetworkTableEntry v_ts;
	private static NetworkTableEntry v_tv;

	private static double currentTurn_kP = Turn_kP;
	private static double gyroHeading;
	private static double angle;
	private static double turnAdjustOutput;
	private static final double RIGHT_ROCKET_CLOSE = 29;
	private static final double RIGHT_ROCKET_FAR = 151;
	private static final double RIGHT_ROCKET_CARGO = 90;
	private static final double LEFT_ROCKET_CLOSE = 331;
	private static final double LEFT_ROCKET_FAR = 209;
	private static final double LEFT_ROCKET_CARGO = 270;

	private static double currentDist_kP = Dist_kP;
	private static double distAdjustOutput;

	public Vision () {
		v_tx = visionTab.add("Limelight X", 0.0).getEntry();
		v_ty = visionTab.add("Limelight Y", 0.0).getEntry();
		v_ta = visionTab.add("Limelight Area", 0.0).getEntry();
		v_ts = visionTab.add("Limelight Yaw", 0.0).getEntry();
		v_tv = visionTab.add("Limelight Valid", false).getEntry();
		turnAdjustOutputNTE = visionTab.add("Turn Adj. Out", 0.0).getEntry();
		turnAdjustkPNTE = visionTab.add("Turn Adj. kP", Turn_kP).getEntry();
		distAdjustOutputNTE = visionTab.add("Dist Adj. Out", 0.0).getEntry();
		distAdjustkPNTE = visionTab.add("Dist Adj. kP", Dist_kP).getEntry();
		limelight.getEntry("stream").setNumber(2);
//		hasTargetNTE = visionTab.add("HasTarget", false).getEntry();
//		distanceNTE = visionTab.add("Distance", 0.0).getEntry();
//		adjustedDistanceNTE = visionTab.add("Adj. Dist", 0.0).getEntry();
//		x_offsetNTE = visionTab.add("X Offset", 0.0).getEntry();
//		yawNTE = visionTab.add("Yaw", 0.0).getEntry();
	}

	public void pushData() {
		latestData = new LimelightData();
		latestData.x = tx.getDouble(0.0);
		latestData.y = ty.getDouble(0.0);
		latestData.area = ta.getDouble(0.0);
		latestData.skew = ts.getDouble(0.0);
//		latestData.valid = (int)tv.getNumber(0) == 1;

		v_tx.setDouble(latestData.x);
		v_ty.setDouble(latestData.y);
		v_ta.setDouble(latestData.area);
		v_ts.setDouble(latestData.skew);


//		hasTargetNTE.setBoolean(hasTarget());
//		if (hasTarget()) {
//			distanceNTE.setDouble(latestData.distance);
//			adjustedDistanceNTE.setDouble(latestData.adjustedDistance);
//			x_offsetNTE.setDouble(latestData.x_offset);
//			yawNTE.setDouble(latestData.yaw);
//		}
		turnAdjustOutputNTE.setDouble(turnAdjustOutput);
		distAdjustOutputNTE.setDouble(distAdjustOutput);

		double newTurn_kP = turnAdjustkPNTE.getDouble(Turn_kP);
		if ( newTurn_kP != currentTurn_kP) {
			currentTurn_kP = newTurn_kP;
		}
		turnAdjustkPNTE.setDouble(currentTurn_kP);

		double newDist_kP = distAdjustkPNTE.getDouble(Dist_kP);
		if (newDist_kP != currentDist_kP) {
			currentDist_kP = newDist_kP;
		}
		distAdjustkPNTE.setDouble(currentDist_kP);
	}

	public void main() {
		/*
		getLatestData();
		turnAdjustOutput = calculateTurnAdjust();
		distAdjustOutput = calculateDistAdjust();
		gyroHeading = RobotMap.navX.getYeet();
		*/
	}

	/*
	public boolean hasTarget() {
		return jevois.detectsTape();
	}
	*/

	public double getTurnAdjust() {
		return turnAdjustOutput;
	}

	/*
	private double calculateTurnAdjust() {
		angle = getNearestAngle();
		double anglePower = gyroHeading - angle * 0.01;
		if (hasTarget()) {
			double val = currentTurn_kP * latestData.x_offset;
			return OscarMath.clip(val + anglePower, -Turn_MaxOutput, Turn_MaxOutput);
		}
		else { return 0; }
	}


	private double getNearestAngle(){
		double rFDiff = Math.abs(gyroHeading - RIGHT_ROCKET_CLOSE);
		double rBDiff = Math.abs(gyroHeading - RIGHT_ROCKET_FAR);
		double rCDiff = Math.abs(gyroHeading - RIGHT_ROCKET_CARGO);
		double lFDiff = Math.abs(gyroHeading - LEFT_ROCKET_CLOSE);
		double lBDiff = Math.abs(gyroHeading - LEFT_ROCKET_FAR);
		double lCDiff = Math.abs(gyroHeading - LEFT_ROCKET_CARGO);

		double rDiff = Math.min(rFDiff, rBDiff);
		double lDiff = Math.min(lFDiff, lBDiff);
		double nearest = Math.min(lDiff, rDiff);

		return nearest;
	}

	public double getDistAdjust() {
		return distAdjustOutput;
	}


	private double calculateDistAdjust() {
		if (hasTarget()) {
			double val = currentDist_kP * -latestData.adjustedDistance;
			return OscarMath.clip(val, -Dist_MaxOutput, Dist_MaxOutput);
		}
		else { return 0; }
	}
	*/

	public double getDistPower(){
		double distPow = 1 / latestData.area * currentDist_kP;
		return OscarMath.clip(-distPow, -Dist_MaxOutput, Dist_MaxOutput);
	}

	public double getRotPower(){
		double offsetPow = latestData.x * currentTurn_kP;
		double yawPow = latestData.skew * currentTurn_kP * 0.75;
		return OscarMath.clip((-offsetPow), -Turn_MaxOutput, Turn_MaxOutput);
	}
	/*
	public JevoisData getData() {
		return latestData;
	}

	private void getLatestData() {
		if (jevois.detectsTape()) {
			latestData = parseData(jevois.getParts());
		}
	}

	private JevoisData parseData(String[] rawDataSplit) {
		JevoisData data = new JevoisData();
		try {
			data.distance = Double.parseDouble(rawDataSplit[0]);
			data.adjustedDistance = Math.abs(data.distance - InchesFromFront);
			data.x_offset = Double.parseDouble(rawDataSplit[1]);
			data.yaw = Double.parseDouble(rawDataSplit[2]);
		} catch (Exception ex) {
			DriverStation.reportError("Failed to parse Jevois Data!", ex.getStackTrace());
		}
		return data;
	}
	*/

	public static class LimelightData {
		public double x;
		public double y;
		public double area;
		public double skew;
		public boolean valid;
	}

	public static class JevoisData {
		public double distance;
		public double adjustedDistance;
		public double x_offset;
		public double yaw;
	}

	@Override
	protected void initDefaultCommand () {

	}
}
