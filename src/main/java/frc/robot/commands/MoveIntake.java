package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants.AutoIntakeShuffleBoard;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakePosition;


public class MoveIntake extends PIDCommand {
    private Intake m_intake;
    private IntakePosition m_position;
    private IntakePosition m_currentIntakePostion = IntakePosition.BOTTOM;

    /**
     * Moves the intake mechanism to a set position.
     * 
     * @param intake the intake subsystem used by the command
     * @param position the position enum to move the mechanism towards
     */
    public MoveIntake(Intake intake, IntakePosition position) {
        super(
            new PIDController(AutoIntakeShuffleBoard.pEntry.getDouble(0.04), AutoIntakeShuffleBoard.iEntry.getDouble(0), AutoIntakeShuffleBoard.dEntry.getDouble(0)),
            intake::getIntakeAngleInPercentage,
            position.getDesiredAngle(),
            intake::setIntakeMotor,
            intake);
        getController().setSetpoint(position.getDesiredAngle());
        getController().setTolerance(.1);

        m_currentIntakePostion = intake.getCurrentIntakePosition();

        AutoIntakeShuffleBoard.currentAngle.setDouble(intake.getIntakeAngleInPercentage());
        AutoIntakeShuffleBoard.targetAngle.setDouble(0);
        AutoIntakeShuffleBoard.isFinished.setBoolean(false);

        addRequirements(intake);
        m_intake = intake;
        m_position = position;

        // TODO: Testing
        m_intake.setIntakeMotorMaxOutput(.5);
    }

    @Override
    public void initialize() {
        super.initialize();
        m_intake.resetEncoders();
        System.out.println("init");
        if (m_position == m_currentIntakePostion) cancel();
    }

    @Override
    public void execute() {
        super.execute();
        System.out.println("cur: " + m_measurement.getAsDouble());
        System.out.println("de: " +  m_position.getDesiredAngle());
        AutoIntakeShuffleBoard.currentAngle.setDouble(m_measurement.getAsDouble());
        AutoIntakeShuffleBoard.targetAngle.setDouble(m_position.getDesiredAngle());
    }
     
    @Override
    public boolean isFinished() {
        switch (m_position) {
            case TOP:
                return m_intake.isTopTachPressed() || getController().atSetpoint();
            case MIDDLE:
                return getController().atSetpoint();
            case BOTTOM:
                return m_intake.isBottomTachPressed() || getController().atSetpoint();
            default:
                return true;
        }
    }
        
    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        System.out.println("finished");
        AutoIntakeShuffleBoard.isFinished.setBoolean(true);
        m_intake.setCurrentIntakePosition(m_position);
    }
}