package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.motorcontrol.CANTalon;
import frc.team832.GrouchLib.motorcontrol.CANVictor;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;
import frc.team832.GrouchLib.util.OscarMath;

public class Fourbar extends SubsystemBase
{
	private CANTalon fourbarTop, fourbarBottom;

	private Fourbar() {
		super();
		SmartDashboard.putData("Fourbar Subsys", this);
	}

	@Override
	public void periodic(){}

	public boolean initialize(){
		boolean successful = true;
		fourbarTop = new CANTalon(7);
		fourbarBottom = new CANTalon(8);

		if (!(fourbarTop.getInputVoltage() > 0)) successful = false;
		if (!(fourbarBottom.getInputVoltage() > 0)) successful = false;

		NeutralMode allIdleMode = NeutralMode.kBrake;
		fourbarTop.setNeutralMode(allIdleMode);
		fourbarBottom.setNeutralMode(allIdleMode);

		fourbarBottom.follow(fourbarTop);//does one need to be inverted?
		fourbarBottom.setInverted(true);


		return successful;
	}

	public void setPosition(double position){
		if (!(position > 0 || position < -5000))
			fourbarTop.setPosition(position);
	}

	public double posToDeg(double pos){
		//bottom = -75
		//top = 55
		return OscarMath.map(pos, 0, -5000,-75, 55);
	}
}
