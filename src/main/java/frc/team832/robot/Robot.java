package frc.team832.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
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
        fourbarTop.resetSensor();

        if (isComp) {
            if (!navX.init()) {
                System.out.println("NavX Failed to Init!!!");
            }
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
        if (pdp.isOnBus()) {
            SmartDashboard.putData(pdp.getInstance());
        }
        drivetrain.pushData();
        elevator.pushData();
        fourbar.pushData();
        jackStands.pushData();
        snowBlower.pushData();
    }

    @Override
    public void robotPeriodic() {
        pushData();
        update();
        //Logger.updateEntries();
    }

    @Override
    public void autonomousInit() {
        Scheduler.getInstance().enable();
        jackStands.resetEncoders();
        fourbarTop.resetSensor();
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
        if(!DriverStation.getInstance().isFMSAttached()){
            autonomousInit();
        }
        currentHatchState = AutoHatchState.None;
        interruptedHatchState = AutoHatchState.None;
    }

    @Override
    public void teleopPeriodic() {
		Scheduler.getInstance().run();
		double leftY = OI.driverPad.getY(GenericHID.Hand.kLeft);
		double rightX = -OI.driverPad.getX(GenericHID.Hand.kRight);
		double rotation;

		if (diffDrive.isQuickTurning()) {
			rotation = rightX * 0.5;
		} else {
			rotation = OscarMath.signumPow(rightX, 2);
		}

		if(OI.driverPad.getTriggerAxis(GenericHID.Hand.kLeft) > .5) {
			drivetrain.teleopControl(
                    leftY,
                    rotation,
                    Drivetrain.DriveMode.CURVATURE,
                    SmartDifferentialDrive.LoopMode.VELOCITY);
		} else {
			drivetrain.teleopControl(
                    leftY,
                    rotation,
                    Drivetrain.DriveMode.CURVATURE,
                    SmartDifferentialDrive.LoopMode.PERCENTAGE);
		}

		Color allianceColor = DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.Blue) ? Color.BLUE : Color.RED;

		if (DriverStation.getInstance().isOperatorControl()) {
			if (matchTimer.hasPeriodPassed(60)) {
				snowBlower.setLEDs(LEDMode.STATIC, Color.GREEN);
			} else {
				snowBlower.setLEDs(LEDMode.STATIC, allianceColor);
			}
		} else {
			snowBlower.setLEDs(LEDMode.STATIC, allianceColor);
		}

		jackStands.teleopControl();

    }

    @Override
    public void disabledPeriodic(){
    }

    @Override
    public void disabledInit() {
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().disable();
        fourbarTop.setNeutralMode(NeutralMode.Coast);
        jackStands.resetEncoders();
        Color forestGreen = new Color(24, 200, 0);
        snowBlower.setLEDs(LEDMode.STATIC, forestGreen);
    }

    public enum AutoHatchState{
        None,
        Grabbing,
        MovingElevator,
        Driving
    }
}
