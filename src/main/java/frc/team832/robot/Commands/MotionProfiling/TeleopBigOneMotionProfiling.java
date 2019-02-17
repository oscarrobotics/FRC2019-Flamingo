package frc.team832.robot.Commands.MotionProfiling;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import edu.wpi.first.wpilibj.command.Command;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismMotionProfile;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Elevator;
import frc.team832.robot.Subsystems.TheBigOne;
import jaci.pathfinder.Trajectory;

public class TeleopBigOneMotionProfiling extends Command {

    public Trajectory _elevatorTraj, _toptraj, _botTraj;

    private static final int min_points = 85; // minimum points to stream to talon before starting. TODO: Can this be lower?


    public TeleopBigOneMotionProfiling(TheBigOne.Constants.MotionProfilePosition destination){
        requires(Robot.elevator);
        requires(Robot.fourbar);
        requires(Robot.theBigOne);

        _elevatorTraj = new OscarMechanismMotionProfile(TheBigOne.currentPos, destination.index(), "Elevator").getTargetTrajectory();
        _toptraj = new OscarMechanismMotionProfile(TheBigOne.currentPos, destination.index(), "TopFourbar").getTargetTrajectory();
        _botTraj = new OscarMechanismMotionProfile(TheBigOne.currentPos, destination.index(), "BottomFourbar").getTargetTrajectory();
    }

    public void initialize(){
        Robot.elevator.startFillingTrajectory(_elevatorTraj);
        Robot.fourbar.startFillingTopTrajectory(_toptraj);
        Robot.fourbar.startFillingBotTrajectory(_botTraj);

        while (Robot.fourbar.getBotMpStatus().btmBufferCnt < min_points || Robot.fourbar.getTopMpStatus().btmBufferCnt < min_points || Robot.elevator.getMPStatus().btmBufferCnt < min_points) {
            Robot.fourbar.periodic();
            Robot.elevator.periodic();
        }
    }

    public void execute(){
        Robot.elevator.setMPControl(SetValueMotionProfile.Enable);
        Robot.fourbar.setMPControl(SetValueMotionProfile.Enable);
    }

    @Override
    protected void end() {
        Robot.elevator.setMPControl(SetValueMotionProfile.Disable);
        Robot.fourbar.setMPControl(SetValueMotionProfile.Disable);
        Robot.elevator.setPosition(Robot.elevator.getCurrentPosition());
        Robot.fourbar.setPosition(Robot.fourbar.getTopCurrentPosition());
    }


    @Override
    protected boolean isFinished() {
        return false;
    }
}
