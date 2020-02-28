package frc.robot.commands;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.controller.PIDController;
import frc.robot.subsystems.Drivetrain;


public class AutoTurn extends ShuffleBoardPIDCommand {
    private final Drivetrain m_drivetrain;
    private final double m_angle;
    
    /**
     * Turns the robot in place to a certain angle.
     * 
     * @param drivetrain The drivetrain subsystem used by the command
     * @param angle The angle that the robot will rotate in degrees
     */
    public AutoTurn(Drivetrain drivetrain, double angle) {
        super(
            "AutoTurn",
            // new PIDController(AutoConstants.TURN_kP, AutoConstants.TURN_kI, AutoConstants.TURN_kD),
            new PIDController(0, 0, 0),
            // P: 0.05 I: 0.5 D: 0.026
            () -> 0,
            angle,
            output -> drivetrain.arcadeDrive(0, -output),
            drivetrain);
        m_drivetrain = drivetrain;
        m_angle = angle;
        
        getController().setTolerance(1);
    }

    @Override
    public void initialize() {
        updatePIDFromShuffleBoard();
        updateTargetInputFromShuffleBoard();
        super.initialize();
        m_drivetrain.setMaxOutput(0.5);
        m_drivetrain.setIdleMode(IdleMode.kBrake);
        double m_initialAngle = m_drivetrain.getYaw();
        if (m_angle > 0) {
            m_measurement = () -> Math.abs(-m_initialAngle + m_drivetrain.getYaw());
        } else {
            m_measurement = () -> -Math.abs(-m_initialAngle + m_drivetrain.getYaw());
        }   
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }

    @Override
    public void execute() {
        super.execute();        
    }

    @Override
    public void end(boolean isInterrupted) {
        super.end(isInterrupted);
        m_drivetrain.setIdleMode(IdleMode.kBrake);
    }
}