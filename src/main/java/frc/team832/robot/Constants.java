package frc.team832.robot;

public class Constants {

    /**
     * order leftMaster, leftSlave, rightMaster, rightSlave
     */
    public static final int[] driveIDs = {22, 23, 24, 25};

    public static final double[] drivePIDF = {0.06/1000, 0, 0, 0.0003}; // PIDF

    public static final double[] yawPID = {0.0085, 0, 0}; // PID

    public static final long[] dtReductions = {(long) (1f/(12f/64f)), (long) (1f/(36f/76f))};

    public static final double[] frontJackstandPIDF = {0,0,0,0};

    public static final double[] backJackstandPIDF = {0,0,0,0};

    public static final double[] frontJackstandPos = {-1000, -60000, -30000};

    public static final double[] backJackstandPos = {-1000, -60000, -30000};

    private Constants() {} // private constructor to disallow instances
}