package frc.team832.robot.Commands.TheBigOne;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import edu.wpi.first.wpilibj.command.Command;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismMotionProfile;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Elevator;
import frc.team832.robot.Subsystems.Fourbar;
import frc.team832.robot.Subsystems.TheBigOne;
import frc.team832.robot.Subsystems.TheBigOne.Constants;

public class TeleopBigOneMotionProfiling extends Command {

    private MechanismMotionProfile _elevatorTraj, _toptraj, _botTraj;
    private Constants.MotionProfilePosition _destination;

    public TeleopBigOneMotionProfiling(Constants.MotionProfilePosition destination){
        requires(Robot.elevator);
        requires(Robot.fourbar);
        requires(Robot.theBigOne);

        _elevatorTraj = new MechanismMotionProfile(TheBigOne.currentPos.index(), destination.index(), "Elevator");
        _toptraj = new MechanismMotionProfile(TheBigOne.currentPos.index(), destination.index(), "Topfourbar");
        _botTraj = new MechanismMotionProfile(TheBigOne.currentPos.index(), destination.index(), "Bottomfourbar");
        _destination = destination;

        Robot.elevator.bufferTrajectory(_elevatorTraj);
        Robot.fourbar.bufferTrajectories(_toptraj, _botTraj);

    }

    public void initialize(){
        System.out.println("Init mp big one");

        Robot.elevator.bufferAndSendMP();
        Robot.fourbar.bufferAndSendMP();
//        Robot.elevator.setMPControl(SetValueMotionProfile.Enable);
//        Robot.fourbar.setMPControl(SetValueMotionProfile.Enable);
    }

    public void execute(){
        System.out.println("Elevator MP status: " + Robot.elevator.getMPStatus());

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
        return Robot.elevator.isMPFinished() && Robot.fourbar.isMPFinished();
    }
}