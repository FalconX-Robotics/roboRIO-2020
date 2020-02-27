package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Ports;

public class Elevator extends SubsystemBase {
    private final CANSparkMax m_elevatorMotorFront = new CANSparkMax(Ports.ELEVATOR_MOTOR_FRONT_PORT, MotorType.kBrushless);
    private final CANSparkMax m_elevatorMotorBack = new CANSparkMax(Ports.ELEVATOR_MOTOR_BACK_PORT, MotorType.kBrushless);
    
    private final SpeedControllerGroup m_elevatorMotorGroup = new SpeedControllerGroup(m_elevatorMotorFront, m_elevatorMotorBack);
    
    private final DigitalInput m_limitSwitchLower = new DigitalInput(Ports.ELEVATOR_LIMIT_SWITCH_LOWER_PORT);
    private final DigitalInput m_limitSwitchUpper = new DigitalInput(Ports.ELEVATOR_LIMIT_SWITCH_UPPER_PORT);

    private final double elevatorSpeed = .9;

    /**
     * Creates an elevator instance that can be moved up or down.
     */
    public Elevator() {
        m_elevatorMotorFront.setIdleMode(IdleMode.kBrake);
        m_elevatorMotorBack.setIdleMode(IdleMode.kBrake);
    }
    /**
     * Controls which position the lift will automatically move to
     */
    public enum ElevatorState {
        HIGH, LOW;
    }

    /**
     * Controls direction when manually adjusting elevator height
     */
    public enum ElevatorDirection {
        UP, DOWN;
    }

    public void setElevatorHigh() {
        if (getUpperSwitchPressed() != true) {
            setElevatorSpeed(elevatorSpeed);
        }
    }

    public void setElevatorLow() {
        if (getLowerSwitchPressed() != true) {
            setElevatorSpeed(-elevatorSpeed);
        }
    }

    public void setElevatorSpeed(final double elevatorSpeed) {
        m_elevatorMotorGroup.set(elevatorSpeed);
    }

    public void stopElevator() {
        m_elevatorMotorGroup.stopMotor();
    }

    public boolean getLowerSwitchPressed() {
        return m_limitSwitchLower.get();
    }

    public boolean getUpperSwitchPressed() {
        return m_limitSwitchUpper.get();
    }
}