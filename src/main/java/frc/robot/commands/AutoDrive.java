package frc.robot.commands;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;

public class AutoDrive extends PIDCommand {
    private final Drivetrain m_drivetrain;
    private double m_timeStopped = 0;
    private double lastMeasure = 0;
    private double tolerance = 1;
    // private final double m_distance;

    // private static DebugAssistant debug = new DebugAssistant("AutoDrive");
    
    /**
     * Constructor. Moves the robot forward a set distance.
     * 
     * @param distance The distance in which to move the drivetrain
     */
    public AutoDrive(Drivetrain drivetrain, double distance) {
        super(
            new PIDController(0.02, 0, 0),
            () -> 0, // will be overridden in initialize
            distance,
            output -> drivetrain.arcadeDrive(output, 0),
            drivetrain);
        getController().setSetpoint(distance);
        drivetrain.setMaxOutput(1);

        m_drivetrain = drivetrain;
        // m_distance = distance;
        getController().setTolerance(tolerance);

        addRequirements(m_drivetrain);

        // debug.printVar("setpoint", getController().getSetpoint());
    }

    @Override
    public void initialize() {
        // debug.trackInitialize();
        super.initialize();
        m_drivetrain.setIdleMode(IdleMode.kBrake);
        double m_initialMeasurement = m_drivetrain.getLeftEncoderPos();
        m_measurement = () -> Math.abs(-m_initialMeasurement + m_drivetrain.getLeftEncoderPos());
    }

    @Override
    public void execute() {
        super.execute();
    }

    private boolean isAround(double a, double b) {
        return b+tolerance > a && b-tolerance < a;
    }

    private boolean notMovingMuch() {
        double measure = m_measurement.getAsDouble();
        // System.out.println("m_timeStopped: " + m_timeStopped);
        // System.out.println(isAround(measure, lastMeasure));
        if (m_timeStopped >= 2) {
            return true;
        } else if (isAround(measure, lastMeasure)) {
            m_timeStopped = m_timeStopped + (1./50.);
            // System.out.println("set:"+ m_timeStopped);
        } else {
            m_timeStopped = 0;
        }
        lastMeasure = measure;
        return false;
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint() || notMovingMuch();
    }

    @Override
    public void end(boolean isInterrupted) {
        // debug.trackEnd(isInterrupted);
        super.end(isInterrupted);
        m_drivetrain.setIdleMode(IdleMode.kBrake);
    }
}