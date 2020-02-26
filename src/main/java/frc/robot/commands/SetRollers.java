package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.RollerState;

public class SetRollers extends CommandBase {    
    private final Intake m_intake;
    private RollerState m_rollerState;

    public SetRollers(Intake intake, RollerState rollerState) {
        m_intake = intake;
        m_rollerState = rollerState;
        addRequirements(m_intake);
    }

    @Override
    public void execute() {
        switch(m_rollerState) {                               
            case INTAKE:
                m_intake.setRollerMotorForward();
                break;
            case OUTTAKE:
                m_intake.setRollerMotorReverse();
                break;
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_intake.stopRollerMotor();
    }
    
}