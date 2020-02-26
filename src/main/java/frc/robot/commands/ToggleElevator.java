package frc.robot.commands;

import static frc.robot.subsystems.Elevator.ElevatorState.HIGH;
import static frc.robot.subsystems.Elevator.ElevatorState.LOW;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Elevator.ElevatorState;

public class ToggleElevator extends CommandBase {
    private final Elevator m_elevator;
    private ElevatorState m_elevatorState = LOW;

    /**
     * Toggles lift between maximum and minimum height.
     * 
     * @param elevator the Elevator subsystem used by the command
     */
    public ToggleElevator(Elevator elevator) {
        m_elevator = elevator;
        addRequirements(m_elevator);
    }

    @Override
    public void initialize() {
        if (m_elevator.getLowerSwitchPressed()) {
            m_elevatorState = LOW;
        } else {
            m_elevatorState = HIGH;
        }
    }

    @Override
    public void execute() { 
        switch(m_elevatorState) {
            case LOW:
                // System.out.println("set high");
                m_elevator.setElevatorHigh();
                break;
            case HIGH:
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
                return true;
        }
    }

    @Override
    public void end(boolean iterrupted) {
        System.out.println("end");
        m_elevator.stopElevator();
    }
}
