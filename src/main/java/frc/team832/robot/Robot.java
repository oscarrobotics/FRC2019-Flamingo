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
import frc.team832.GrouchLib.Motion.SmartDifferentialDrive;
import frc.team832.GrouchLib.Sensors.CANifier;
import frc.team832.GrouchLib.Sensors.NavXMicro;
import frc.team832.robot.Subsystems.*;

import static com.ctre.phoenix.CANifier.PWMChannel.*;
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

    public double kP = .00025, kI = 0.0, kD = 0.0, kF = 0.0;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {

        if (!RobotMap.init()) {
            // Oh no!
//            System.out.println("Something went wrong during RobotMap.init()! Look above here for more");
            throw new RuntimeException("Something went wrong during RobotMap.init()! Look above here for more.");
        }

        drivetrain = new Drivetrain(RobotMap.diffDrive);
        elevator = new Elevator(RobotMap.elevatorMech);
        fourbar = new Fourbar(RobotMap.fourbarTopMech, RobotMap.fourbarBottomMech);
        jackStands = new JackStands(RobotMap.frontJackStand, RobotMap.backJackStand, RobotMap.jackStandDrive);
        System.out.println("D, E, F, J INIT");

        snowBlower = new SnowBlower(RobotMap.cargoIntake, RobotMap.hatchHolder, RobotMap.canifier, RobotMap.hatchGrabbor);

        complexLift = new ComplexLift(RobotMap.complexLiftMech);

        theBigOne = new TheBigOne(complexLift, snowBlower);
        System.out.println("S, C, T INIT");


        jackStands.resetEncoders();

        oi = new OI();
        System.out.println("OI INIT");

        SmartDashboard.putData("Auto choices", chooser);
        navX.init();
    }

    public void update() {
        snowBlower.update();
    }

    private void pushData() {
        SmartDashboard.putData(pdp.getInstance());
        SmartDashboard.putNumber("JoystickForward", OI.driverPad.getY(GenericHID.Hand.kLeft));
        SmartDashboard.putNumber("Fourbar error: ", fourbarTop.getClosedLoopError());
        drivetrain.pushData();
        elevator.pushData();
        fourbar.pushData();
        jackStands.pushData();
        snowBlower.pushData();
        SmartDashboard.putNumber("NavX data", navX.getYaw());
    }

    @Override
    public void robotPeriodic() {
        pushData();
        update();
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
        Scheduler.getInstance().enable();
        jackStands.resetEncoders();
        jackStands.setBackPosition(0);
        fourbar.setPosition(Fourbar.Constants.FourbarPosition.Middle.getIndex());
        elevator.setPosition(Elevator.Constants.ElevatorPosition.Middle.getIndex());
//        jackStands.setPosition("TEST1");
        snowBlower.setHatchHolderPosition(snowBlower.getHoldorCurrentPosition());
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        snowBlower.setHatchHolderPosition(snowBlower.getHoldorCurrentPosition());
        drivetrain.teleopControl(
                Math.pow(OI.driverPad.getTriggerAxis(GenericHID.Hand.kRight)- OI.driverPad.getTriggerAxis(GenericHID.Hand.kLeft), 1),
                Math.pow(-OI.driverPad.getX(GenericHID.Hand.kRight), 1),
                Drivetrain.DriveMode.CURVATURE,
                SmartDifferentialDrive.LoopMode.VELOCITY);

//        snowBlower.teleopControl();
        jackStands.teleopControl();


        if (OI.driverPad.getBumper(GenericHID.Hand.kRight)) {
            pcm.setOutput(IDs.PCM.lightPort, true);
        } else {
            pcm.setOutput(IDs.PCM.lightPort, false);
        }

        Scheduler.getInstance().run();
    }

    @Override
    public void disabledPeriodic(){
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().disable();
    }

    @Override
    public void disabledInit() {
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().disable();

        jackStands.resetEncoders();
    }
}
