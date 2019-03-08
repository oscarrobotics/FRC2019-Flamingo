package frc.team832.robot.Commands.HatchFunctions;

import edu.wpi.first.wpilibj.command.Command;
import frc.team832.robot.Robot;
import frc.team832.robot.Subsystems.Elevator;


public class AutoHatchElevator extends Command {

    String _index;
    double _pos;
    long startTime;

    public AutoHatchElevator(String index) {
        requires(Robot.elevator);
        _index = index;
        _pos = Elevator.Constants.Positions.getByIndex(index).getTarget();
    }

    public AutoHatchElevator(double pos) {
        requires(Robot.elevator);
        _pos = pos;
    }

    @Override
    protected void initialize() {
        startTime = System.currentTimeMillis();
        Robot.elevator.setPosition(_pos);
        Robot.currentHatchState = Robot.AutoHatchState.MovingElevator;
    }

    /**
     * The execute method is called repeatedly when this Command is
     * scheduled to run until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        //TODO: add buttons and positions
    }

    /**
     * <p>
     * Returns whether this command is finished. If it is, then the command will be removed and
     * {@link #end()} will be called.
     * </p><p>
     * It may be useful for a team to reference the {@link #isTimedOut()}
     * method for time-sensitive commands.
     * </p><p>
     * Returning false will result in the command never ending automatically. It may still be
     * cancelled manually or interrupted by another command. Returning true will result in the
     * command executing once and finishing immediately. It is recommended to use
     * {@link edu.wpi.first.wpilibj.command.InstantCommand} (added in 2017) for this.
     * </p>
     *
     * @return whether this command is finished.
     * @see Command#isTimedOut() isTimedOut()
     */
    @Override
    protected boolean isFinished() {
        return Robot.elevator.atTargetPosition() ||
                (Robot.interruptedHatchState != Robot.AutoHatchState.None && Robot.interruptedHatchState != Robot.AutoHatchState.MovingElevator);
    }


    /**
     * Called once when the command ended peacefully; that is it is called once
     * after {@link #isFinished()} returns true. This is where you may want to
     * wrap up loose ends, like shutting off a motor that was being used in the
     * command.
     */
    @Override
    protected void end() {
        if(Robot.interruptedHatchState == Robot.AutoHatchState.MovingElevator){
            Robot.interruptedHatchState = Robot.AutoHatchState.None;
        }
    }


    /**
     * <p>
     * Called when the command ends because somebody called {@link #cancel()} or
     * another command shared the same requirements as this one, and booted it out. For example,
     * it is called when another command which requires one or more of the same
     * subsystems is scheduled to run.
     * </p><p>
     * This is where you may want to wrap up loose ends, like shutting off a motor that was being
     * used in the command.
     * </p><p>
     * Generally, it is useful to simply call the {@link #end()} method within this
     * method, as done here.
     * </p>
     */
    @Override
    protected void interrupted() {
        super.interrupted();
    }
}
