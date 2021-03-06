/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team832.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.team832.lib.driverstation.dashboard.DashboardManager;
import frc.team832.lib.motorcontrol.NeutralMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	public RobotState curState = RobotState.DISABLED, lastState = RobotState.DISABLED, thirdState = RobotState.DISABLED;

	@Override
	public void robotInit () {
		System.out.println("INIT - Robot - BEGIN");

		if (!RobotContainer.init()) {
			System.out.println("INIT - RobotContainer - FAIL");
		} else {
			System.out.println("INIT - RobotContainer - OK");
		}
	}

	private void enabledInit() {

		if(!(curState == RobotState.TELEOP && lastState == RobotState.DISABLED && thirdState == RobotState.AUTONOMOUS)){
		RobotContainer.fourbar.zeroEncoder();
		RobotContainer.fourbar.setIdleMode(NeutralMode.kBrake);
		RobotContainer.elevator.setIdleMode(NeutralMode.kBrake);
		RobotContainer.drivetrain.setIdleMode(NeutralMode.kBrake);
		RobotContainer.drivetrain.resetGyro();
		RobotContainer.drivetrain.resetEncoders();
		LEDs.setLEDs(LEDs.LEDMode.DEFAULT);
		}
	}

	/**
	 * This function is called every robot packet, no matter the mode. Use
	 * this for items like diagnostics that you want ran during disabled,
	 * autonomous, teleoperated and test.
	 *
	 * <p>This runs after the mode specific periodic functions, but before
	 * LiveWindow and SmartDashboard integrated updating.
	 */
	@Override
	public void robotPeriodic () {
		CommandScheduler.getInstance().run();
		DashboardManager.updateAllTabs();
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
	public void autonomousInit () {
		thirdState = lastState;
		lastState = curState;
		curState = RobotState.AUTONOMOUS;

		enabledInit();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic () {
	}

	@Override
	public void teleopInit () {
		thirdState = lastState;
		lastState = curState;
		curState = RobotState.AUTONOMOUS;

		if (DriverStation.getInstance().isDSAttached()) { // human enable
			enabledInit();
		} else if (DriverStation.getInstance().isFMSAttached()) { // field enable
			System.out.println("FMS ON");
		}
	}

	@Override
	public void disabledInit() {
		thirdState = lastState;
		lastState = curState;
		curState = RobotState.DISABLED;

		RobotContainer.fourbar.setIdleMode(NeutralMode.kCoast);
		RobotContainer.drivetrain.setIdleMode(NeutralMode.kCoast);
		RobotContainer.elevator.setIdleMode(NeutralMode.kCoast);
		LEDs.setLEDs(LEDs.LEDMode.OFF);
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic () {
		CommandScheduler.getInstance().run();
	}
}
