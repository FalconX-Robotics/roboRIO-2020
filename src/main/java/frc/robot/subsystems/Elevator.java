package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Ports;

public class Elevator extends SubsystemBase {
    private final CANSparkMax m_elevatorMotorFront = new CANSparkMax(Ports.ELEVATOR_MOTOR_FRONT_PORT, MotorType.kBrushed);
    private final CANSparkMax m_elevatorMotorBack = new CANSparkMax(Ports.ELEVATOR_MOTOR_BACK_PORT, MotorType.kBrushed);
    
    private final SpeedControllerGroup m_elevatorMotorGroup = new SpeedControllerGroup(m_elevatorMotorFront, m_elevatorMotorBack);
    
    private final DigitalInput m_limitSwitchLower = new DigitalInput(Ports.ELEVATOR_LIMIT_SWITCH_LOWER_PORT);
    private final DigitalInput m_limitSwitchUpper = new DigitalInput(Ports.ELEVATOR_LIMIT_SWITCH_UPPER_PORT);

    public Elevator() {

    }

    public enum ElevatorState {
        HIGH, LOW;
    }

    public void setElevatorHigh() {
        if(getUpperSwitchPressed() != true) {
            setElevatorSpeed(.5);                      //TODO: confirm motor direction for elevator methods
        }
    }

    public void setElevatorLow() {
        if(getLowerSwitchPressed() != true) {
            setElevatorSpeed(.5);
        }
    }

    public void setElevatorSpeed(final double elevatorSpeed) {
        m_elevatorMotorGroup.set(elevatorSpeed);
    }

    public boolean getLowerSwitchPressed() {
        return m_limitSwitchLower.get();
    }

    public boolean getUpperSwitchPressed() {
        return m_limitSwitchUpper.get();
    }

    @Override
    public void periodic() {

    }
}