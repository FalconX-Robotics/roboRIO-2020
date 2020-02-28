package frc.robot.commands;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.controller.PIDController;
import frc.robot.subsystems.Drivetrain;

public class AutoDrive extends ShuffleBoardPIDCommand {
    private final Drivetrain m_drivetrain;
    private final double m_distance;
    
    /**
     * Constructor. Moves the robot forward a set distance.
     * 
     * @param distance The distance in which to move the drivetrain
     */
    public AutoDrive(Drivetrain drivetrain, double distance) {
        super(
            "AutoDrive",
            new PIDController(0, 0, 0),
            () -> 0, // will be overridden in initialize
            distance,
            output -> drivetrain.arcadeDrive(output, 0),
            drivetrain);
        getController().setSetpoint(distance);
        drivetrain.setMaxOutput(1);

        m_drivetrain = drivetrain;
        m_distance = distance;
        getController().setTolerance(.5);

        addRequirements(m_drivetrain);
    }

    @Override
    public void initialize() {
        updatePIDFromShuffleBoard();
        updateTargetInputFromShuffleBoard();
        super.initialize();
        m_drivetrain.setIdleMode(IdleMode.kBrake);
        double m_initialMeasurement = m_drivetrain.getLeftEncoderPos();
        m_measurement = () -> Math.abs(-m_initialMeasurement + m_drivetrain.getLeftEncoderPos());
    }

    @Override
    public void execute() {
        super.execute();
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }

    @Override
    public void end(boolean isInterrupted) {
        super.end(isInterrupted);
        m_drivetrain.setIdleMode(IdleMode.kBrake);
    }
}