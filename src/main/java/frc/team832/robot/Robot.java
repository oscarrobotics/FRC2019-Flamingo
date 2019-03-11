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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Motion.SmartDifferentialDrive;
import frc.team832.GrouchLib.Util.OscarMath;
import frc.team832.robot.Subsystems.*;
import frc.team832.robot.Subsystems.SnowBlower.LEDMode;
import io.github.oblarg.oblog.Logger;

import java.awt.*;

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

    public static Drivetrain drivetrain;
    public static Elevator elevator;
    public static Fourbar fourbar;
    public static ComplexLift complexLift;
    public static SnowBlower snowBlower;
    public static TheBigOne theBigOne;
    public static JackStands jackStands;

    public static AutoHatchState currentHatchState = AutoHatchState.None;
    public static AutoHatchState interruptedHatchState = AutoHatchState.None;

    private Timer matchTimer = new Timer();
    private float rainbowNum = 0;

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

        if(isComp||!isComp){
            fourbarTop.resetSensor();
            navX.init();
        }

        OI.init();
        OI.setupCommands();

        System.out.println("OI INIT");

        Logger.configureLoggingAndConfig(this, false);
    }

    private void update() {
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
        SmartDashboard.putNumber("NavX Yaw", navX.getYaw());
    }

    @Override
    public void robotPeriodic() {
        pushData();
        update();
        Logger.updateEntries();
    }

    @Override
    public void autonomousInit() {
        Scheduler.getInstance().enable();
        jackStands.resetEncoders();
//        jackStands.setPosition("Top");
        fourbar.setPosition(Fourbar.Constants.FourbarPosition.Bottom.getIndex());
        elevator.setPosition(Elevator.Constants.ElevatorPosition.Top.getIndex());
    }

    @Override
    public void autonomousPeriodic() {
        teleopPeriodic();
    }

    @Override
    public void teleopInit(){
        Scheduler.getInstance().enable();
        //if(!DriverStation.getInstance().isFMSAttached()){
            autonomousInit();
        //}
        currentHatchState = AutoHatchState.None;
        interruptedHatchState = AutoHatchState.None;
    }

    @Override
    public void teleopPeriodic() {
        double leftY = OI.driverPad.getY(GenericHID.Hand.kLeft);
        double rightX = -OI.driverPad.getX(GenericHID.Hand.kRight);

        double rotation = OscarMath.signumPow(rightX, 2);

        System.out.println("Operator Y: " + OI.operatorBox.getY());

//        if (matchTimer.hasPeriodPassed(60)) {
//            snowBlower.setLEDs(LEDMode.STATIC, Color.GREEN);
//        } else {
//            Color allianceColor = DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.Blue) ? Color.BLUE : Color.RED;
//            snowBlower.setLEDs(LEDMode.CUSTOM_BREATHE, allianceColor);
//        }
        drivetrain.teleopControl(
                leftY,
                rotation,
                Drivetrain.DriveMode.CURVATURE,
                SmartDifferentialDrive.LoopMode.VELOCITY);


//        snowBlower.teleopControl();
        jackStands.teleopControl();

//        if(OI.op2.get()){
//            fourbar.testAdjustment(150);
//
//        }
//
//        if(OI.operatorBox.getRawButtonReleased(5)){
//            fourbar.testAdjustment(-150);
//        }
//
//        if(OI.operatorBox.getRawButtonReleased(3)){
//            elevator.testAdjustment(-25);
//        }
//
//        if(OI.operatorBox.getRawButtonReleased(6)){
//            elevator.testAdjustment(25);
//        }

        Scheduler.getInstance().run();
    }

    @Override
    public void disabledPeriodic(){
    }

    @Override
    public void disabledInit() {
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().disable();
        jackStands.resetEncoders();
        snowBlower.setLEDs(LEDMode.CUSTOM_FLASH, Color.GREEN);
    }

    public enum AutoHatchState{
        None,
        Grabbing,
        MovingElevator,
        Driving
    }
}
