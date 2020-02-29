package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Elevator.ElevatorDirection;

public class MoveElevator extends CommandBase {
    private final Elevator m_elevator;
    private ElevatorDirection m_elevatorDirection;
    
    /**
     * Manually adjusts lift height
     * 
     * @param elevator the Elevator subsystem used by the command
     * @param elevatorDirection the direction the elevator will be moved
     */
    public MoveElevator(Elevator elevator, ElevatorDirection elevatorDirection) {
        m_elevator = elevator;
        m_elevatorDirection = elevatorDirection;
        addRequirements(m_elevator);
    }

    @Override
    public void execute() {
        switch(m_elevatorDirection) {
            case UP:
                m_elevator.setMotor(.4);
                break;
            case DOWN:
                m_elevator.setMotor(-.4);
                break;
        }
    }

    @Override
    public boolean isFinished() {
        switch (m_elevatorDirection) {
            case DOWN:
                return m_elevator.getLowerSwitchPressed();
            default:
                return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_elevator.stopMotor();
    }
    
}