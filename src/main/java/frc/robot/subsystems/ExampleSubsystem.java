package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

public class ExampleSubsystem {
    private CANSparkMax motor;

    public ExampleSubsystem(CANSparkMax motor) {
        this.motor = motor;
    }

    /**
     * @param speed speed in wrong unit
     */
    public void runMotor(double speed) {
        motor.set(speed / 100.);
    }

}
