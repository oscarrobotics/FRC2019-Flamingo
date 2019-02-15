/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team832.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Motion.OscarSmartDiffDrive;
import frc.team832.GrouchLib.Sensors.OscarCANifier;
import frc.team832.robot.Subsystems.*;

import static frc.team832.robot.RobotMap.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
// If you rename or move this class, update the build.properties file in the project root
public class Robot extends TimedRobot {

    private static final String DEFAULT_AUTO = "Default";
    private static final String CUSTOM_AUTO = "My Auto";
    private String autoSelected;
    private SendableChooser<String> chooser = new SendableChooser<>();

    public static Drivetrain drivetrain;
    public static Elevator elevator;
    public static Fourbar fourbar;
    public static ComplexLift complexLift;
    public static SnowBlower snowBlower;
    public static TheBigOne theBigOne;
    public static JackStands jackStands;

    private static OscarCANifier.Ultrasonic ultrasonic;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {

        if (!RobotMap.init()) {
            // Oh no!
            throw new RuntimeException("Something went wrong during RobotMap.init()! Look above here for more.");
        }

        drivetrain = new Drivetrain(RobotMap.diffDrive);
        elevator = new Elevator(RobotMap.elevatorMech);
        fourbar = new Fourbar(RobotMap.fourbarTopMech, RobotMap.fourbarBottomMech);
//        fourbar.setTopUpperLimit(680);
//        fourbar.setTopLowerLimit(180);
//        fourbar.setBottomLowerLimit(915);
//        fourbar.setBottomUpperLimit(200);
        fourbar.setPID(8,0,0); // TODO: DONT DO THIS HERE GAVIN!!!!!!
//        snowBlower = new SnowBlower(RobotMap.cargoIntake, RobotMap.hatchHolder, RobotMap.canifier, RobotMap.hatchGrabbor);

//        complexLift = new ComplexLift(RobotMap.complexLiftMech);
        jackStands = new JackStands(RobotMap.frontJackStand, RobotMap.backJackStand, RobotMap.jackStandDrive);
        jackStands.setUpperLimit(78500);
        jackStands.setLowerLimit(0);

        jackStands.resetEncoders();

        OI.init();

//        ultrasonic = RobotMap.canifier.getUltrasonic(CANifier.PWMChannel.PWMChannel0, CANifier.PWMChannel.PWMChannel1);
//        ultrasonic.start();

        SmartDashboard.putData("Auto choices", chooser);
    }

    @Override
    public void robotPeriodic() {
        SmartDashboard.putData(pdp.getInstance());
        drivetrain.pushData();
        elevator.pushData();
        fourbar.pushData();
        jackStands.pushData();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString line to get the auto name from the text box below the Gyro
     *
     * <p>You can add additional auto modes by adding additional comparisons to
     * the switch structure below with additional strings. If using the
     * SendableChooser make sure to add them to the chooser code above as well.
     */
    @Override
    public void autonomousInit() {
        autoSelected = chooser.getSelected();
        // autoSelected = SmartDashboard.getString("Auto Selector",
        // defaultAuto);
        System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        switch (autoSelected) {
            case CUSTOM_AUTO:

                break;
            case DEFAULT_AUTO:
            default:
                // Put default auto code here
                break;
        }
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        drivetrain.teleopControl(
                Math.pow(OI.driverPad.getY(GenericHID.Hand.kLeft), 3),
                Math.pow(-OI.driverPad.getX(GenericHID.Hand.kRight), 3),
                Drivetrain.DriveMode.CURVATURE,
                OscarSmartDiffDrive.LoopMode.VELOCITY);

        jackStands.teleopControl();

//        fourbar.teleopControl();
//        elevator.teleopControl();
//        ultrasonic.update();
//        double dist = ultrasonic.getRangeInches();
//        System.out.println("Inches: " + ((dist != 0.0) ? dist : "N/A"));
//        complexLift.mainLoop();
    }

    @Override
    public void disabledInit() {
        jackStands.resetEncoders();
    }
}
