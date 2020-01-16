package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Ports;


/**
 * Drivetrain
 */
public class Drivetrain extends SubsystemBase {
    // in inches
    public static final double WHEEL_CIRCUMFERENCE = 6 * Math.PI;
    public static final int NEO_ENCODER_CPR = 4096;
    public static final int NEO_ENCODER_PPR = 4096 / 4;
    public static final int SRX_ENCODER_PPR = 4096 / 4;

    private final CANSparkMax m_frontLeftMotor = new CANSparkMax(Ports.FRONT_LEFT_MOTOR_PORT, MotorType.kBrushless);
    private final CANSparkMax m_frontRightMotor = new CANSparkMax(Ports.FRONT_RIGHT_MOTOR_PORT, MotorType.kBrushless);
    private final CANSparkMax m_rearLeftMotor = new CANSparkMax(Ports.REAR_LEFT_MOTOR_PORT, MotorType.kBrushless);
    private final CANSparkMax m_rearRightMotor = new CANSparkMax(Ports.REAR_RIGHT_MOTOR_PORT, MotorType.kBrushless);

    private final WPI_TalonSRX m_leftSRXEncoderMotor = new WPI_TalonSRX(Ports.LEFT_ENCODER_MOTOR);
    private final WPI_TalonSRX m_rightSRXEncoderMotor = new WPI_TalonSRX(Ports.RIGHT_ENCODER_MOTOR);

    private final SpeedControllerGroup m_leftSide = new SpeedControllerGroup(m_frontLeftMotor, m_rearLeftMotor);
    private final SpeedControllerGroup m_rightSide = new SpeedControllerGroup(m_frontRightMotor, m_rearRightMotor);
    
    private final DifferentialDrive m_drivetrain = new DifferentialDrive(m_leftSide, m_rightSide);

    private final CANEncoder m_leftNeoEncoder = m_frontLeftMotor.getEncoder(EncoderType.kQuadrature, NEO_ENCODER_CPR);
    private final CANEncoder m_rightNeoEncoder = m_frontRightMotor.getEncoder(EncoderType.kQuadrature, NEO_ENCODER_CPR);
    private final double m_motor_deadband = 0.05;

    public Drivetrain(EncoderBrand encoderBrand) {
        m_drivetrain.setDeadband(m_motor_deadband);
        m_drivetrain.setSafetyEnabled(true);
        
        // set up encoders
        EncoderBrand.setCurrent(encoderBrand);
        m_leftNeoEncoder.setPositionConversionFactor(WHEEL_CIRCUMFERENCE / m_leftNeoEncoder.getCountsPerRevolution());
        m_rightNeoEncoder.setPositionConversionFactor(WHEEL_CIRCUMFERENCE / m_rightNeoEncoder.getCountsPerRevolution());
        m_leftSRXEncoderMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        m_rightSRXEncoderMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        resetEncoders();
        
        // for testing
        // setMaxOutput(0.2);
    }

    public enum EncoderBrand {
        NEO, SRX;

        private static EncoderBrand current = NEO;

        public static EncoderBrand getCurrent() {
            return current;
        }

        public static void setCurrent(EncoderBrand brand) {
            if (brand == null) throw new IllegalArgumentException("'brand' cannot be null.");
            if (brand == current) return;
            current = brand;
        }
    }

    public void resetEncoders() {
        switch (EncoderBrand.getCurrent()) {
            case NEO:
                m_leftNeoEncoder.setPosition(0);
                m_rightNeoEncoder.setPosition(0);
                break;
            case SRX:
                m_leftSRXEncoderMotor.setSelectedSensorPosition(0);
                m_rightSRXEncoderMotor.setSelectedSensorPosition(0);
                break;
        }
    }

    public double getLeftEncoderPos(EncoderBrand brand) {
        switch (brand) {
            case NEO:
                return m_leftNeoEncoder.getPosition();
            case SRX:
                return m_leftSRXEncoderMotor.getSelectedSensorPosition() * (WHEEL_CIRCUMFERENCE / SRX_ENCODER_PPR);
            default:
                return 0;
        }
    }

    public double getLeftEncoderPos() {
        return getLeftEncoderPos(EncoderBrand.getCurrent());
    }

    public double getRightEncoderPos(EncoderBrand encoderBrand) {
        switch (encoderBrand) {
            case NEO:
                return m_rightNeoEncoder.getPosition();
            case SRX:
                return m_rightSRXEncoderMotor.getSelectedSensorPosition() * (WHEEL_CIRCUMFERENCE / SRX_ENCODER_PPR);
            default:
                return 0;
        }
    }

    public double getRightEncoderPos() {
        return getRightEncoderPos(EncoderBrand.getCurrent());
    }

    public double getAvgEncoderPos() {
        return (getLeftEncoderPos() + getRightEncoderPos()) / 2;
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        m_drivetrain.tankDrive(leftSpeed, rightSpeed, true);
    }

    public void setMaxOutput(double maxOutput) {
        m_drivetrain.setMaxOutput(maxOutput);
    }

    @Override
    public void periodic() {
        // for testing
        ShuffleboardTab encoderTab = Shuffleboard.getTab("encoder");
        encoderTab.add("raw left neo pos", m_leftNeoEncoder.getPosition());
        encoderTab.add("raw left srx pos", m_leftSRXEncoderMotor.getSelectedSensorPosition());
        encoderTab.add("left neo pos", getLeftEncoderPos(EncoderBrand.NEO));
        encoderTab.add("left srx pos", getLeftEncoderPos(EncoderBrand.SRX));
    }
}