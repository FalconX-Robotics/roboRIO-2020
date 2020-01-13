package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Ports;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * Drivetrain
 */
public class Drivetrain extends SubsystemBase {

    private final CANSparkMax m_frontLeftMotor = new CANSparkMax(Ports.FRONT_LEFT_MOTOR_PORT, MotorType.kBrushless);
    private final CANSparkMax m_frontRightMotor = new CANSparkMax(Ports.FRONT_RIGHT_MOTOR_PORT, MotorType.kBrushless);
    private final CANSparkMax m_rearLeftMotor = new CANSparkMax(Ports.REAR_LEFT_MOTOR_PORT, MotorType.kBrushless);
    private final CANSparkMax m_rearRightMotor = new CANSparkMax(Ports.REAR_RIGHT_MOTOR_PORT, MotorType.kBrushless);

    private final WPI_TalonSRX m_leftEncoder = new WPI_TalonSRX(Ports.LEFT_ENCODER_MOTOR);
    private final WPI_TalonSRX m_rightEncoder = new WPI_TalonSRX(Ports.RIGHT_ENCODER_MOTOR);

    // private final WPI_TalonSRX m_rearLeftMotor = new WPI_TalonSRX(Ports.REAR_LEFT_MOTOR_PORT);
    // private final WPI_TalonSRX m_rearRightMotor = new WPI_TalonSRX(Ports.REAR_RIGHT_MOTOR_PORT);

    private final SpeedControllerGroup m_leftSide = new SpeedControllerGroup(m_frontLeftMotor, m_rearLeftMotor);
    private final SpeedControllerGroup m_rightSide = new SpeedControllerGroup(m_frontRightMotor, m_rearRightMotor);
    
    private final DifferentialDrive m_drivetrain = new DifferentialDrive(m_leftSide, m_rightSide);

    // private final CANEncoder m_leftEncoder = m_frontLeftMotor.getEncoder(EncoderType.kQuadrature, 1024);
    // private final CANEncoder m_rightEncoder = m_frontRightMotor.getEncoder(EncoderType.kQuadrature, 1024);


    // private final Encoder m_leftEncoder = new Encoder(Ports.LEFT_ENCODER_A, Ports.LEFT_ENCODER_B, false, EncodingType.k4X);
    // private final Encoder m_rightEncoder = new Encoder(Ports.RIGHT_ENCODER_A, Ports.RIGHT_ENCODER_B, false, EncodingType.k4X);

    private final double m_motor_deadband = 0.05;

    public Drivetrain() {
        m_drivetrain.setDeadband(m_motor_deadband);
        m_drivetrain.setSafetyEnabled(true);
        // TODO: remove after testing
        // setMaxOutput(0.2);

        // m_leftEncoder.setDistancePerPulse(Constants.kEncoderDistancePerPulse);
        // m_rightEncoder.setDistancePerPulse(Constants.kEncoderDistancePerPulse);

        // m_leftEncoder.reset();
        // m_rightEncoder.reset();

        // // For testing encoders
        // SmartDashboard.putData(m_frontRightMotor.getSelected);
        // SmartDashboard.putData(m_rightEncoder);

        m_leftEncoder.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        m_rightEncoder.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        m_leftEncoder.setSelectedSensorPosition(0);
        m_rightEncoder.setSelectedSensorPosition(0);

        // m_leftEncoder.setPosition(0);
        // m_rightEncoder.setPosition(0);

    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        m_drivetrain.tankDrive(leftSpeed, rightSpeed, true);
    }

    public void setMaxOutput(double maxOutput) {
        m_drivetrain.setMaxOutput(maxOutput);
    }

    // public void resetEncoders() {
    //     m_leftEncoder.setPosition(0);
    //     m_rightEncoder.setPosition(0);
    // }

    public double getLeftEncoderDistance(double gearRatio) {
        // return m_leftEncoder.getDistance();
        // return m_frontLeftMotor.getSelected
        return m_leftEncoder.getSelectedSensorPosition() * Constants.kEncoderDistancePerPulse * gearRatio;
    }

    public double getRightEncoderDistance(double gearRatio) {
        // return m_rightEncoder.getDistance();
        return m_rightEncoder.getSelectedSensorPosition() * Constants.kEncoderDistancePerPulse * gearRatio;
    }

    public double getEncoderDistance(double gearRatio) {
        return (getLeftEncoderDistance(gearRatio) + getRightEncoderDistance(gearRatio)) / 2;
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("left encoder pos raw", m_leftEncoder.getSelectedSensorPosition());
        SmartDashboard.putNumber("left encoder ratio 1", getLeftEncoderDistance(1));
        SmartDashboard.putNumber("left encoder ratio 16.37", getLeftEncoderDistance(16.37));
        SmartDashboard.putNumber("left encoder ratio 6.0", getLeftEncoderDistance(6));

        SmartDashboard.putNumber("left encoder velocity", m_leftEncoder.getSelectedSensorVelocity());
    }
}