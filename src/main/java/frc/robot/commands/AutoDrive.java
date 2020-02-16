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
import frc.robot.Constants.AutoDriveShuffleBoard;
import frc.robot.subsystems.Drivetrain;

public class AutoDrive extends PIDCommand {
    private final Drivetrain m_drivetrain;
    private final double m_distance;
    
    public AutoDrive(Drivetrain drivetrain, double distance) {
        super(
            new PIDController(AutoDriveShuffleBoard.pEntry.getDouble(0), AutoDriveShuffleBoard.iEntry.getDouble(0), AutoDriveShuffleBoard.dEntry.getDouble(0)),
            () -> 0, // will be overriden in initialize
            distance,
            output -> drivetrain.arcadeDrive(output, 0),
            drivetrain);
        getController().setSetpoint(distance);
        drivetrain.setMaxOutput(1);

        m_drivetrain = drivetrain;
        m_distance = distance;

        AutoDriveShuffleBoard.currentDistance.setDouble(m_drivetrain.getLeftEncoderPos());
        AutoDriveShuffleBoard.targetDistance.setDouble(0);
        AutoDriveShuffleBoard.isFinished.setBoolean(false);

        getController().setTolerance(.5);

        addRequirements(m_drivetrain);
    }

    @Override
    public void initialize() {
        super.initialize();
        m_drivetrain.setIdleMode(IdleMode.kBrake);
        double m_initialMeasurement = m_drivetrain.getLeftEncoderPos();
        m_measurement = () -> Math.abs(-m_initialMeasurement + m_drivetrain.getLeftEncoderPos());
    }

    @Override
    public void execute() {
        super.execute();
        AutoDriveShuffleBoard.currentDistance.setDouble(m_measurement.getAsDouble());
        AutoDriveShuffleBoard.currentDistance2.setDouble(m_measurement.getAsDouble());
        AutoDriveShuffleBoard.targetDistance.setDouble(m_distance);
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }

    @Override
    public void end(boolean isInterrupted) {
        super.end(isInterrupted);
        m_drivetrain.setIdleMode(IdleMode.kBrake);
        AutoDriveShuffleBoard.isFinished.setBoolean(true);
    }
}