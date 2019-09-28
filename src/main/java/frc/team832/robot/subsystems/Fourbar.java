package frc.team832.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.GrouchLib.motorcontrol.CANTalon;
import frc.team832.GrouchLib.motorcontrol.CANVictor;
import frc.team832.GrouchLib.motorcontrol.NeutralMode;
import frc.team832.GrouchLib.util.OscarMath;
import frc.team832.robot.Constants;

public class Fourbar extends SubsystemBase
{
	private CANTalon fourbarTop, fourbarBottom;

	public Fourbar() {
		super();
		SmartDashboard.putData("Fourbar Subsys", this);
	}

	@Override
	public void periodic(){}

	public boolean initialize(){
		boolean successful = true;
		fourbarTop = new CANTalon(Constants.FOURBARTOP_CAN_ID);
		fourbarBottom = new CANTalon(Constants.FOURBARBOTTOM_CAN_ID);

		if (!(fourbarTop.getInputVoltage() > 0)) successful = false;
		if (!(fourbarBottom.getInputVoltage() > 0)) successful = false;

		NeutralMode allIdleMode = NeutralMode.kBrake;
		fourbarTop.setNeutralMode(allIdleMode);
		fourbarBottom.setNeutralMode(allIdleMode);

		fourbarBottom.follow(fourbarTop);
		fourbarBottom.setInverted(true);//does one need to be inverted?

		//setDefaultCommand(new)

		return successful;
	}

	public double posToDeg(double pos){
		//bottom = -75
		//top = 55
		return OscarMath.map(pos, FourbarPosition.BOTTOM.value, FourbarPosition.TOP.value,-75, 55);
	}

	public static enum FourbarPosition{
		BOTTOM(0),
		MIDDLE(-2500),
		TOP(-5000);

		public final int value;

		FourbarPosition(int value){
			this.value = value;
		}
	}
}
