package frc.team832.robot;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.team832.lib.control.PDP;
import frc.team832.lib.driverstation.controllers.POV;
import frc.team832.lib.driverstation.controllers.StratComInterface;
import frc.team832.lib.driverstation.controllers.StratComInterface.ThreeSwitchPos;
import frc.team832.lib.driverstation.controllers.Xbox360Controller;
import frc.team832.robot.commands.MoveJackstands;
import frc.team832.robot.commands.MoveSingleJackstand;
import frc.team832.robot.commands.Storage;
import frc.team832.robot.commands.automaticDriving.DriveToVisionTarget;
import frc.team832.robot.subsystems.*;
import frc.team832.robot.subsystems.Intake.CargoDirection;
import frc.team832.robot.subsystems.Intake.HatchDirection;
import frc.team832.robot.subsystems.Jackstand.BackJackPosition;
import frc.team832.robot.subsystems.Jackstand.FrontJackPosition;
import frc.team832.robot.subsystems.Jackstand.JackstandPosition;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@SuppressWarnings("WeakerAccess")
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
    public static final SuperStructure superStructure = new SuperStructure(fourbar, elevator);
    public static final Vision vision = new Vision();
    public static final PDP pdp = new PDP(0);

    public static boolean init() {
        boolean successful = true;

        if (!LEDs.initialize()) {
            System.out.println("LEDs INIT - FAIL");
        } else {
            System.out.println("LEDs INIT - OK");
            LEDs.setLEDs(LEDs.LEDMode.OFF);
        }

        if (!drivetrain.initialize()) {
            successful = false;
            System.out.println("Drivetrain INIT - FAIL");
        } else {
            System.out.println("Drivetrain INIT - OK");
        }

        Paths.initializePaths(drivetrain.driveKinematics, Constants.DRIVE_PATH_MAX_VELOCITY_METERS_PER_SEC, Constants.DRIVE_PATH_MAX_ACCELERATION_METERS_PER_SEC_SQ);

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

        if (!vision.init()) {
            successful = false;
            System.out.println("Vision INIT - FAIL");
        }

        //Commands: drivePad
        drivePad.backButton.whenPressed(new MoveJackstands(JackstandPosition.LVL2_UP, jackstand));
        drivePad.startButton.whenPressed(new MoveJackstands(JackstandPosition.LVL3_UP, jackstand));
        drivePad.bButton.whenPressed(new MoveSingleJackstand(FrontJackPosition.RETRACTED, jackstand));
        drivePad.yButton.whenPressed(new MoveSingleJackstand(BackJackPosition.RETRACTED, jackstand));
        drivePad.pov.getPOVButton(POV.Position.Up).whenHeld(new StartEndCommand(() -> jackstand.setDrivePower(0.45), jackstand::stopDrive, jackstand));
        drivePad.pov.getPOVButton(POV.Position.Down).whenHeld(new StartEndCommand(() -> jackstand.setDrivePower(-0.45), jackstand::stopDrive, jackstand));
        drivePad.aButton.whileHeld(new DriveToVisionTarget(drivetrain, vision));


        //Commands: stratComInterface
        stratComInterface.getArcadeBlackLeft().whenHeld(new StartEndCommand(() -> intake.runCargo(CargoDirection.UP), intake::stopCargo, intake));
        stratComInterface.getArcadeWhiteLeft().whenHeld(new StartEndCommand(() -> intake.runCargo(CargoDirection.DOWN), intake::stopCargo, intake));
        stratComInterface.getArcadeBlackRight().whenHeld(new StartEndCommand(() -> intake.runHatch(HatchDirection.IN), intake::stopHatch, intake));
        stratComInterface.getArcadeWhiteRight().whenHeld(new StartEndCommand(() -> intake.runHatch(HatchDirection.OUT), intake::stopHatch, intake));

        stratComInterface.getDoubleToggleUp().whileHeld(new Storage(ThreeSwitchPos.SWITCH_UP, superStructure, fourbar, elevator));
        stratComInterface.getDoubleToggleDown().whileHeld(new Storage(ThreeSwitchPos.SWITCH_DOWN, superStructure, fourbar, elevator));

        stratComInterface.getSC1().whenPressed(new ConditionalCommand (new DoNothing(), new AutoMoveSuperStructure(SuperStructure.SuperStructurePosition.ROCKETHATCH_HIGH, superStructure, fourbar, elevator), () -> stratComInterface.getKeySwitch().get()));
        stratComInterface.getSC1().whenPressed(new ConditionalCommand (new DoNothing(), new AutoMoveSuperStructure(SuperStructure.SuperStructurePosition.ROCKETHATCH_HIGH, superStructure, fourbar, elevator), () -> stratComInterface.getKeySwitch().get()));
        stratComInterface.getSC1().whenPressed(new ConditionalCommand (new DoNothing(), new AutoMoveSuperStructure(SuperStructure.SuperStructurePosition.ROCKETHATCH_HIGH, superStructure, fourbar, elevator), () -> stratComInterface.getKeySwitch().get()));
        stratComInterface.getSC1().whenPressed(new ConditionalCommand (new DoNothing(), new AutoMoveSuperStructure(SuperStructure.SuperStructurePosition.ROCKETHATCH_HIGH, superStructure, fourbar, elevator), () -> stratComInterface.getKeySwitch().get()));
        stratComInterface.getSC1().whenPressed(new ConditionalCommand (new DoNothing(), new AutoMoveSuperStructure(SuperStructure.SuperStructurePosition.ROCKETHATCH_HIGH, superStructure, fourbar, elevator), () -> stratComInterface.getKeySwitch().get()));
        stratComInterface.getSC1().whenPressed(new ConditionalCommand (new DoNothing(), new AutoMoveSuperStructure(SuperStructure.SuperStructurePosition.ROCKETHATCH_HIGH, superStructure, fourbar, elevator), () -> stratComInterface.getKeySwitch().get()));


        var keySwitchCommand = new RunCommand(superStructure::moveManual, fourbar, elevator, superStructure);

        stratComInterface.getKeySwitch().whileActiveContinuous(keySwitchCommand);

//        drivePad.aButton.whenPressed(new AutonomousHatchScore(Paths.RightHab_RightFrontRocket, SuperStructure.SuperStructurePosition.CARGOSHIP_HATCH, drivetrain, superStructure, elevator, fourbar, intake));
//        drivePad.aButton.whenPressed(new RamseteCommand(Paths.Test_Three_Meters_Forward, drivetrain::getLatestPose2d, new RamseteController(2, 0.7), drivetrain.driveKinematics, drivetrain::consumeWheelSpeeds, drivetrain));

        vision.setLight(false);
        return successful;

    }
}
