package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakePosition;


public class MoveIntake extends CommandBase {
    
    private Intake m_intake;
    private IntakePosition m_position;
    private IntakePosition m_currentIntakePostion = m_intake.getCurrentIntakePosition();

    public MoveIntake(Intake intake, IntakePosition position) { 
        m_intake = intake;
        m_position = position;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        switch (m_position) {
            case GROUND:
                if(m_currentIntakePostion == IntakePosition.STORE) {
                    //go from store to ground
                } 
                else if(m_currentIntakePostion == IntakePosition.DISPENSE){
                    //go from dispense to ground
                }
            case STORE:
                if(m_currentIntakePostion == IntakePosition.GROUND) {
                    //go from ground to store
                } 
                else if(m_currentIntakePostion == IntakePosition.DISPENSE){
                    //go from dispense to store
                }
            case DISPENSE:
                if(m_currentIntakePostion == IntakePosition.STORE) {
                    //go from store to dispense
                } 
                else if(m_currentIntakePostion == IntakePosition.GROUND){
                    //go from dispense to dispense
                }      
        }
    }
     
    @Override
    public boolean isFinished() {
        return true; 
    }
    
    // ^^ I don't know what those actually do, I'm going to try and figure it out friday
    
    @Override
    public void end(boolean interrupted) {
        m_intake.setCurrentIntakePosition(m_position);
    }
}