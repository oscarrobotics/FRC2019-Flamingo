package frc.team832.robot;

import edu.wpi.first.wpilibj.GenericHID;

public class StratComInterface extends GenericHID {

	public StratComInterface(int port) {
		super(port);
	}

	public enum SCIButton {
		SC1(1),
		SC2(2),
		SC3(3),
		SC4(4),
		SC5(5),
		SC6(6),
		SCPlus(7),
		SCMinus(8),
		SCSideTop(9),
		SCSideMid(10),
		SCSideBot(11),
		ArcadeBlackLeft(12),
		ArcadeBlackRight(13),
		ArcadeWhiteLeft(14),
		ArcadeWhiteRight(15),
		SingleToggle(16),
		DoubleToggleUp(17),
		DoubleToggleDown(18),
		KeySwitch(19);

		private final int value;

		SCIButton(int value) {
			this.value = value;
		}

	}

	public boolean getKeySwitch() { return getRawButton(SCIButton.KeySwitch.value);}

	public boolean getKeySwitchDisabled() { return getRawButtonReleased(SCIButton.KeySwitch.value); }

	public boolean getKeySwitchEnabled() { return getRawButtonPressed(SCIButton.KeySwitch.value); }

	public double getXAxis(){
		return getRawAxis(0);
	}

	public double getYAxis(){
		return getRawAxis(1);
	}

	@Override
	public double getX(Hand hand) {
		return getX();
	}

	@Override
	public double getY(Hand hand) {
		return getY();
	}

}