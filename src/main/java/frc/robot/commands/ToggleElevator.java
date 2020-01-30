package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Elevator.ElevatorState;

/**
 * Toggles lift between maximum and minimum height
 */
public class ToggleElevator extends CommandBase{
    private final Elevator m_elevator;
    
    private ElevatorState m_elevatorState;

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
        else {
            m_elevator.setElevatorLow();
        }
    }

}
