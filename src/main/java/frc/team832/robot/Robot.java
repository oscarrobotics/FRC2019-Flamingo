package frc.team832.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.Motion.SmartDifferentialDrive;
import frc.team832.GrouchLib.Sensors.NavXMicro;
import frc.team832.GrouchLib.Util.OscarMath;
import frc.team832.robot.Commands.ZeroNavX;
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
    public static Vision vision;

    public static OI.ThreeSwitchPos ThreeSwitchPos = OI.ThreeSwitchPos.OFF;

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

        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);

        drivetrain = new Drivetrain(RobotMap.diffDrive);
        elevator = new Elevator(RobotMap.elevatorMech);
        fourbar = new Fourbar(RobotMap.fourbarTopMech);
        jackStands = new JackStands(RobotMap.frontJackStand, RobotMap.backJackStand, RobotMap.jackStandDrive);
        System.out.println("D, E, F, J INIT");

        snowBlower = new SnowBlower(RobotMap.cargoIntake, RobotMap.hatchHolder, RobotMap.canifier);
        complexLift = new ComplexLift(RobotMap.complexLiftMech);
        theBigOne = new TheBigOne(complexLift, snowBlower);
        System.out.println("S, C, T INIT");

        jackStands.resetEncoders();
        fourbarTop.resetSensor();

        vision = new Vision();


        if (isComp) {
            if (!navX.init()) {
                System.out.println("NavX Failed to Init!!!");
            }
        }

        OI.init();
        OI.setupCommands();
        SmartDashboard.putData(new ZeroNavX());

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
        ThreeSwitchPos = OI.getThreeSwitch();
        navX.pushData();
        vision.main();
        vision.pushData();
    }

    @Override
    public void autonomousInit() {
        Scheduler.getInstance().enable();
        jackStands.resetEncoders();
        fourbarTop.resetSensor();
        fourbar.setPosition(Fourbar.Constants.FourbarPosition.Bottom.getIndex());
        elevator.setPosition(Elevator.Constants.ElevatorPosition.Top.getIndex());
        jackStands.setPosition("Retract");
    }

    @Override
    public void autonomousPeriodic() {
        teleopPeriodic();
    }

    @Override
    public void teleopInit(){
        Scheduler.getInstance().enable();
        Color allianceColor = DriverStation.getInstance().getAlliance().equals(DriverStation.Alliance.Blue) ? Color.BLUE : Color.RED;
        if(!DriverStation.getInstance().isFMSAttached()){
            autonomousInit();
        }

        currentHatchState = AutoHatchState.None;
        interruptedHatchState = AutoHatchState.None;
    }

    @Override
    public void teleopPeriodic() {
		Scheduler.getInstance().run();
		jackStands.teleopControl();
        OI.runLED();
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
        snowBlower.setLEDs(LEDMode.STATIC, Color.GREEN);
        navX.zero();
    }

    public enum AutoHatchState{
        None,
        Grabbing,
        MovingElevator,
        Driving
    }
}
