/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team832.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Motion.SmartDifferentialDrive;
import frc.team832.GrouchLib.Sensors.CANifier;
import frc.team832.GrouchLib.Sensors.NavXMicro;
import frc.team832.GrouchLib.Util.OscarMath;
import frc.team832.robot.Subsystems.*;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;
import org.opencv.core.Mat;

import java.awt.*;

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
    private static int timer = 0;
    private float rainbowNum = 0;
    public static boolean isHolding = false;

    CANifier.Ultrasonic heightUltrasonic;

    public double kP = .00025, kI = 0.0, kD = 0.0, kF = 0.0;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {

        if (!RobotMap.init()) {
//            System.out.println("Something went wrong during RobotMap.init()! Look above here for more");
            throw new RuntimeException("Something went wrong during RobotMap.init()! Look above here for more.");
        }

        drivetrain = new Drivetrain(RobotMap.diffDrive);
        elevator = new Elevator(RobotMap.elevatorMech);
        fourbar = new Fourbar(RobotMap.fourbarTopMech);
        jackStands = new JackStands(RobotMap.frontJackStand, RobotMap.backJackStand, RobotMap.jackStandDrive);
        System.out.println("D, E, F, J INIT");

        snowBlower = new SnowBlower(RobotMap.cargoIntake, RobotMap.hatchHolder, RobotMap.canifier, RobotMap.hatchGrabbor);

        complexLift = new ComplexLift(RobotMap.complexLiftMech);

        theBigOne = new TheBigOne(complexLift, snowBlower);
        System.out.println("S, C, T INIT");


        jackStands.resetEncoders();

        if(isComp){
            fourbarTop.resetSensor();
        }

        oi = new OI();
        System.out.println("OI INIT");

//        SmartDashboard.putData("Auto choices", chooser);
        //navX.init();
        Logger.configureLoggingAndConfig(this, false);
        navX.init();

        heightUltrasonic = canifier.getUltrasonic(PWMChannel0, PWMChannel1);
    }

    public void update() {
//        snowBlower.update();
    }

    private void pushData() {
        SmartDashboard.putData(pdp.getInstance());
        SmartDashboard.putNumber("JoystickForward", OI.driverPad.getY(GenericHID.Hand.kLeft));
        SmartDashboard.putNumber("Fourbar error: ", fourbarTop.getClosedLoopError());
        drivetrain.pushData();
        elevator.pushData();
        fourbar.pushData();
        jackStands.pushData();
//        snowBlower.pushData();
        SmartDashboard.putNumber("NavX Yaw", navX.getYaw());

        heightUltrasonic.update();
        SmartDashboard.putNumber("BallDist", heightUltrasonic.getRangeInches());
    }

    @Override
    public void robotPeriodic() {
//        pushData();
//        update();
//        Logger.updateEntries();
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
        //autoSelected = chooser.getSelected();
        // autoSelected = SmartDashboard.getString("Auto Selector",
        // defaultAuto);
        Scheduler.getInstance().enable();
        jackStands.resetEncoders();
        fourbar.setPosition(Fourbar.Constants.FourbarPosition.Bottom.getIndex());
        elevator.setPosition(Elevator.Constants.ElevatorPosition.Top.getIndex());
        System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
//        switch (autoSelected) {
//            case CUSTOM_AUTO:
//
//                break;
//            case DEFAULT_AUTO:
//            default:
//                // Put default auto code here
//                break;
//        }
        teleopPeriodic();
    }

    @Override
    public void teleopInit(){
        Scheduler.getInstance().enable();
        if(!DriverStation.getInstance().isFMSAttached()){
            autonomousInit();
        }

//        jackStands.setPosition("TEST1");
//        snowBlower.setHatchHolderPosition(snowBlower.getHoldorCurrentPosition());
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        timer++; // TODO: FIXXX!!! use the WPILib Timer object
//        snowBlower.setHatchHolderPosition(snowBlower.getHoldorCurrentPosition());
        double triggerThrottle = OI.driverPad.getTriggerAxis(GenericHID.Hand.kRight)- OI.driverPad.getTriggerAxis(GenericHID.Hand.kLeft);
        double leftY = OI.driverPad.getY(GenericHID.Hand.kLeft);
        double rightX = -OI.driverPad.getX(GenericHID.Hand.kRight);

        double yPower = leftY;
        double rotation = OscarMath.signumPow(rightX, 2);

        if(timer >= 4500){
            snowBlower.setLEDs(SnowBlower.LEDMode.STATIC, Color.GREEN);
        }else {
            snowBlower.setLEDs(SnowBlower.LEDMode.STATIC, Color.BLUE);
//            if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue) {
//                snowBlower.setLED(Color.BLUE);
//            } else {
//                snowBlower.setLED(Color.RED);
//            }
        }
        drivetrain.teleopControl(
                leftY,
                rotation,
                Drivetrain.DriveMode.CURVATURE,
                SmartDifferentialDrive.LoopMode.PERCENTAGE);

//        snowBlower.teleopControl();
        jackStands.teleopControl();

        if(oi.operatorBox.getRawButtonReleased(2)){
            fourbar.testAdjustment(200);

        }

        if(OI.operatorBox.getRawButtonReleased(5)){
            fourbar.testAdjustment(-200);
        }

        if(OI.operatorBox.getRawButtonReleased(3)){
            elevator.testAdjustment(-25);
        }

        if(OI.operatorBox.getRawButtonReleased(6)){
            elevator.testAdjustment(25);
        }

       /* if(OI.driverPad.getAButton()){
            //jackStands.setFrontArbFFPos(jackStands.getFrontCorrectionPower(),"Bottom");
            jackStands.setBackPosition("Bottom");
            jackStands.setFrontPosition("Bottom");
            isHolding = false;
        } else if(OI.driverPad.getXButton()) {
            jackStands.setBackPosition("Bottom");
            isHolding = false;
        } else if(OI.driverPad.getBButton()){
            jackStands.setFrontPosition("Bottom");
            isHolding = false;
        } else if(OI.driverPad.getYButton()){
            jackStands.setBackPosition("Top");
            jackStands.setFrontPosition("Top");
            //jackStands.setFrontArbFFPos(jackStands.getFrontCorrectionPower(), "Top");
            isHolding = false;
        } else if(OI.driverPad.getBumper(GenericHID.Hand.kLeft)){
            jackStands.setBackPosition("Top");
            isHolding = false;
        } else if(OI.driverPad.getBumper(GenericHID.Hand.kRight)){
            jackStands.setFrontPosition("Top");
            isHolding = false;
        } else {
            if (!isHolding){
                jackStands.setBackPosition(jackStands.getBackCurrentPosition());
                jackStands.setFrontPosition(jackStands.getFrontCurrentPosition());
                isHolding = true;
            }

        } */

        Scheduler.getInstance().run();
    }

    @Override
    public void disabledPeriodic(){
        rainbowNum += 0.002f;
        snowBlower.sendHSB(rainbowNum, 1.0f, 0.5f);
    }

    @Override
    public void disabledInit() {
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().disable();
        jackStands.resetEncoders();

    }
}
