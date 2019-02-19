/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team832.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Motion.OscarSmartDiffDrive;
import frc.team832.GrouchLib.Sensors.OscarCANifier;
import frc.team832.GrouchLib.Util.MiniPID;
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
    public static OI oi;
    public MiniPID holderPID;

    public double kP = .00025, kI = 0.0, kD = 0.0, kF = 0.0;
    private static OscarCANifier.Ultrasonic ultrasonic;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {

        if (!RobotMap.init()) {
            // Oh no!
            System.out.println("Something went wrong during RobotMap.init()! Look above here for more");
            throw new RuntimeException("Something went wrong during RobotMap.init()! Look above here for more.");
        }

        drivetrain = new Drivetrain(RobotMap.diffDrive);
        elevator = new Elevator(RobotMap.elevatorMech);
        fourbar = new Fourbar(RobotMap.fourbarTopMech, RobotMap.fourbarBottomMech);
        jackStands = new JackStands(RobotMap.frontJackStand, RobotMap.backJackStand, RobotMap.jackStandDrive);
        System.out.println("D, E, F, J INIT");

        holderPID = new MiniPID(1, 0, 0);

        snowBlower = new SnowBlower(RobotMap.cargoIntake, RobotMap.hatchHolder, holderPID, RobotMap.canifier, RobotMap.hatchGrabbor);

        complexLift = new ComplexLift(RobotMap.complexLiftMech);

        theBigOne = new TheBigOne(complexLift, snowBlower);

        jackStands.resetEncoders();

        oi = new OI();
        System.out.println("OI INIT");

//        ultrasonic = RobotMap.canifier.getUltrasonic(CANifier.PWMChannel.PWMChannel0, CANifier.PWMChannel.PWMChannel1);
//        ultrasonic.start();

        SmartDashboard.putData("Auto choices", chooser);

        drivetrain.setPIDF(.00005, 0, 0, 0);
    }

    @Override
    public void robotPeriodic() {
        SmartDashboard.putData(pdp.getInstance());
        SmartDashboard.putNumber("JoystickForward", OI.driverPad.getY(GenericHID.Hand.kLeft));
//        drivetrain.pushData();
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

    @Override
    public void teleopInit(){
        jackStands.resetEncoders();
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

        Scheduler.getInstance().run();

//        jackStands.teleopControl();

//        if(OI.driverPad.getBumper(GenericHID.Hand.kRight)){
//            pcm.setOutput(IDs.lightPort, true);
//        }else{
//            pcm.setOutput(IDs.lightPort, false);
//        }

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
