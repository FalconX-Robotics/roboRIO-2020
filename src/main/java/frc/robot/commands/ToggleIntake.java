package frc.robot.commands;

import static frc.robot.subsystems.Intake.IntakePosition.TOP;
import static frc.robot.subsystems.Intake.IntakePosition.BOTTOM;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakePosition;

public class ToggleIntake extends CommandBase {
    private final Intake m_intake;
    private IntakePosition m_intakePosition = IntakePosition.BOTTOM;

    /**
     * Toggles the arm position
     * 
     * @param intake the Intake subsystem used by the command
     */
    public ToggleIntake(Intake intake) {
        m_intake = intake;
        addRequirements(m_intake);
    }

    @Override
    public void initialize() {
        if (m_intake.getCurrentIntakePosition() == BOTTOM) {
            m_intakePosition = BOTTOM;
        } else {
            m_intakePosition= TOP;
        }
    }

    @Override
    public void execute() { 
        switch(m_intakePosition) {
            case BOTTOM:
                m_intake.setIntakeMotorForward();
                break;
            case MIDDLE:
                m_intake.setIntakeMotorReverse();
                break;
            case TOP:
                m_intake.setIntakeMotorReverse();
                break;
        }
    }
        

    @Override
    public boolean isFinished() {
        switch(m_intakePosition) {
            case BOTTOM:
                return m_intake.isTopTachPressed();
            case MIDDLE:
                return m_intake.isBottomTachPressed();
            case TOP:
                return m_intake.isBottomTachPressed();
            default:
                return true;
        }
    }

    @Override
    public void end(boolean iterrupted) {
        System.out.println("end");
        m_intake.stopIntakeMotor();
    }
}
