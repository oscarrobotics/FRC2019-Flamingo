package frc.team832.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class StratComInterface extends GenericHID {

	//Constructor
	public StratComInterface(int port) {
		super(port);
	}

	//Represent buttons on StratComInterface
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

	//Gets current state of key switch
	public boolean getKeySwitch() { return getRawButton(SCIButton.KeySwitch.value);}
	public boolean getKeySwitchDisabled() { return getRawButtonReleased(SCIButton.KeySwitch.value); }
	public boolean getKeySwitchEnabled() { return getRawButtonPressed(SCIButton.KeySwitch.value); }

	//Gets current states for each of the main SCI buttons (the ones with the numbers)
	public boolean getSC1() { return getRawButton(SCIButton.SC1.value);}
		public boolean getSC1Pressed() { return getRawButtonPressed(SCIButton.SC1.value);}
		public boolean getSC1Released() { return getRawButtonReleased(SCIButton.SC1.value);}
	public boolean getSC2() { return getRawButton(SCIButton.SC2.value);}
		public boolean getSC2Pressed() { return getRawButtonPressed(SCIButton.SC2.value);}
		public boolean getSC2Released() { return getRawButtonReleased(SCIButton.SC2.value);}
	public boolean getSC3() { return getRawButton(SCIButton.SC3.value);}
		public boolean getSC3Pressed() { return getRawButtonPressed(SCIButton.SC3.value);}
		public boolean getSC3Released() { return getRawButtonReleased(SCIButton.SC3.value);}
	public boolean getSC4() { return getRawButton(SCIButton.SC4.value);}
		public boolean getSC4Pressed() { return getRawButtonPressed(SCIButton.SC4.value);}
		public boolean getSC4Released() { return getRawButtonReleased(SCIButton.SC4.value);}
	public boolean getSC5() { return getRawButton(SCIButton.SC5.value);}
		public boolean getSC5Pressed() { return getRawButtonPressed(SCIButton.SC5.value);}
		public boolean getSC5Released() { return getRawButtonReleased(SCIButton.SC5.value);}
	public boolean getSC6() { return getRawButton(SCIButton.SC6.value);}
		public boolean getSC6Pressed() { return getRawButtonPressed(SCIButton.SC6.value);}
		public boolean getSC6Released() { return getRawButtonReleased(SCIButton.SC6.value);}

	//Gets current state of plus and minus buttons
	public boolean getSCPlus() { return getRawButton(SCIButton.SCPlus.value);}
		public boolean getSCPlusPressed() { return getRawButtonPressed(SCIButton.SCPlus.value);}
		public boolean getSCPlusReleased() { return getRawButtonReleased(SCIButton.SCPlus.value);}
	public boolean getSCMinus() { return getRawButton(SCIButton.SCMinus.value);}
		public boolean getSCMinusPressed() { return getRawButtonPressed(SCIButton.SCPlus.value);}
		public boolean getSCMinusReleased() { return getRawButtonReleased(SCIButton.SCPlus.value);}

	//Gets current state of SCI side buttons
	public boolean getSCSideTop() { return getRawButton(SCIButton.SCSideTop.value);}
		public boolean getSCSideTopPressed() { return getRawButtonPressed(SCIButton.SCSideTop.value);}
		public boolean getSCSideTopReleased() { return getRawButtonReleased(SCIButton.SCSideTop.value);}
	public boolean getSCSideMid() { return getRawButton(SCIButton.SCSideMid.value);}
		public boolean getSCSideMidPressed() { return getRawButtonPressed(SCIButton.SCSideMid.value);}
		public boolean getSCSideMidReleased() { return getRawButtonReleased(SCIButton.SCSideMid.value);}
	public boolean getSCSideBot() { return getRawButton(SCIButton.SCSideBot.value);}
		public boolean getSCSideBotPressed() { return getRawButtonPressed(SCIButton.SCSideBot.value);}
		public boolean getSCSideBotReleased() { return getRawButtonReleased(SCIButton.SCSideBot.value);}

	//Gets current state of the 4 arcade buttons
	public boolean getArcadeBlackLeft() { return getRawButton(SCIButton.ArcadeBlackLeft.value);}
		public boolean getArcadeBlackLeftPressed() { return getRawButtonPressed(SCIButton.ArcadeBlackLeft.value);}
		public boolean getArcadeBlackLeftReleased() { return getRawButtonReleased(SCIButton.ArcadeBlackLeft.value);}
	public boolean getArcadeBlackRight() { return getRawButton(SCIButton.ArcadeBlackRight.value);}
		public boolean getArcadeBlackRightPressed() { return getRawButtonPressed(SCIButton.ArcadeBlackRight.value);}
		public boolean getArcadeBlackRightReleased() { return getRawButtonReleased(SCIButton.ArcadeBlackRight.value);}
	public boolean getArcadeWhiteLeft() { return getRawButton(SCIButton.ArcadeWhiteLeft.value);}
		public boolean getArcadeWhiteLeftPressed() { return getRawButtonPressed(SCIButton.ArcadeWhiteLeft.value);}
		public boolean getArcadeWhiteLeftReleased() { return getRawButtonReleased(SCIButton.ArcadeWhiteLeft.value);}
	public boolean getArcadeWhiteRight() { return getRawButton(SCIButton.ArcadeWhiteRight.value);}
		public boolean getArcadeWhiteRightPressed() { return getRawButtonPressed(SCIButton.ArcadeWhiteRight.value);}
		public boolean getArcadeWhiteRightReleased() { return getRawButtonReleased(SCIButton.ArcadeWhiteRight.value);}

	//Gets state of single toggle
	public boolean getSingleToggle() { return getRawButton(SCIButton.SingleToggle.value);}

	//Gets state of double toggle
	public boolean getDoubleToggleUp() { return getRawButton(SCIButton.DoubleToggleUp.value);}
	public boolean getDoubleToggleDown() { return getRawButton(SCIButton.DoubleToggleDown.value);}

	//Gets state of left slider
	@Override
	public double getX(Hand hand) { return getX();}
	//Gets state of right slider
	@Override
	public double getY(Hand hand) { return getY();}
}