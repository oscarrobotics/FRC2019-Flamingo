package frc.team832.robot;

import frc.team832.lib.motors.DTPowerTrain;
import frc.team832.lib.motors.Gearbox;
import frc.team832.lib.motors.Motors;
import frc.team832.robot.subsystems.*;

public class Constants {

    /**
     * order leftMaster, leftSlave, rightMaster, rightSlave
     */
    public static final int[] DRIVE_IDS = {22, 23, 24, 25};

    public static final double[] DRIVE_PIDF = {0.06/1000, 0, 0, 0.0003}; // PIDF
    public static final double[] DRIVE_PATH_PIDVA = {0.8, 0, 0, 1/3.3528, 1/10.83};
    public static final double DRIVE_ACCEL_RAMP = 0.25;
    public static final double DRIVE_DECEL_RAMP = 0.5;
    public static final double WHEEL_DIAMETER_METERS = 0.14986;
    public static final float[] DT_REDUCTIONS = {(1f/(12f/64f)), (1f/(36f/76f))};
    public static final Gearbox DRIVE_GEARBOX = new Gearbox(DT_REDUCTIONS[0], DT_REDUCTIONS[1]);
    public static final DTPowerTrain DRIVE_POWERTRAIN = new DTPowerTrain(DRIVE_GEARBOX, Motors.NEO, 2, WHEEL_DIAMETER_METERS);

    public static final double DRIVE_PATH_MAX_VELOCITY_METERS_PER_SEC = 3.3528/2;
    public static final double DRIVE_PATH_MAX_ACCELERATION_METERS_PER_SEC_SQ = 10.83/2 ;

    public static final double[] YAW_PID = {0.0085, 0, 0}; // PID

    //Fourbar
    public static final double[] ARM_PIDF = {.75, 0.0, 0.0, 0.0};//.02 ff
    public static final double ARM_GRAV_FF = 0.1;

    public static final double FOURBAR_SOFT_MAX = Fourbar.FourbarPosition.TOP.value + 50;
    public static final double FOURBAR_SOFT_MIN = Fourbar.FourbarPosition.BOTTOM.value - 50;

    //    public static final double FOURBAR_MIN_ANGLE = -67.5; // Before gas shocks
    public static final double FOURBAR_MIN_ANGLE = -70;
    //    public static final double FOURBAR_MAX_ANGLE = 55;
    public static final double FOURBAR_MAX_ANGLE = 52;

    public static final int FOURBAR_VELOCITY = 200;
    public static final int FOURBAR_ACCELERATION = 600;



    //Elevator
    public static final double[] elevatorPIDF = {8, 0, 0, 0};

    public static final double ELEVATOR_SOFT_MAX = Elevator.ElevatorPosition.TOP.value + 5;
    public static final double ELEVATOR_SOFT_MIN = Elevator.ElevatorPosition.BOTTOM.value - 5;

    public static final double POT_RANGE = (Elevator.ElevatorPosition.TOP.value) - (Elevator.ElevatorPosition.BOTTOM.value);
    public static final double POT_TO_INCHES = 44.0 / POT_RANGE;
    public static final double INCHES_TO_POT = 1 / POT_TO_INCHES;

    public static final int ELEVATOR_VELOCITY = 80;
    public static final int ELEVATOR_ACCELERATION = 160;

    public static final double ELEVATOR_ARBFF = 0.3;

    //Jackstand
    public static final double[] FRONT_JACKSTAND_PIDF = {0,0,0,0};
    public static final double[] BACK_JACKSTAND_PIDF = {0,0,0,0};

    public static final double FRONTJACK_SOFT_MAX = Jackstand.JackstandPosition.LVL3_UP.frontValue - 1000;
    public static final double FRONTJACK_SOFT_MIN = Jackstand.JackstandPosition.RETRACTED.frontValue + 1000;

    public static final double BACKJACK_SOFT_MAX = Jackstand.JackstandPosition.LVL3_UP.backValue - 1000;
    public static final double BACKJACK_SOFT_MIN = Jackstand.JackstandPosition.RETRACTED.backValue + 1000;

    public static final int FRONT_JACKSTAND_VELOCITY = 6000; //1 sec for full range
    public static final int FRONT_JACKSTAND_ACCELERATION = 9000;

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