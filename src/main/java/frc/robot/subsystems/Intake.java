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

    public enum IntakePosition {
        BOTTOM, MIDDLE, TOP;
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

    public void setIntakeMotor(double speed) {
        m_intakeMotor.set(speed);
    }

    public void setIntakeMotorForward() {
        m_intakeMotor.set(m_intakeMotorSpeed);
    }

    public void setIntakeMotorReverse() {
        m_intakeMotor.set(-m_intakeMotorSpeed);
    }

    public void stopIntakeMotor() {
        m_intakeMotor.stopMotor();
    }

    public void setRollerMotorForward(double speed) {
        m_rollerMotor.set(speed);
    }

    public void setRollerMotorReverse(double speed) {
        m_rollerMotor.set(-speed);
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
}