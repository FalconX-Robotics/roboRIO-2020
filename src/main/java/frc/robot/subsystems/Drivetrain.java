package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.Ports;

/**
 * Drivetrain
 */
public class Drivetrain extends SubsystemBase {

    private final CANSparkMax m_frontLeftMotor = new CANSparkMax(Ports.FRONT_LEFT_MOTOR_PORT, MotorType.kBrushless);
    private final CANSparkMax m_frontRightMotor = new CANSparkMax(Ports.FRONT_RIGHT_MOTOR_PORT, MotorType.kBrushless);
    private final CANSparkMax m_rearLeftMotor = new CANSparkMax(Ports.REAR_LEFT_MOTOR_PORT, MotorType.kBrushless);
    private final CANSparkMax m_rearRightMotor = new CANSparkMax(Ports.REAR_RIGHT_MOTOR_PORT, MotorType.kBrushless);

    private final SpeedControllerGroup m_leftSide = new SpeedControllerGroup(m_frontLeftMotor, m_rearLeftMotor);
    private final SpeedControllerGroup m_rightSide = new SpeedControllerGroup(m_frontRightMotor, m_rearRightMotor);
    
    private final DifferentialDrive m_drivetrain = new DifferentialDrive(m_leftSide, m_rightSide);

    private final double m_motor_deadband = 0.05;

    public Drivetrain() {
        m_drivetrain.setDeadband(m_motor_deadband);
        m_drivetrain.setSafetyEnabled(true);
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        m_drivetrain.tankDrive(leftSpeed, rightSpeed);
    }

    public void setMaxOutput(double maxOutput) {
        m_drivetrain.setMaxOutput(maxOutput);
    }
}