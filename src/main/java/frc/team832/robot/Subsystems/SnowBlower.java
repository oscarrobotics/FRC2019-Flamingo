package frc.team832.robot.Subsystems;

import com.ctre.phoenix.CANifier;
import frc.team832.GrouchLib.Mechanisms.OscarSimpleMechanism;
import frc.team832.GrouchLib.Mechanisms.OscarSmartMechanism;

public class SnowBlower {

    private OscarSimpleMechanism m_intake;
    private OscarSmartMechanism m_hatchHoldor;
    private OscarSmartMechanism m_hatchGrabbor;
    private CANifier m_canifier;

    public SnowBlower(OscarSimpleMechanism intake, OscarSmartMechanism hatchHolder, CANifier canifier,OscarSmartMechanism hatchGrabber){
        m_intake = intake;
        m_hatchHoldor = hatchHolder;
        m_canifier = canifier;
        m_hatchGrabbor = hatchGrabber;
    }



}
