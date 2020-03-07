package frc.robot.commands;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;

import static edu.wpi.first.wpilibj.util.ErrorMessages.requireNonNullParam;

/**
 * ShuffleBoardPIDCommand
 */
public class ShuffleBoardPIDCommand extends PIDCommand {

    private final String m_tabName;
    private final ShuffleboardTab m_tab;

    private final NetworkTableEntry m_pEntry;
    private final NetworkTableEntry m_iEntry;
    private final NetworkTableEntry m_dEntry;

    private final NetworkTableEntry m_finishedEntry;

    private final NetworkTableEntry m_measurementGraphEntry; // displayed as a graph
    private final NetworkTableEntry m_measurementEntry; // displayed as the exact value
    private final ComplexWidget m_startButton;

    private final NetworkTableEntry m_targetInputEntry;

    public ShuffleBoardPIDCommand(String tabName, PIDController controller, DoubleSupplier measurementSource,
            DoubleSupplier setpoint, DoubleConsumer useOutput, Subsystem... requirements) {
        super(controller, measurementSource, setpoint, useOutput, requirements);

        requireNonNullParam(tabName, "tabName", "ShuffleBoardPIDCommand");
        m_tabName = tabName;

        m_tab = Shuffleboard.getTab(m_tabName);
        m_pEntry = m_tab.add("P", 0.).withSize(2, 1).withPosition(0, 0).getEntry();
        m_iEntry = m_tab.add("I", 0.).withSize(2, 1).withPosition(0, 1).getEntry();
        m_dEntry = m_tab.add("D", 0.).withSize(2, 1).withPosition(0, 2).getEntry();

        m_targetInputEntry = m_tab.add("Target Input", 0.).withSize(2, 1).withPosition(2, 2).getEntry();

        m_finishedEntry = m_tab.add("Finished", false).withSize(2, 2).withPosition(2, 0).getEntry();

        m_measurementGraphEntry = m_tab.add("Current Measurement Graph", 0.).withWidget(BuiltInWidgets.kGraph)
                .withSize(6, 4).withPosition(4, 0).getEntry();
        m_measurementEntry = m_tab.add("Current Measurement", 0.).withSize(2, 1).withPosition(4, 4).getEntry();

        m_startButton = m_tab.add("Start", this).withSize(2, 1).withPosition(0, 3);
    }

    public ShuffleBoardPIDCommand(String tabName, PIDController controller, DoubleSupplier measurementSource,
            double setpoint, DoubleConsumer useOutput, Subsystem... requirements) {
        this(tabName, controller, measurementSource, () -> setpoint, useOutput, requirements);
    }

    /**
     * Gets the values of the PID entries and sets it to the controller.
     * 
     * @param defaultP the default value for value P
     * @param defaultI the default value for value I
     * @param defaultD the default value for value D
     */
    public void updatePIDFromShuffleBoard(double defaultP, double defaultI, double defaultD) {
        getController().setP(m_pEntry.getDouble(defaultP));
        getController().setI(m_iEntry.getDouble(defaultI));
        getController().setD(m_dEntry.getDouble(defaultD));
    }

    /**
     * Gets the values of the PID entries and sets it to the controller.
     * Sets the PID values to 0 if no value is found.
     */
    public void updatePIDFromShuffleBoard() {
        updatePIDFromShuffleBoard(0, 0, 0);
    }

    /**
     * Sets the target to the target input entry value in shuffleboard.
     * 
     * @param defaultValue the default value for input target entry
     */
    public void updateTargetInputFromShuffleBoard(double defaultValue) {
        m_setpoint = () -> m_targetInputEntry.getDouble(defaultValue);
    }

    /**
     * Sets the target to the target input entry value in shuffleboard.
     * Sets the target to 0 if no value is found.
     */
    public void updateTargetInputFromShuffleBoard() {
        updateTargetInputFromShuffleBoard(0.);
    }


    protected void updateMeasurements(double measurement) {
        m_measurementGraphEntry.setDouble(measurement);
        m_measurementEntry.setDouble(measurement);
    }

    @Override
    public void initialize() {
        super.initialize();
        m_finishedEntry.setBoolean(false);
    }

    @Override
    public void execute() {
        double measurement = m_measurement.getAsDouble();
        m_useOutput.accept(m_controller.calculate(measurement,
                                              m_setpoint.getAsDouble()));
        updateMeasurements(measurement);
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        updateMeasurements(m_measurement.getAsDouble());
        m_finishedEntry.setBoolean(true);
    }

    public String getTabName() {
        return m_tabName;
    }

    public ShuffleboardTab getTab() {
        return m_tab;
    }

    public NetworkTableEntry getPEntry() {
        return m_pEntry;
    }

    public NetworkTableEntry getIEntry() {
        return m_iEntry;
    }

    public NetworkTableEntry getDEntry() {
        return m_dEntry;
    }

    public NetworkTableEntry getTargetInputEntry() {
        return m_targetInputEntry;
    }

    public NetworkTableEntry getIsFinishedEntry() {
        return m_finishedEntry;
    }

    public NetworkTableEntry getMeasurementGraphEntry() {
        return m_measurementGraphEntry;
    }

    public NetworkTableEntry getMeasurementEntry() {
        return m_measurementEntry;
    }

    public ComplexWidget getStartButton() {
        return m_startButton;
    }
}