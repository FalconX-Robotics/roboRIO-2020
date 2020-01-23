package frc.robot.subsystems;

import java.util.Arrays;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Ports;

/**
 * Drivetrain
 */
public class Drivetrain extends SubsystemBase {
    // in inches
    public final double WHEEL_CIRCUMFERENCE = 6. * Math.PI;
    // public final int NEO_ENCODER_CPR = 42;
    // public final double NEO_ENCODER_PPR = NEO_ENCODER_CPR / 4;
    // public final int SRX_ENCODER_CPR = 1024;
    // public final double SRX_ENCODER_PPR = SRX_ENCODER_CPR / 4.;
    public final double kSRXUnitsPerRevolution = 4096.;
    
    public final double kNeoEncoderConversionFactor = WHEEL_CIRCUMFERENCE * (12./50.) * (24./50.);
    public final double kSRXEncoderConversionFactor = WHEEL_CIRCUMFERENCE / kSRXUnitsPerRevolution;

    private final CANSparkMax m_frontLeftMotor = new CANSparkMax(Ports.FRONT_LEFT_MOTOR_PORT, MotorType.kBrushless);
    private final CANSparkMax m_frontRightMotor = new CANSparkMax(Ports.FRONT_RIGHT_MOTOR_PORT, MotorType.kBrushless);
    private final CANSparkMax m_rearLeftMotor = new CANSparkMax(Ports.REAR_LEFT_MOTOR_PORT, MotorType.kBrushless);
    private final CANSparkMax m_rearRightMotor = new CANSparkMax(Ports.REAR_RIGHT_MOTOR_PORT, MotorType.kBrushless);

    private final WPI_TalonSRX m_leftSRXEncoderMotor = new WPI_TalonSRX(Ports.LEFT_ENCODER_MOTOR);
    private final WPI_TalonSRX m_rightSRXEncoderMotor = new WPI_TalonSRX(Ports.RIGHT_ENCODER_MOTOR);

    private final SpeedControllerGroup m_leftSide = new SpeedControllerGroup(m_frontLeftMotor, m_rearLeftMotor);
    private final SpeedControllerGroup m_rightSide = new SpeedControllerGroup(m_frontRightMotor, m_rearRightMotor);

    private final DifferentialDrive m_drivetrain = new DifferentialDrive(m_leftSide, m_rightSide);

    private final CANEncoder m_leftNeoEncoder = m_frontLeftMotor.getEncoder();
    private final CANEncoder m_rightNeoEncoder = m_frontRightMotor.getEncoder();
    private EncoderBrand currentEncoderBrand = EncoderBrand.NEO;

    private final double m_motor_deadband = 0.1;

    private final PigeonIMU m_gyro = new PigeonIMU(Ports.GYRO_PORT);
    
    private final ShuffleboardTab sensorInfoTab = Shuffleboard.getTab("Sensor Info");
    public final ShuffleboardLayout encoderLayout = sensorInfoTab.getLayout("Encoder", BuiltInLayouts.kList);
    public final ShuffleboardLayout gyroLayout = sensorInfoTab.getLayout("Gyro", BuiltInLayouts.kList);

    private final NetworkTableEntry rawLeftSrxPosWidget = encoderLayout
            .add("RAW left SRX pos", m_leftSRXEncoderMotor.getSelectedSensorPosition()).getEntry();
    private final NetworkTableEntry rawLeftNeoPosWidget = encoderLayout
            .add("RAW left NEO pos", m_leftSRXEncoderMotor.getSelectedSensorPosition()).getEntry();
    private final NetworkTableEntry leftNeoPosWidget = encoderLayout
            .add("left NEO pos", getLeftEncoderPos(EncoderBrand.NEO)).getEntry();
    private final NetworkTableEntry leftSrxPosWidget = encoderLayout
            .add("left SRX pos", getLeftEncoderPos(EncoderBrand.SRX)).getEntry();
    private final NetworkTableEntry rawGyroWidget = gyroLayout.add("raw gyro vals", Arrays.toString(getYawPitchRoll()))
            .getEntry();

    public Drivetrain(final EncoderBrand encoderBrand) {
        // config motors
        m_drivetrain.setDeadband(m_motor_deadband);
        m_drivetrain.setSafetyEnabled(true);
        
        m_frontLeftMotor.restoreFactoryDefaults();
        m_frontRightMotor.restoreFactoryDefaults();
        m_leftSRXEncoderMotor.configFactoryDefault();
        m_rightSRXEncoderMotor.configFactoryDefault();

        // m_leftSide.(true);
        // m_rightSide.setInverted(true);

        // config encoders
        setCurrentEncoderBrand(encoderBrand);

        m_leftNeoEncoder.setPositionConversionFactor(kNeoEncoderConversionFactor);
        m_rightNeoEncoder.setPositionConversionFactor(kNeoEncoderConversionFactor);
        // m_leftNeoEncoder.setInverted(false);
        // m_leftNeoEncoder.setInverted(false);
        
        m_leftSRXEncoderMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        m_rightSRXEncoderMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        // m_leftSRXEncoderMotor.configSelectedFeedbackCoefficient(kSRXEncoderConversionFactor);
        // m_rightSRXEncoderMotor.configSelectedFeedbackCoefficient(kSRXEncoderConversionFactor);
        m_leftSRXEncoderMotor.setSensorPhase(true);
        m_rightSRXEncoderMotor.setSensorPhase(true);
        
        resetEncoders();

        // for testing
        setMaxOutput(0.5);
    }
    
    public enum EncoderBrand {
        NEO, SRX;
    }

    public EncoderBrand getCurrentEncoderBrand() {
        return currentEncoderBrand;
    }

    public void setCurrentEncoderBrand(final EncoderBrand brand) {
        if (brand == null || brand == currentEncoderBrand)
            return;
        currentEncoderBrand = brand;
    }

    public void resetEncoders(EncoderBrand brand) {
        switch (brand) {
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

    public void resetEncoders() {
        resetEncoders(getCurrentEncoderBrand());
    }

    public double getLeftEncoderPos(final EncoderBrand brand) {
        switch (brand) {
        case NEO:
            return m_leftNeoEncoder.getPosition();
        case SRX:
            return m_leftSRXEncoderMotor.getSelectedSensorPosition(0);
        default:
            return 0;
        }
    }

    public double getLeftEncoderPos() {
        return getLeftEncoderPos(getCurrentEncoderBrand());
    }

    public double getRightEncoderPos(final EncoderBrand encoderBrand) {
        switch (encoderBrand) {
        case NEO:
            return m_rightNeoEncoder.getPosition();
        case SRX:
            return m_rightSRXEncoderMotor.getSelectedSensorPosition(0);
        default:
            return 0;
        }
    }

    public double getRightEncoderPos() {
        return getRightEncoderPos(getCurrentEncoderBrand());
    }

    public double getAvgEncoderPos() {
        return (getLeftEncoderPos() + getRightEncoderPos()) / 2;
    }

    public void resetGyro() {
        m_gyro.setYaw(0);
    }

    public double[] getYawPitchRoll() {
        final double data[] = new double[3];
        m_gyro.getYawPitchRoll(data);
        return data;
    }

    public double getYaw() {
        return getYawPitchRoll()[0];
    }

    public double getPitch() {
        return getYawPitchRoll()[1];
    }

    public double getRoll() {
        return getYawPitchRoll()[2];
    }

    public void tankDrive(final double leftSpeed, final double rightSpeed) {
        m_drivetrain.tankDrive(leftSpeed, rightSpeed, true);
    }

    public void arcadeDrive(final double forwardSpeed, final double rotationSpeed) {
        m_drivetrain.arcadeDrive(forwardSpeed, rotationSpeed, true);
    }

    public void setMaxOutput(final double maxOutput) {
        m_drivetrain.setMaxOutput(maxOutput);
    }

    public void stopMotor() {
        m_drivetrain.stopMotor();
    }

    public void setInvertedMotors(boolean isInverted) {
        m_leftSide.setInverted(isInverted);
        m_rightSide.setInverted(isInverted);
    }

    @Override
    public void periodic() {
        rawLeftSrxPosWidget.setDouble(getLeftEncoderPos(EncoderBrand.SRX));
        leftSrxPosWidget.setDouble(((double) getLeftEncoderPos(EncoderBrand.SRX)) * kSRXEncoderConversionFactor);

        rawLeftNeoPosWidget.setDouble(getLeftEncoderPos(EncoderBrand.NEO) / kNeoEncoderConversionFactor);
        leftNeoPosWidget.setDouble(getLeftEncoderPos(EncoderBrand.NEO));

        rawGyroWidget.setString(Arrays.toString(getYawPitchRoll()));
    }
}