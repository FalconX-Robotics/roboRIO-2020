package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Drivetrain.EncoderBrand;

public class AutoDrive extends PIDCommand {
    private final Drivetrain m_drivetrain;
    private final double m_driveDistance;
    private final double m_driveSpeed;

    private static final ShuffleboardTab m_sensorInfoTab = Shuffleboard.getTab("Sensor Info");
    private static final ShuffleboardLayout m_autoDrivePID = m_sensorInfoTab.getLayout("Auto Drive PID", BuiltInLayouts.kList);

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

    public AutoDrive(Drivetrain drivetrain, double distance, double speed) { 
        super(
            new PIDController(m_pEntry.getDouble(0), m_iEntry.getDouble(0), m_dEntry.getDouble(0)),
            drivetrain::getAvgEncoderPos,
            distance,
            output -> drivetrain.arcadeDrive(output, 0),
            drivetrain);

        getController().setSetpoint(distance);

        System.out.println(getController().getSetpoint());
        m_drivetrain = drivetrain;
        m_driveDistance = distance;
        m_driveSpeed = speed;
        m_currentDistance.setDouble(m_drivetrain.getAvgEncoderPos());
        m_targetDistance.setDouble(0);
        System.out.println(m_pEntry.getDouble(0));
        System.out.println(m_iEntry.getDouble(0));
        System.out.println(m_dEntry.getDouble(0));
        System.out.println(distance);
        m_isFinished.setBoolean(false);

        getController().setTolerance(1.);

        addRequirements(m_drivetrain);
    }

    @Override
    public void execute() {
        super.execute();
        System.out.println("is running");
        m_currentDistance.setDouble(m_drivetrain.getAvgEncoderPos());
        m_targetDistance.setDouble(0);
    }

    @Override
    public void initialize() {
        super.initialize();
        System.out.println("init");
        m_drivetrain.setMaxOutput(.3);
    }

    @Override
    public boolean isFinished() {
        System.out.println(getController().getSetpoint());
        // System.out.println(getController);
        System.out.println(getController().getPositionError());
        System.out.println(m_drivetrain.getLeftEncoderPos());
        System.out.println(m_drivetrain.getRightEncoderPos());
        System.out.println(m_drivetrain.getAvgEncoderPos());
        System.out.println(getController().atSetpoint());
        return getController().atSetpoint();
    }

    @Override
    public void end(boolean isInterrupted) {
        super.end(isInterrupted);
        m_isFinished.setBoolean(true);
        System.out.println(isInterrupted ? "interrupted" : "finished");
    }
}