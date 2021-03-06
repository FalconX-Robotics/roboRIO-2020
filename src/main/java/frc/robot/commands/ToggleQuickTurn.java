package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Drivetrain;

public class ToggleQuickTurn extends InstantCommand {
    private final Drivetrain m_drivetrain;
    

    public ToggleQuickTurn(Drivetrain drivetrain) {
        m_drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        m_drivetrain.toggleQuickTurn();
    }
}