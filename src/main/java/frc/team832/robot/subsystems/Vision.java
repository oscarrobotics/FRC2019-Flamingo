package frc.team832.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.lib.driverstation.dashboard.DashboardManager;
import frc.team832.lib.driverstation.dashboard.DashboardUpdatable;
import frc.team832.lib.driverstation.dashboard.DashboardWidget;
import frc.team832.lib.util.OscarMath;
import frc.team832.lib.util.RollingAverage;

public class Vision extends SubsystemBase implements DashboardUpdatable {

    private final Solenoid light;
    private double yawKp = .0065;
    private double distanceKp = 0.01;

    public Vision() {
        super();
        light = new Solenoid(0);
        setName("Vision Subsys");
        DashboardManager.addTab(this);
    }

    private final String PS3EyeCamName = "USB Camera-B4.09.24.1";
    private final String defaultCameraTableName = "chameleon-vision/" + PS3EyeCamName;
    private final String defaultCameraPublisherTableName = "CameraPublisher/" + PS3EyeCamName;
    private final NetworkTable chameleonVisionTable = NetworkTableInstance.getDefault().getTable(defaultCameraTableName);
    private final NetworkTable cameraPublisherTable = NetworkTableInstance.getDefault().getTable(defaultCameraPublisherTableName);
    private final NetworkTableEntry chamVis_pitchEntry = chameleonVisionTable.getEntry("pitch"); // double
    private final NetworkTableEntry chamVis_yawEntry = chameleonVisionTable.getEntry("yaw"); // double
    private final NetworkTableEntry chamVis_distanceEntry = chameleonVisionTable.getEntry("distance"); // double
    private final NetworkTableEntry chamVis_timestampEntry = chameleonVisionTable.getEntry("timestamp"); // long
    private final NetworkTableEntry chamVis_isValidEntry = chameleonVisionTable.getEntry("is_valid"); // boolean
    private final NetworkTableEntry chamVis_driverModeEntry = chameleonVisionTable.getEntry("driver_mode"); // boolean (settable)
    private final NetworkTableEntry chamVis_camStreamsEntry = cameraPublisherTable.getEntry("streams"); // String[]

    private NetworkTableEntry dashboard_pitchEntry;
    private NetworkTableEntry dashboard_yawEntry;
    private NetworkTableEntry dashboard_areaEntry;
    private NetworkTableEntry dashboard_isValidEntry;
    private NetworkTableEntry dashboard_distanceEntry;
    private NetworkTableEntry dashboard_driverModeEntry;
    private NetworkTableEntry dashboard_processTimeEntry;
    private NetworkTableEntry dashboard_visionYawKp;
    private NetworkTableEntry dashboard_visionDistanceKp;

    private double visionPitch;
    private double visionYaw;
    private double visionArea;
    private boolean visionIsValid;
    private double visionProcessTime;

    private RollingAverage processTimeAvg = new RollingAverage(50);

    private Thread visionQueryThread;

    private Runnable visionQueryRunnable = () -> {
        while(true) {
            visionPitch = chamVis_pitchEntry.getDouble(0.0);
            visionYaw = chamVis_yawEntry.getDouble(0.0);
            visionArea = chamVis_distanceEntry.getDouble(0.0);
            visionIsValid = chamVis_isValidEntry.getBoolean(false);

            if (visionIsValid) {
                var frameTimestamp = chamVis_timestampEntry.getValue().getTime();
                var rioTimestamp = RobotController.getFPGATime();
                var diff = (rioTimestamp - frameTimestamp) / 1000.0;
                processTimeAvg.add(diff);
                visionProcessTime = processTimeAvg.getAverage();
            } else {
                visionProcessTime = 0;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public boolean init() {
        dashboard_pitchEntry = DashboardManager.addTabItem(this, "CV Pitch", -1.0);
        dashboard_yawEntry = DashboardManager.addTabItem(this, "CV Yaw", -1.0);
        dashboard_areaEntry = DashboardManager.addTabItem(this, "CV Area", -1.0);
        dashboard_isValidEntry = DashboardManager.addTabItem(this, "CV IsValid", false);
        dashboard_driverModeEntry = DashboardManager.addTabItem(this, "CV DriverMode", false, DashboardWidget.ToggleButton);
        dashboard_distanceEntry = DashboardManager.addTabItem(this, "Distance", 0.0);
        dashboard_processTimeEntry = DashboardManager.addTabItem(this, "CV Process Time", "0ms");
        dashboard_visionYawKp = DashboardManager.addTabItem(this, "Yaw Kp", 0);
        dashboard_visionDistanceKp = DashboardManager.addTabItem(this, "Distance Kp", 0);

        visionQueryThread = new Thread(visionQueryRunnable);
        visionQueryThread.start();

        return true;
    }

    @Override
    public String getDashboardTabName() {
        return m_name;
    }

    @Override
    public void updateDashboardData() {
        if (yawKp != dashboard_visionYawKp.getDouble(0.005)) {
            yawKp = dashboard_visionYawKp.getDouble(yawKp);
        }
        if (distanceKp != dashboard_visionDistanceKp.getDouble(0.01)) {
            distanceKp = dashboard_visionDistanceKp.getDouble(distanceKp);
        }

        dashboard_pitchEntry.setDouble(OscarMath.round(visionPitch, 2));
        dashboard_yawEntry.setDouble(OscarMath.round(visionYaw, 2));
        dashboard_areaEntry.setDouble(OscarMath.round(visionArea, 2));
        dashboard_distanceEntry.setDouble(getDistance());
        dashboard_isValidEntry.setBoolean(visionIsValid);
        dashboard_visionDistanceKp.setDouble(distanceKp);
        dashboard_visionYawKp.setDouble(yawKp);

        dashboard_processTimeEntry.setString(OscarMath.round(visionProcessTime, 2) + "ms");

//        var currentDriverModeStatus = chamVis_driverModeEntry.getBoolean(false);
//        var newDriverModeStatus = dashboard_driverModeEntry.getBoolean(false);
//        if (newDriverModeStatus != currentDriverModeStatus) {
//            chamVis_driverModeEntry.setBoolean(newDriverModeStatus);
//        }
    }

    public void setDriverMode(boolean mode){
        chamVis_driverModeEntry.setBoolean(mode);
    }

    public double getYawKp() {
        return yawKp;
    }

    //close 22000
    //far 1000

    public double getAdjustedYawKp() {
        return yawKp * (500.0 / getDistance()) + 0.75;
    }

    public double getDistance() {
        return (500.0 / chamVis_distanceEntry.getDouble(0));
    }

    public double getArea(){
        return dashboard_areaEntry.getDouble(0);
    }

    public double getYaw(){
        return dashboard_yawEntry.getDouble(0);
    }

    public boolean isValid() { return dashboard_isValidEntry.getBoolean(false); }

    public void setLight(boolean on){
        light.set(on);
    }
}
