package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Ports;

public class Climber extends SubsystemBase {

    CANSparkMax m_gondalaMotor = new CANSparkMax(Ports.GONDALA_MOTOR_PORT, MotorType.kBrushless);

    public Climber() {

    }


    /**
     * @param speed The speed of the motor. -1 is left (maybe) and +1 is right (maybe)
     */

    //TODO check if directions are correct
    public void moveGondola(double speed) {
        m_gondalaMotor.set(speed);
    }
    /**
     * Stops the gondola 
     */
    public void stopGondola() {
        m_gondalaMotor.stopMotor();
    }
}