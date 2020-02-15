package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakePosition;


public class MoveIntake extends PIDCommand {
    
    private Intake m_intake;
    private IntakePosition m_position;
    private IntakePosition m_currentIntakePostion = m_intake.getCurrentIntakePosition();

    public MoveIntake(Intake intake, IntakePosition position) {
        super(
            new PIDController(AutoIntakeShuffleBoard.pEntry.getDouble(0), AutoDriveShuffleBoard.iEntry.getDouble(0), AutoDriveShuffleBoard.dEntry.getDouble(0)),
            m_intake::getPitch,
            position.desiredAngle(),
            output -> drivetrain.arcadeDrive(output, 0),
            drivetrain);
        getController().setSetpoint(distance);
        m_intake = intake;
        m_position = position;
    }

    @Override
    public void initialize() {
        if (m_position == m_currentIntakePostion) cancel();
    }

    @Override
    public void execute() {
        switch (m_position) {
            case TOP:
                m_intake.setIntakeMotorForward();
                break;

            case MIDDLE:
                m_intake.setIntakeMotor(0);

                break;

            case BOTTOM:
                m_intake.setIntakeMotorReverse();
                break;     
        }
    }

    private boolean isAround(double input, double target, double threshold) {
        return Math.abs(input - target) <= threshold;
    }
     
    @Override
    public boolean isFinished() {
        switch (m_position) {
            case TOP:
                return m_intake.isTopTachPressed();
            case MIDDLE:
                return isAround(m_intake.getPitch(), 10., 2);
            case BOTTOM:
                return m_intake.isBottomTachPressed();
            default:
                return true;
        }
    }
        
    @Override
    public void end(boolean interrupted) {
        m_intake.setCurrentIntakePosition(m_position);
    }
}