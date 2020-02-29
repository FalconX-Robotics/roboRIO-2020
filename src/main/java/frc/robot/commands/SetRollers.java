package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.RollerState;

public class SetRollers extends CommandBase {
    private final double m_intakeSpeed = 0.2;     //TODO: test for appropriate speed
    private final double m_outtakeSpeed = 0.2;
    
    private final Intake m_intake;
    private RollerState m_rollerState;

    /**
     * Sets the direction in which the intake subsystem motors are turning. Forward
     * for intake and reverse for outtake.
     * 
     * @param intake the intake subsystem used by the command
     * @param rollerState the direction enum in which to move the rollers
     */
    public SetRollers(Intake intake, RollerState rollerState) {
        m_intake = intake;
        m_rollerState = rollerState;
        addRequirements(m_intake);
    }

    @Override
    public void execute() {
        switch(m_rollerState) {                               
            case INTAKE:
                m_intake.setRollerMotorReverse();
                break;
            case OUTTAKE:
                m_intake.setRollerMotorForward();
                break;
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_intake.stopRollerMotor();
    }
    
}