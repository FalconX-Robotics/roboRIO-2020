package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Ports;

public class Elevator extends SubsystemBase {

    private final CANSparkMax m_elevatorMotor = new CANSparkMax(Ports.ELEVATOR_MOTOR_PORT, MotorType.kBrushed);

    public Elevator() {
        
    }

    public void setElevatorSpeed(final double elevatorSpeed) {
        m_elevatorMotor.set(elevatorSpeed);
    }

    @Override
    public void periodic() {

    }
}