package frc.team832.robot;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.team832.GrouchLib.driverstation.controllers.Xbox360Controller;
import frc.team832.robot.commands.*;
import frc.team832.robot.subsystems.*;

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

        if (!jackstand.initialize()) {
            successful = false;
            System.out.println("Jackstand INIT - FAIL");
        }

        //Commands: drivePad
        drivePad.yButton.whenPressed(new MoveJackstands(jackstand, Jackstand.JackstandPosition.RETRACTED));
        drivePad.bButton.whenPressed(new MoveJackstands(jackstand, Jackstand.JackstandPosition.LVL3_UP));
        drivePad.startButton.whenPressed(new MoveJackstands(jackstand, Jackstand.JackstandPosition.LVL2_UP));

        //Commands: stratComInterface
        stratComInterface.getArcadeBlackRight().whenHeld(new CargoUp(intake));
        stratComInterface.getArcadeBlackRight().whenHeld(new HatchIn(intake));
        stratComInterface.getArcadeWhiteLeft().whenHeld(new CargoDown(intake));
        stratComInterface.getArcadeWhiteRight().whenHeld(new HatchOut(intake));

//        stratComInterface.getSC1().and(stratComInterface.getKeySwitch().negate()).whenActive(new AutoMoveFourbar(fourbar, Fourbar.FourbarPosition.MIDDLE));

//        stratComInterface.getSC3().and(stratComInterface.getKeySwitch().negate()).whenActive(new AutoMoveSuperStructure(superStructure, fourbar, elevator, SuperStructure.SuperStructurePosition.ROCKETHATCH_LOW));

        stratComInterface.getKeySwitch()
                .whenHeld(new ManualMoveElevator(elevator))
                .whenHeld(new ManualMoveFourbar(fourbar));

        return successful;
    }
}
