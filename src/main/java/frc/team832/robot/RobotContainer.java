package frc.team832.robot;

import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.team832.lib.driverstation.controllers.Xbox360Controller;
import frc.team832.robot.commands.*;
import frc.team832.robot.subsystems.*;
import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;

import java.io.IOException;

public class RobotContainer {

    //Creates the drivePad object of XboxController class
    public static final Xbox360Controller drivePad = new Xbox360Controller(0);

    //Creates the stratComInterface of the StratComInterface Class
    public static final StratComInterface stratComInterface = new StratComInterface(1);
    //Creates the JoystickButton object

    public static final Drivetrain drivetrain = new Drivetrain();
    public static final Intake intake = new Intake();
    public static final Fourbar fourbar = new Fourbar();
    public static final Elevator elevator = new Elevator();
    public static final Jackstand jackstand = new Jackstand();
    public static final SuperStructure superStructure = new SuperStructure(fourbar, elevator, intake);

    public static boolean init() {
        boolean successful = true;

        if (!drivetrain.initialize()) {
            successful = false;
            System.out.println("Drivetrain INIT - FAIL");
        } else {
            System.out.println("Drivetrain INIT - OK");
        }

        if (!intake.initialize()) {
            successful = false;
            System.out.println("Intake INIT - FAIL");
        }

        if (!fourbar.initialize()) {
            successful = false;
            System.out.println("Fourbar INIT - FAIL");
        } else {
            System.out.println("Fourbar INIT - OK");
        }

        if (!elevator.initialize()) {
            successful = false;
            System.out.println("Elevator INIT - FAIL");
        } else {
            System.out.println("Elevator INIT - OK");
        }

        if (!superStructure.initialize(successful)) {
            successful = false;
            System.out.println("Superstructure INIT - FAIL");
        } else {
            System.out.println("Superstructure INIT - OK");
        }

        if (!jackstand.initialize()) {
            successful = false;
            System.out.println("Jackstand INIT - FAIL");
        }

        //Commands: drivePad
//        drivePad.yButton.whenPressed(new MoveJackstands(jackstand, Jackstand.JackstandPosition.RETRACTED));
//        drivePad.bButton.whenPressed(new MoveJackstands(jackstand, Jackstand.JackstandPosition.LVL3_UP));
//        drivePad.startButton.whenPressed(new MoveJackstands(jackstand, Jackstand.JackstandPosition.LVL2_UP));

        //Commands: stratComInterface
        stratComInterface.getArcadeBlackLeft().whenHeld(new StartEndCommand(() -> intake.runCargo(Intake.CargoDirection.UP), intake::stopCargo, intake));
        stratComInterface.getArcadeWhiteLeft().whenHeld(new StartEndCommand(() -> intake.runCargo(Intake.CargoDirection.DOWN), intake::stopCargo, intake));
        stratComInterface.getArcadeBlackRight().whenHeld(new StartEndCommand(() -> intake.runHatch(Intake.HatchDirection.IN), intake::stopHatch, intake));
        stratComInterface.getArcadeWhiteRight().whenHeld(new StartEndCommand(() -> intake.runHatch(Intake.HatchDirection.OUT), intake::stopHatch, intake));

//        stratComInterface.getSC1().whenPressed(new ConditionalCommand(new AutoMoveSuperStructure(SuperStructure.SuperStructurePosition.INTAKECARGO, superStructure, fourbar, elevator), new Storage(getThreeSwitch(), superStructure, fourbar, elevator), egg));

        var keySwitchCommand = new RunCommand(superStructure::moveManual, fourbar, elevator, superStructure);

        stratComInterface.getKeySwitch().whileActiveContinuous(keySwitchCommand);


        return successful;
    }

    public static ThreeSwitchPos getThreeSwitch () {
        if (stratComInterface.getDoubleToggleUp().get())
            return ThreeSwitchPos.UP;
        else if (stratComInterface.getDoubleToggleDown().get())
            return ThreeSwitchPos.DOWN;
        else
            return ThreeSwitchPos.OFF;
    }

    public enum ThreeSwitchPos{
        UP,
        DOWN,
        OFF
    }

}
