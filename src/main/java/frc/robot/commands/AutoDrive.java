package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Drivetrain.EncoderBrand;

public class AutoDrive extends PIDCommand {
    private final Drivetrain m_drivetrain;
    private final double m_driveDistance;
    private final double m_driveSpeed;

    public AutoDrive(Drivetrain drivetrain, double distance, double speed) { 
        super(
            new PIDController(0, 0, 0),
            drivetrain::getAvgEncoderPos,
            distance,
            output -> drivetrain.arcadeDrive(output, 0),
            drivetrain);
        m_drivetrain = drivetrain;
        m_driveDistance = distance;
        m_driveSpeed = speed;

        getController().setTolerance(1);

        addRequirements(m_drivetrain);
    }

    @Override
    public void initialize() {
        m_drivetrain.setMaxOutput(.1);
    }

    
    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }
}