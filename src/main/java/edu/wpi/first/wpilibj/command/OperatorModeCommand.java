package edu.wpi.first.wpilibj.command;

import frc.team832.robot.OI;

import java.util.Enumeration;

public abstract class OperatorModeCommand extends Command {
	private Command m_onDefault;
	private Command m_onState1;
	private Command m_onState2;
	private Command m_onState3;

	private Command m_chosenCommand;

	private void requireAll() {
		if (m_onDefault != null) {
			for (Enumeration e = m_onDefault.getRequirements(); e.hasMoreElements();) {
				requires((Subsystem) e.nextElement());
			}
		}

		if (m_onState1 != null) {
			for (Enumeration e = m_onState1.getRequirements(); e.hasMoreElements();) {
				requires((Subsystem) e.nextElement());
			}
		}

		if (m_onState2 != null) {
			for (Enumeration e = m_onState2.getRequirements(); e.hasMoreElements();) {
				requires((Subsystem) e.nextElement());
			}
		}
		if (m_onState3 != null) {
			for (Enumeration e = m_onState3.getRequirements(); e.hasMoreElements();) {
				requires((Subsystem) e.nextElement());
			}
		}
	}

	public OperatorModeCommand(Command onDefault, Command onState1, Command onState2, Command onState3) {
		m_onDefault = onDefault;
		m_onState1 = onState1;
		m_onState2 = onState2;
		m_onState3 = onState3;

		requireAll();
	}

	protected abstract OI.OperatorMode condition();

	@Override
	protected void _initialize() {
		switch (condition()) {
			case Intake:
				m_chosenCommand = m_onDefault;
				break;
			case CargoShip:
				m_chosenCommand = m_onState1;
				break;
			case Rocket_Hatch:
				m_chosenCommand = m_onState2;
				break;
			case Rocket_Cargo:
				m_chosenCommand = m_onState3;
				break;
		}

		if (m_chosenCommand != null) {
			m_chosenCommand.clearRequirements();

			m_chosenCommand.start();
		}
		super._initialize();
	}

	@Override
	protected synchronized void _cancel() {
		if (m_chosenCommand != null && m_chosenCommand.isRunning()) {
			m_chosenCommand.cancel();
		}

		super._cancel();
	}

	@Override
	protected boolean isFinished() {
		if (m_chosenCommand != null) {
			return m_chosenCommand.isCompleted();
		} else {
			return true;
		}
	}

	@Override
	protected void _interrupted() {
		if (m_chosenCommand != null && m_chosenCommand.isRunning()) {
			m_chosenCommand.cancel();
		}

		super._interrupted();
	}
}
