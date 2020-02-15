package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.RollerState;

public class SetRollers extends CommandBase {

    private final double intakeSpeed = 0.2;     //TODO: test for appropriate speed
    private final double outtakeSpeed = 0.5;
    
    private final Intake m_intake;
    private RollerState m_rollerState;

    public SetRollers(Intake intake, RollerState rollerState) {
        m_intake = intake;
        m_rollerState = rollerState;
        addRequirements(m_intake);
    }

    @Override
    public void execute(

    )
    
}