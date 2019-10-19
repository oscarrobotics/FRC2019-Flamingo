package frc.team832.robot;

import frc.team832.robot.subsystems.*;

public class Constants {

    /**
     * order leftMaster, leftSlave, rightMaster, rightSlave
     */
    public static final int[] driveIDs = {22, 23, 24, 25};

    public static final double[] drivePIDF = {0.06/1000, 0, 0, 0.0003}; // PIDF

    public static final double[] yawPID = {0.0085, 0, 0}; // PID

    public static final long[] dtReductions = {(long) (1f/(12f/64f)), (long) (1f/(36f/76f))};



    //Fourbar
    public static final double[] armPIDF = {.75, 0.0, 0.0, 0.0};//.02 ff
    public static final double armGravFF = 0.1;

    public static final double FOURBAR_SOFT_MAX = Fourbar.FourbarPosition.TOP.value + 50;
    public static final double FOURBAR_SOFT_MIN = Fourbar.FourbarPosition.BOTTOM.value - 50;

    //    public static final double FOURBAR_MIN_ANGLE = -67.5; // Before gas shocks
    public static final double FOURBAR_MIN_ANGLE = -70;
    //    public static final double FOURBAR_MAX_ANGLE = 55;
    public static final double FOURBAR_MAX_ANGLE = 52;

    public static final int FOURBAR_VELOCITY = 250; //2 sec for full range (0-5000)
    public static final int FOURBAR_ACCELERATION = 500;



    //Elevator
    public static final double[] elevatorPIDF = {10, 0, 0, 0};

    public static final double ELEVATOR_SOFT_MAX = Elevator.ElevatorPosition.TOP.value + 5;
    public static final double ELEVATOR_SOFT_MIN = Elevator.ElevatorPosition.BOTTOM.value - 5;

    public static final double POT_RANGE = (Elevator.ElevatorPosition.TOP.value) - (Elevator.ElevatorPosition.BOTTOM.value);
    public static final double POT_TO_INCHES = 44.0 / POT_RANGE;
    public static final double INCHES_TO_POT = 1 / POT_TO_INCHES;

    public static final int ELEVATOR_VELOCITY = 60; //approx 1 sec for full range (85-475)
    public static final int ELEVATOR_ACCELERATION = 90;



    //Jackstand
    public static final double[] frontJackstandPIDF = {0,0,0,0};
    public static final double[] backJackstandPIDF = {0,0,0,0};

    public static final double FRONTJACK_SOFT_MAX = Jackstand.JackstandPosition.LVL3_UP.frontValue - 1000;
    public static final double FRONTJACK_SOFT_MIN = Jackstand.JackstandPosition.RETRACTED.frontValue + 1000;

    public static final double BACKJACK_SOFT_MAX = Jackstand.JackstandPosition.LVL3_UP.backValue - 1000;
    public static final double BACKJACK_SOFT_MIN = Jackstand.JackstandPosition.RETRACTED.backValue + 1000;

    public static final int FRONT_JACKSTAND_VELOCITY = 6000; //1 sec for full range
    public static final int FRONT_JACKSTAND_ACCELERATION = 6000;

    public static final int BACK_JACKSTAND_VELOCITY = 6000; //1 sec for full range
    public static final int BACK_JACKSTAND_ACCELERATION = 9000;



    //CAN ID's
    public static final int ELEVATOR_CAN_ID = 6;
    public static final int FOURBARTOP_CAN_ID = 7;
    public static final int FOURBARBOTTOM_CAN_ID = 8;
    public static final int FRONTJACK_CAN_ID = 9;
    public static final int BACKJACK_CAN_ID = 10;
    public static final int CARGOINTAKE_CAN_ID = 12;
    public static final int HATCHINTAKE_CAN_ID = 13;

    private Constants() {} // private constructor to disallow instances
}