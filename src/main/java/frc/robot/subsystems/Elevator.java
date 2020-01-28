package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Ports;

public class Elevator extends SubsystemBase {
    private final CANSparkMax m_elevatorMotorFront = new CANSparkMax(Ports.ELEVATOR_MOTOR_FRONT_PORT, MotorType.kBrushed);
    private final CANSparkMax m_elevatorMotorBack = new CANSparkMax(Ports.ELEVATOR_MOTOR_BACK_PORT, MotorType.kBrushed);
    
    private final SpeedControllerGroup m_elevatorMotorGroup = new SpeedControllerGroup(m_elevatorMotorFront, m_elevatorMotorBack);
    
    private final CANEncoder m_elevatorNeoEncoder = m_elevatorMotorFront.getEncoder();

    public Elevator() {

    }

    public void setElevatorSpeed(final double elevatorSpeed) {
        m_elevatorMotorGroup.set(elevatorSpeed);
    }

    public double getEncoderPos() {
        return m_elevatorNeoEncoder.getPosition();
    }

    public void resetEncoder() {
        m_elevatorNeoEncoder.setPosition(0);
    }

    @Override
    public void periodic() {

    }
}