package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Ports;

public class Climber extends SubsystemBase {
    // CANSparkMax m_gondalaMotor = new CANSparkMax(Ports.GONDALA_MOTOR_PORT, MotorType.kBrushless);
    WPI_TalonSRX m_gondolaMotor = new WPI_TalonSRX(Ports.GONDOLA_MOTOR_PORT);

    /**
     * Creates a climber instance that can be moved forwards or backwards
     * along the switch.
     */
    public Climber() {
    }

    /**
     * @param speed The speed of the motor. -1 is left (maybe) and +1 is right (maybe)
     */
    public void moveGondola(double speed) {
        //TODO check if directions are correct
        m_gondolaMotor.set(speed);
    }
    /**
     * Stops the gondola 
     */
    public void stopGondola() {
        m_gondolaMotor.stopMotor();
    }
}