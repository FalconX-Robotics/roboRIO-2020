package frc.robot.commands;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;

public class AutoDrive extends PIDCommand {
    private final Drivetrain m_drivetrain;
    private final double m_distance;
    private final double m_speed;

    private static final ShuffleboardTab m_autoDriveTab = Shuffleboard.getTab("Auto Drive");
    private static final ShuffleboardLayout m_autoDrivePID = m_autoDriveTab.getLayout("Auto Drive PID", BuiltInLayouts.kList);

    private static final NetworkTableEntry m_pEntry = m_autoDrivePID
            .add("P", 0).getEntry();
    private static final NetworkTableEntry m_iEntry = m_autoDrivePID
            .add("I", 0).getEntry();
    private static final NetworkTableEntry m_dEntry = m_autoDrivePID
            .add("D", 0).getEntry();
    private static final NetworkTableEntry m_isFinished = m_autoDrivePID
    .add("Is finished", false).getEntry();
    private static final NetworkTableEntry m_currentDistance = m_autoDrivePID.add("Current Distance", 0).withWidget(BuiltInWidgets.kGraph).getEntry();
    private static final NetworkTableEntry m_targetDistance = m_autoDrivePID.add("Target Distance", 0).withWidget(BuiltInWidgets.kGraph).getEntry();

    static {
        m_autoDrivePID.withSize(4, 5);
    }

    public AutoDrive(Drivetrain drivetrain, double distance, double speed) {
        super(
            new PIDController(m_pEntry.getDouble(0), m_iEntry.getDouble(0), m_dEntry.getDouble(0)),
            () -> 0, // will be overriden in initialize
            distance,
            output -> drivetrain.arcadeDrive(output, 0),
            drivetrain);
        getController().setSetpoint(distance);
        drivetrain.setMaxOutput(1);

        m_drivetrain = drivetrain;
        m_distance = distance;
        m_speed = speed;

        m_currentDistance.setDouble(m_drivetrain.getLeftEncoderPos());
        m_targetDistance.setDouble(0);
        m_isFinished.setBoolean(false);

        getController().setTolerance(.5);

        addRequirements(m_drivetrain);
    }

    @Override
    public void initialize() {
        super.initialize();
        m_drivetrain.setIdleMode(IdleMode.kCoast);
        double m_initialMeasurement = m_drivetrain.getLeftEncoderPos();
        System.out.println(m_initialMeasurement);
        System.out.println(m_drivetrain.getLeftEncoderPos());
        m_measurement = () -> Math.abs(-m_initialMeasurement + m_drivetrain.getLeftEncoderPos());
    }

    @Override
    public void execute() {
        super.execute();
        System.out.println("executing");
        System.out.println(m_measurement.getAsDouble());
        System.out.println("left: " + m_drivetrain.getLeftEncoderPos());
        System.out.println("right: " + m_drivetrain.getRightEncoderPos());
        m_currentDistance.setDouble(m_measurement.getAsDouble());
        m_targetDistance.setDouble(m_distance);
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }

    @Override
    public void end(boolean isInterrupted) {
        super.end(isInterrupted);
        m_drivetrain.setIdleMode(IdleMode.kBrake);
        m_isFinished.setBoolean(true);
    }
}