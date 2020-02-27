package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Elevator.ElevatorState;

public class ToggleElevator extends CommandBase{
    private final Elevator m_elevator;
    
    private ElevatorState m_elevatorState;

    /**
     * Toggles lift between maximum and minimum height.
     * 
     * @param elevator the Elevator subsystem used by the command
     */
    public ToggleElevator(Elevator elevator) {
        m_elevator = elevator;
        addRequirements(m_elevator);
    }
    
    public void inititalize() {
        if(m_elevator.getLowerSwitchPressed()) {
            m_elevatorState = ElevatorState.LOW;
        }
        else if(m_elevator.getUpperSwitchPressed()) {
            m_elevatorState = ElevatorState.HIGH;
        }
    }

    @Override
    public void execute() { 
        switch(m_elevatorState) {
            case LOW:
                m_elevator.setElevatorHigh();
                break;
            case HIGH:
                m_elevator.setElevatorLow();
                break;
            default:
                m_elevator.setElevatorLow();
                break;
        }
    }
        

    @Override
    public boolean isFinished() {
        switch(m_elevatorState) {
            case LOW:
                return m_elevator.getUpperSwitchPressed();
            case HIGH:
                return m_elevator.getLowerSwitchPressed();
            default:
                return m_elevator.getLowerSwitchPressed();
        }
    }
}
