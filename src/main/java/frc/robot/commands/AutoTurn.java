package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;


public class AutoTurn extends PIDCommand {
    private final Drivetrain m_drivetrain;
    private final double m_angle;
    
    /**
     * Turns the robot in place to a certain angle.
     * 
     * @param angle The angle that the robot will rotate in degrees
     * @param drivetrain The drivetrain subsystem used by the command
     */
    public AutoTurn(double angle, Drivetrain drivetrain) {
        super(
            new PIDController(1, 1, 1),
            drivetrain::getYaw,
            angle,
            output -> drivetrain.arcadeDrive(0, output),
            drivetrain);
        m_drivetrain = drivetrain;
        m_angle = angle;
        
        getController().enableContinuousInput(-180, 180);
        getController().setTolerance(5, 10);

    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }
}