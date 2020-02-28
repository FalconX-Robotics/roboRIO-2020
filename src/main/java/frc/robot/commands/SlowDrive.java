package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class SlowDrive extends CommandBase{
    private final Drivetrain m_drivetrain;
    private final double m_slowSpeed;

    /**
     * Sets the speed of the drivetrain to a lower speed for more precice control.
     * @param drivetrain the drivetrain subsystem used by the command
     */
    public SlowDrive(Drivetrain drivetrain, double slowSpeed) {
        m_slowSpeed = slowSpeed;
        m_drivetrain = drivetrain;
    }

    @Override
    public void execute() {
        m_drivetrain.setMaxOutput(m_slowSpeed);
    }

    public void end() {
        m_drivetrain.setMaxOutput(1.0);
    }
}