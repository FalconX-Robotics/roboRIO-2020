package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Drivetrain.EncoderBrand;

public class AutoDrive extends CommandBase {
    private final Drivetrain m_drivetrain;
    private final double m_driveDistance;
    private final double m_driveSpeed;

    public AutoDrive(Drivetrain drivetrain, double distance, double speed) { 
        m_drivetrain = drivetrain;
        m_driveDistance = distance;
        m_driveSpeed = speed;
        addRequirements(m_drivetrain);
    }

    @Override
    public void initialize() {
        m_drivetrain.setMaxOutput(1);
    }

    @Override
    public void execute() {
        if(m_driveDistance > 0) {
            m_drivetrain.tankDrive(m_driveSpeed, m_driveSpeed); //TODO: adjust auto speed after testing
        }
        else {
            m_drivetrain.tankDrive(-1*m_driveSpeed, -1*m_driveSpeed); 
        }
    }
    
    @Override
    public boolean isFinished() {
        if(m_driveDistance > 0) {
            return m_drivetrain.getLeftEncoderPos(EncoderBrand.NEO) >= m_driveDistance;  //TODO: add buffer for stopping distance
        }
        else {
            return m_drivetrain.getLeftEncoderPos(EncoderBrand.NEO) <= m_driveDistance;
        }
    }
    
    @Override
    public void end(boolean interrupted) {
        m_drivetrain.stopMotor();
    }
}