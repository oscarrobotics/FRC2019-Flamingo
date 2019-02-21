package frc.team832.robot.Commands.MotionProfiling;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import edu.wpi.first.wpilibj.command.Command;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismMotionProfile;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Elevator;
import frc.team832.robot.Subsystems.Fourbar;
import frc.team832.robot.Subsystems.TheBigOne;

public class TeleopBigOneMotionProfiling extends Command {

    private MechanismMotionProfile _elevatorTraj, _toptraj, _botTraj;

    public TeleopBigOneMotionProfiling(TheBigOne.Constants.MotionProfilePosition destination){
        requires(Robot.elevator);
        requires(Robot.fourbar);
        requires(Robot.theBigOne);

        _elevatorTraj = new MechanismMotionProfile(TheBigOne.currentPos, destination.index(), "Elevator");
        _toptraj = new MechanismMotionProfile(TheBigOne.currentPos, destination.index(), "TopFourbar");
        _botTraj = new MechanismMotionProfile(TheBigOne.currentPos, destination.index(), "BottomFourbar");
    }

    public void initialize(){
        Robot.elevator.bufferTrajectory(_elevatorTraj);
        Robot.fourbar.bufferTrajectories(_toptraj, _botTraj);
    }

    public void execute(){
        Robot.elevator.setMPControl(SetValueMotionProfile.Enable);
        Robot.fourbar.setMPControl(SetValueMotionProfile.Enable);
    }

    @Override
    protected void end() {
        Robot.elevator.setMPControl(SetValueMotionProfile.Disable);
        Robot.fourbar.setMPControl(SetValueMotionProfile.Disable);
        Robot.elevator.setPosition(_elevatorTraj.finalPosition(Elevator.Constants.Positions));
        Robot.fourbar.setPosition(_toptraj.finalPosition(Fourbar.Constants.Positions));
    }


    @Override
    protected boolean isFinished() {
        return false;
    }
}
