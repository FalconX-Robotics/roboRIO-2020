package frc.robot.commands;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants.AutoTurnShuffleBoard;
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
    public AutoTurn(Drivetrain drivetrain, double angle) {
        super(
            new PIDController(AutoTurnShuffleBoard.pEntry.getDouble(0), AutoTurnShuffleBoard.iEntry.getDouble(0), AutoTurnShuffleBoard.dEntry.getDouble(0)),
            // P: 0.05 I: 0.5 D: 0.026
            () -> 0,
            angle,
            output -> drivetrain.arcadeDrive(0, -output),
            drivetrain);
        m_drivetrain = drivetrain;
        m_angle = angle;
        
        // getController().enableContinuousInput(-180, 180);
        getController().setTolerance(1);

        AutoTurnShuffleBoard.currentAngle.setDouble(m_drivetrain.getYaw());
        AutoTurnShuffleBoard.targetAngle.setDouble(0);
        AutoTurnShuffleBoard.isFinished.setBoolean(false);
    }

    @Override
    public void initialize() {
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
        AutoTurnShuffleBoard.currentAngle.setDouble(m_measurement.getAsDouble());
        AutoTurnShuffleBoard.currentAngle2.setDouble(m_measurement.getAsDouble());
        AutoTurnShuffleBoard.targetAngle.setDouble(m_angle);
        
    }

    @Override
    public void end(boolean isInterrupted) {
        super.end(isInterrupted);
        m_drivetrain.setIdleMode(IdleMode.kBrake);
        AutoTurnShuffleBoard.isFinished.setBoolean(true);
    }
}