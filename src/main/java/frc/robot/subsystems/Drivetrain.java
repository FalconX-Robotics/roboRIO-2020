
package frc.robot.subsystems;

import java.util.Arrays;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Ports;
import frc.robot.Constants.DriveConstants;

/**
 * Drivetrain
 */
public class Drivetrain extends SubsystemBase {
    // in inches
    public final double WHEEL_CIRCUMFERENCE = 6. * Math.PI;
    public final double kSRXUnitsPerRevolution = 4096.;

    public final double kNeoEncoderConversionFactor = WHEEL_CIRCUMFERENCE * (12. / 50.) * (24. / 50.);
    public final double kSRXEncoderConversionFactor = (WHEEL_CIRCUMFERENCE / kSRXUnitsPerRevolution) * (50. / 24.);

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

    private final double m_motor_deadband = 0;

    private final PigeonIMU m_gyro = new PigeonIMU(Ports.DRIVETRAIN_GYRO_PORT);

    private final DifferentialDriveOdometry m_odometry;

    private final ShuffleboardTab m_sensorInfoTab = Shuffleboard.getTab("Sensor Info");
    private final ShuffleboardLayout m_encoderLayout = m_sensorInfoTab.getLayout("Encoder", BuiltInLayouts.kList);
    private final ShuffleboardLayout m_gyroLayout = m_sensorInfoTab.getLayout("Gyro", BuiltInLayouts.kList);

    private final NetworkTableEntry m_rawLeftNeoPosWidget = m_encoderLayout
            .add("RAW left NEO pos", m_leftSRXEncoderMotor.getSelectedSensorPosition()).getEntry();
    private final NetworkTableEntry m_leftNeoPosWidget = m_encoderLayout
            .add("left NEO pos", getLeftEncoderPos(EncoderBrand.NEO)).getEntry();
    private final NetworkTableEntry rawLeftSrxPosWidget = m_encoderLayout
            .add("RAW left SRX pos", m_leftSRXEncoderMotor.getSelectedSensorPosition()).getEntry();
    private final NetworkTableEntry m_leftSrxPosWidget = m_encoderLayout
            .add("left SRX pos", getLeftEncoderPos(EncoderBrand.SRX)).getEntry();
    private final NetworkTableEntry m_rawGyroWidget = m_gyroLayout
            .add("raw gyro vals", Arrays.toString(getYawPitchRoll())).getEntry();
    private final NetworkTableEntry m_talonTachWidget = m_sensorInfoTab.add("Talon Tach", false).getEntry();

    private final DigitalInput m_talonTach = new DigitalInput(Constants.Ports.TALON_TACH_PORT);

    /**
     * Creates a drivetrain instance which can be controlled to move the robot. This also
     * contains and outputs all drivetrain measurements (position, speed, angle, etc.) used
     * by other commands and subsystems during autonomous.
     * 
     * @param encoderBrand the brand of encoder being used to record drivetrain measurements
     */
    public Drivetrain(final EncoderBrand encoderBrand) {
        // config motors
        // m_drivetrain.setDeadband(m_motor_deadband);
        m_drivetrain.setSafetyEnabled(true);

        m_frontLeftMotor.restoreFactoryDefaults();
        m_frontRightMotor.restoreFactoryDefaults();
        m_leftSRXEncoderMotor.configFactoryDefault();
        m_rightSRXEncoderMotor.configFactoryDefault();

        // config encoders
        setCurrentEncoderBrand(encoderBrand);

        m_leftNeoEncoder.setPositionConversionFactor(kNeoEncoderConversionFactor);
        m_rightNeoEncoder.setPositionConversionFactor(-kNeoEncoderConversionFactor);
        // m_leftNeoEncoder.setInverted(false);
        // m_leftNeoEncoder.setInverted(false);

        m_leftSRXEncoderMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        m_rightSRXEncoderMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        m_leftSRXEncoderMotor.setSensorPhase(true);
        m_rightSRXEncoderMotor.setSensorPhase(true);

        resetEncoders();

        m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));

        // for testing
        // setMaxOutput(0.5);

        setIdleMode(IdleMode.kBrake);
        setRamp(DriveConstants.RAMP);
    }

    /**
     * Sets what the drivetrain does when it is not moving (either braking or coasting).
     */
    public void setIdleMode(IdleMode idleMode) {
        m_frontLeftMotor.setIdleMode(idleMode);
        m_frontRightMotor.setIdleMode(idleMode);
        m_rearLeftMotor.setIdleMode(idleMode);
        m_rearRightMotor.setIdleMode(idleMode);
    }

    /**
     * Sets the ramp (acceleration rate) of the drivetrain motors.
     * 
     * @param ramp the number of seconds to accelerate from 0 to full throttle
     */
    public void setRamp(double ramp) {
        m_frontLeftMotor.setOpenLoopRampRate(ramp);
        m_frontRightMotor.setOpenLoopRampRate(ramp);
        m_rearLeftMotor.setOpenLoopRampRate(ramp);
        m_rearRightMotor.setOpenLoopRampRate(ramp);
    }

    //enum for the brand of encoder being used to measure with the drivetrain
    public enum EncoderBrand {
        NEO, SRX;
    }

    //returns the brand of encoder being used
    public EncoderBrand getCurrentEncoderBrand() {
        return currentEncoderBrand;
    }

    //changes the brand of encoder being used
    public void setCurrentEncoderBrand(final EncoderBrand brand) {
        if (brand == null || brand == currentEncoderBrand)
            return;
        currentEncoderBrand = brand;
    }

    //sets the encoder positions back to 0
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

    //returns the current position of the left encoder
    public double getLeftEncoderPos(final EncoderBrand brand) {
        switch (brand) {
        case NEO:
            return m_leftNeoEncoder.getPosition();
        case SRX:
            return ((double) m_leftSRXEncoderMotor.getSelectedSensorPosition(0)) * kSRXEncoderConversionFactor;
        default:
            return 0;
        }
    }
    public double getLeftEncoderPos() {
        return getLeftEncoderPos(getCurrentEncoderBrand());
    }

    //returns the current position of the right encoder
    public double getRightEncoderPos(final EncoderBrand encoderBrand) {
        switch (encoderBrand) {
        case NEO:
            return m_rightNeoEncoder.getPosition();
        case SRX:
            return m_rightSRXEncoderMotor.getSelectedSensorPosition(0) * kSRXEncoderConversionFactor;
        default:
            return 0;
        }
    }
    public double getRightEncoderPos() {
        return getRightEncoderPos(getCurrentEncoderBrand());
    }

    //returns the average position of the left and right encoders
    public double getAvgEncoderPos(final EncoderBrand encoderBrand) {
        return (getLeftEncoderPos(encoderBrand) + getRightEncoderPos(encoderBrand)) / 2;
    }
    public double getAvgEncoderPos() {
        return getAvgEncoderPos(currentEncoderBrand);
    }

    //returns the current speed of the left encoder
    public double getLeftEncoderSpeed(final EncoderBrand brand) {
        switch (brand) {
        case NEO:
            return m_leftNeoEncoder.getVelocity();
        case SRX:
            return m_leftSRXEncoderMotor.getSelectedSensorVelocity(0);
        default:
            return 0;
        }
    }
    public double getLeftEncoderSpeed() {
        return getLeftEncoderSpeed(getCurrentEncoderBrand());
    }

    //returns the current speed of the right encoder
    public double getRightEncoderSpeed(final EncoderBrand encoderBrand) {
        switch (encoderBrand) {
        case NEO:
            return m_rightNeoEncoder.getVelocity();
        case SRX:
            return m_rightSRXEncoderMotor.getSelectedSensorVelocity(0);
        default:
            return 0;
        }
    }
    public double getRightEncoderSpeed() {
        return getRightEncoderSpeed(getCurrentEncoderBrand());
    }

    //returns the average speed of the left and right encoders
    public double getAvgEncoderSpeed(final EncoderBrand encoderBrand) {
        return (getLeftEncoderSpeed(encoderBrand) + getRightEncoderSpeed(encoderBrand)) / 2;
    }
    public double getAvgEncoderSpeed() {
        return (getLeftEncoderSpeed() + getRightEncoderSpeed()) / 2;
    }

    //returns the speed of both the left and right encoders (convenient for ramsete)
    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(getLeftEncoderSpeed(), getRightEncoderSpeed());
    }

    //sets the gyroscope yaw (rotation) back to 0
    public void resetGyro() {
        m_gyro.setYaw(0);
    }

    //returns all 3 gyro measurements as a double[]
    public double[] getYawPitchRoll() {
        final double data[] = new double[3];
        m_gyro.getYawPitchRoll(data);
        return data;
    }

    //returns the gyro yaw
    public double getYaw() {
        return getYawPitchRoll()[0];
    }

    //reutrns the gyro pitch
    public double getPitch() {
        return getYawPitchRoll()[1];
    }

    //returns the gyro roll
    public double getRoll() {
        return getYawPitchRoll()[2];
    }

    //returns the yaw as a direction between 0 and 360
    public double getHeading() {
        return Math.IEEEremainder(getYaw(), 360);
    }

    //returns position as a vector of translation and rotation (convenient for ramsete)
    public Pose2d getPose() {
        return m_odometry.getPoseMeters();
    }

    /**
     * Shifts the origin translation and rotation of the drivetrain.
     * 
     * @param pose the pose to set as the new origin (0)
     */
    public void resetOdometry(Pose2d pose) {
        resetEncoders();
        m_odometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
    }

    /**
     * Moves the drivetrain with tank drive using the given speeds.
     * 
     * @param leftSpeed the speed of the left side of the drivetrain
     * @param rightSpeed the speed of the right side of the drivetrain
     * @param squareInput whether or not to drive using squared inputs
     */
    public void tankDrive(final double leftSpeed, final double rightSpeed, final boolean squareInput) {
        m_drivetrain.tankDrive(leftSpeed, rightSpeed, squareInput);
    }
    public void tankDrive(final double leftSpeed, final double rightSpeed) {
        tankDrive(leftSpeed, rightSpeed, false);
    }

    /**
     * Moves the drivetrain with arcade drive using the given speeds.
     * 
     * @param forwardSpeed the speed in which to move the drivetrain forward or backward
     * @param rotationSpeed the speed in which to turn the drivetrain left or right
     * @param squareInput whether or not to drive using squared inputs
     */
    public void arcadeDrive(final double forwardSpeed, final double rotationSpeed, final boolean squareInput) {
        m_drivetrain.arcadeDrive(forwardSpeed, rotationSpeed, squareInput);
    }
    public void arcadeDrive(final double forwardSpeed, final double rotationSpeed) {
        arcadeDrive(forwardSpeed, rotationSpeed, false);
    }

    /**
     * Controls the left and right sides of the drive directly with voltages.
     *
     * @param vLeft  the commanded left output
     * @param vRight the commanded right output
     */
    public void tankDriveVolts(Double vLeft, Double vRight) {
        m_frontLeftMotor.setVoltage(vLeft);
        m_frontRightMotor.setVoltage(vRight);
        m_rearLeftMotor.setVoltage(vLeft);
        m_rearRightMotor.setVoltage(vRight);
    }

    //sets the maximum output, whic is "multiplied with the output percentage computed by the drive functions"
    public void setMaxOutput(final double maxOutput) {
        m_drivetrain.setMaxOutput(maxOutput);
    }

    //stops the left and right side of the drivetrain from moving
    public void stopMotor() {
        m_drivetrain.stopMotor();
    }

    //returns whether or not the Talon Tach is being triggered or not
    public boolean getTalonTachPressed() {
        return m_talonTach.get();
    }

    @Override
    public void periodic() {
        m_rawLeftNeoPosWidget.setDouble(getLeftEncoderPos(EncoderBrand.NEO) / kNeoEncoderConversionFactor);
        m_leftNeoPosWidget.setDouble(getLeftEncoderPos(EncoderBrand.NEO));

        rawLeftSrxPosWidget.setDouble(getLeftEncoderPos(EncoderBrand.SRX) / kSRXEncoderConversionFactor);
        m_leftSrxPosWidget.setDouble(getLeftEncoderPos(EncoderBrand.SRX));

        m_rawGyroWidget.setString(Arrays.toString(getYawPitchRoll()));

        m_talonTachWidget.setBoolean(getTalonTachPressed());

        // System.out.println("left enc: " + getLeftEncoderPos());
        // System.out.println("right enc: " + getRightEncoderPos());
    }
}
