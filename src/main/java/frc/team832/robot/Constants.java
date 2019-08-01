package frc.team832.robot;

public class Constants {

    /**
     * order leftMaster, leftSlave, rightMaster, rightSlave
     */
    public static final int[] driveIDs = {22, 23, 24, 25};

    public static final double[] drivePIDF = {0.0001, 0, 0, 0.00015}; // PIDF

    public static final double[] yawPID = {0.0085, 0, 0}; // PID

    private Constants() {} // private constructor to disallow instances
}