package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class AutoDriveForward extends CommandBase {
    private final Drivetrain m_drivetrain;

    public AutoDriveForward(Drivetrain subsystem) {
        m_drivetrain = subsystem;
        addRequirements(m_drivetrain);
    }
    
    @Override
    public void initialize() {
        //Drive forward 3 feet at 20% speed
        while(m_drivetrain.getEncoderDistance(1) < 36) {   //TODO: put gear ratio
            m_drivetrain.tankDrive(.2, .2);
        } 
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
    
    @Override
    public void end(boolean interrupted) {
        m_drivetrain.tankDrive(0, 0);
    }
}