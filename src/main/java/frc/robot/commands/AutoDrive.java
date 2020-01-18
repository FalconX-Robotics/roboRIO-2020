package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class AutoDrive extends CommandBase {
    private final Drivetrain m_drivetrain;
    private final double m_driveDistance;

    public AutoDrive(Drivetrain drivetrain, double distance) { 
        m_drivetrain = drivetrain;
        m_driveDistance = distance;
        addRequirements(m_drivetrain);
    }

    @Override
    public void execute() {
        if(m_driveDistance > 0) {
            m_drivetrain.tankDrive(0.2, 0.2); //TODO: adjust auto speed after testing
        }
        else {
            m_drivetrain.tankDrive(-0.2, -0.2); 
        }
    }
    
    @Override
    public boolean isFinished() {
        if(m_driveDistance > 0) {
            return m_drivetrain.getLeftEncoderPos() >= m_driveDistance;  //TODO: add buffer for stopping distance
        }
        else {
            return m_drivetrain.getLeftEncoderPos() <= m_driveDistance;
        }
    }
    
    @Override
    public void end(boolean interrupted) {
        m_drivetrain.tankDrive(0, 0);
    }
}