package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Drivetrain;

public class ToggleDrivetrainSpeed extends InstantCommand {
    private static boolean isOnInitMaxOutput = true;
    private final Drivetrain m_drivetrain;
    private final double m_initMaxOutput;
    private final double m_endMaxOutput;

    /**
     * Sets the speed of the drivetrain to a lower speed for more precice control.
     * @param drivetrain the drivetrain subsystem used by the command
     */
    public ToggleDrivetrainSpeed(Drivetrain drivetrain, double initMaxOutput, double endMaxOutput) {
        m_initMaxOutput = initMaxOutput;
        m_endMaxOutput = endMaxOutput;
        m_drivetrain = drivetrain;

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        System.out.println("toggled");
        m_drivetrain.setMaxOutput(isOnInitMaxOutput ? m_initMaxOutput : m_endMaxOutput);
        isOnInitMaxOutput = !isOnInitMaxOutput;
    }

    public boolean isOnInitMaxOutput() {
        return isOnInitMaxOutput;
    }
}