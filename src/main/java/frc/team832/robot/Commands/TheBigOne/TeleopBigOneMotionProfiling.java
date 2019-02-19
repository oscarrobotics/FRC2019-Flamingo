package frc.team832.robot.Commands.TheBigOne;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import edu.wpi.first.wpilibj.command.Command;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismMotionProfile;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Elevator;
import frc.team832.robot.Subsystems.Fourbar;
import frc.team832.robot.Subsystems.TheBigOne;

public class TeleopBigOneMotionProfiling extends Command {

    private OscarMechanismMotionProfile _elevatorTraj, _toptraj, _botTraj;

    public TeleopBigOneMotionProfiling(TheBigOne.Constants.MotionProfilePosition destination){
        requires(Robot.elevator);
        requires(Robot.fourbar);
        requires(Robot.theBigOne);

        System.out.println("Constructor");
        _elevatorTraj = new OscarMechanismMotionProfile(TheBigOne.currentPos.index(), destination.index(), "Elevator");
        //_toptraj = new OscarMechanismMotionProfile(TheBigOne.currentPos.index(), destination.index(), "Topfourbar");
        //_botTraj = new OscarMechanismMotionProfile(TheBigOne.currentPos.index(), destination.index(), "Bottomfourbar");
    }

    public void initialize(){
        System.out.println("Init mp big one");
        Robot.elevator.bufferTrajectory(_elevatorTraj);
        //Robot.fourbar.bufferTrajectories(_toptraj, _botTraj);
//        Robot.elevator.setMPControl(SetValueMotionProfile.Enable);
//        Robot.fourbar.setMPControl(SetValueMotionProfile.Enable);
        Robot.elevator.startMP();
        Robot.fourbar.startMP();
    }

    public void execute(){
        System.out.println("execute big one");

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
