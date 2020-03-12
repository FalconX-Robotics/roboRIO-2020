package frc.robot.commands;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;


public class AutoTurn extends PIDCommand {
    private final Drivetrain m_drivetrain;
    private final double m_angle;
    

    private double m_timeStopped = 0;
    private double lastMeasure = 0;
    private double tolerance = 1;

    // private static DebugAssistant debug = new DebugAssistant("AutoTurn");
    
    /**
     * Turns the robot in place to a certain angle.
     * 
     * @param drivetrain The drivetrain subsystem used by the command
     * @param angle The angle that the robot will rotate in degrees
     */
    public AutoTurn(Drivetrain drivetrain, double angle) {
        super(
            // new PIDController(AutoConstants.TURN_kP, AutoConstants.TURN_kI, AutoConstants.TURN_kD),
            new PIDController(.01, 0, 0),
            // P: 0.05 I: 0.5 D: 0.026
            () -> 0,
            angle % 360,
            output -> drivetrain.arcadeDrive(0, -output),
            drivetrain);
        m_drivetrain = drivetrain;
        m_angle = angle;
        
        getController().setTolerance(tolerance);
        addRequirements(drivetrain);

        // debug.printVar("setpoint", getController().getSetpoint());
    }

    @Override
    public void initialize() {
        // debug.trackInitialize();
        super.initialize();
        m_drivetrain.setMaxOutput(0.5);
        m_drivetrain.setIdleMode(IdleMode.kBrake);
        double m_initialAngle = m_drivetrain.getYaw();
        if (m_angle > 0) {
            m_measurement = () -> Math.abs(-m_initialAngle + m_drivetrain.getYaw()) % 360;
        } else {
            m_measurement = () -> -Math.abs(-m_initialAngle + m_drivetrain.getYaw()) % 360;
        }   
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
            m_timeStopped=m_timeStopped+(1./50.);
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
    public void execute() {
        super.execute();        
    }

    @Override
    public void end(boolean isInterrupted) {
        // debug.trackEnd(isInterrupted);
        super.end(isInterrupted);
        m_drivetrain.setIdleMode(IdleMode.kBrake);
    }
}