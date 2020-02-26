package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Ports;

public class Intake extends SubsystemBase {

    private final DigitalInput topTalonTach = new DigitalInput(Ports.TOP_TACH_PORT);
    private final DigitalInput bottomTalonTach = new DigitalInput(Ports.BOTTOM_TACH_PORT);

    private final WPI_TalonSRX m_rollerMotor = new WPI_TalonSRX(Ports.ROLLER_MOTOR_PORT);                                                                                                      // not sure                                                                                                          // rn
    private final WPI_TalonSRX m_intakeMotor = new WPI_TalonSRX(Ports.INTAKE_MECHANISM_MOTOR_PORT);

    private final PigeonIMU m_gyro = new PigeonIMU(Ports.INTAKE_GYRO_PORT);

    private double m_intakeMotorSpeed = 0.5; 
    private double m_rollerMotorSpeed = 0.5;
    private IntakePosition currentIntakePosition = IntakePosition.BOTTOM;
    
    private double m_maxOutput = 1;

    public enum IntakePosition {
        BOTTOM(0), MIDDLE(10), TOP(90);

        private double desiredAngle;

        private IntakePosition(double desiredAngle) {
            this.desiredAngle = desiredAngle;
        }

        public double getDesiredAngle() {
            return desiredAngle;
        }
    }

    public enum RollerState {
        INTAKE, OUTTAKE;
    }

    public IntakePosition getCurrentIntakePosition() {
        return currentIntakePosition;
    }

    public void setCurrentIntakePosition(final IntakePosition position) {
        if (position == null || position == currentIntakePosition)
            return;
        currentIntakePosition = position;
    }

    private double limit(double input, double max, double min) {
        if (input > max) return max;
        if (input < min) return min;
        return input;
    }

    public void setIntakeMotorForward() {
        setIntakeMotor(m_intakeMotorSpeed);
    }

    public void setIntakeMotorReverse() {
        setIntakeMotor(-m_intakeMotorSpeed);
    }

    public void setIntakeMotor(double speed) {
        m_intakeMotor.set(limit(speed, m_maxOutput, -m_maxOutput));
    }

    public void stopIntakeMotor() {
        m_intakeMotor.stopMotor();
    }

    public void setRollerMotorForward() {
        m_rollerMotor.set(m_rollerMotorSpeed);
    }

    public void setRollerMotorReverse() {
        m_rollerMotor.set(-m_rollerMotorSpeed);
    }

    public void setRollerMotor(double speed) {
        m_rollerMotor.set(speed);
    }
    
    public void stopRollerMotor() {
        m_rollerMotor.stopMotor();
    }

    public boolean isTopTachPressed() {
        return topTalonTach.get();
    }

    public boolean isBottomTachPressed() {
        return bottomTalonTach.get();
    }

    public double getPitch() {
        final double data[] = new double[3];
        m_gyro.getYawPitchRoll(data);
        return data[0];
    }

    public void setIntakeMotorMaxOutput(double maxOutput) {
        this.m_maxOutput = maxOutput;
    }

    @Override
    public void periodic() {
        System.out.println("Top Limit switch: " + isTopTachPressed());
        System.out.println("Bottom Limit switch: " + isBottomTachPressed());
        
    }
}