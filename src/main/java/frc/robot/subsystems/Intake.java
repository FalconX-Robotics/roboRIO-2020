package frc.robot.subsystems;

import static com.ctre.phoenix.motorcontrol.FeedbackDevice.CTRE_MagEncoder_Relative;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Ports;

public class Intake extends SubsystemBase {
    private final DigitalInput topSwitch = new DigitalInput(Ports.TOP_TACH_PORT);
    private final DigitalInput bottomSwitch = new DigitalInput(Ports.BOTTOM_TACH_PORT);

    private final WPI_TalonSRX m_rollerMotor = new WPI_TalonSRX(Ports.ROLLER_MOTOR_PORT); // not sure // rn
    private final WPI_TalonSRX m_armMotor = new WPI_TalonSRX(Ports.ARM_MOTOR_PORT);

    // private final PigeonIMU m_gyro = new PigeonIMU(Ports.INTAKE_GYRO_PORT);

    private double m_defaultArmMotorSpeed = 0.5;
    private double m_defaultRollerMotorSpeed = 0.7;
    private ArmPosition armCurrentPosition = ArmPosition.BOTTOM;

    private double m_armMaxOutput = 1;
    
    private static final ShuffleboardTab robotStatustab = Shuffleboard.getTab("Robot Status");
    private static final ShuffleboardLayout intakeLayout = robotStatustab.getLayout("Intake", BuiltInLayouts.kList);
    private final NetworkTableEntry intakePositionWidget = intakeLayout
        .add("Intake Position", getIntakePos()).getEntry();
    private final NetworkTableEntry lowSwitchWidget = intakeLayout
        .add("Lower Limit Switch", getBottomSwitchPressed()).getEntry();
    private final NetworkTableEntry toprSwitchWidget = intakeLayout
        .add("Lower Limit Switch", getTopSwitchPressed()).getEntry();

    
    public Intake() {
        m_armMotor.configFactoryDefault();
        m_armMotor.configSelectedFeedbackSensor(CTRE_MagEncoder_Relative);

        m_armMotor.setNeutralMode(NeutralMode.Brake);
        m_armMotor.setInverted(true);

        resetEncoders();
        setArmMotorMaxOutput(.7);

        // m_gyro.configFactoryDefault();
    }

    /**
     * If return value is 1 then the intake is up fully.
     * if return value is 0 then the intake is leveled.
     * 
     * @return a number from 1 to 0
     */
    public double getArmAngleInPercentage() {
        return m_armMotor.getSelectedSensorPosition(0) / 2221.;
    }

    public void resetEncoders() {
        m_armMotor.setSelectedSensorPosition(0);
    }

    public enum ArmPosition {
        BOTTOM(0.05), MIDDLE(.5), TOP(.9);

        private double targetAngle;

        private ArmPosition(double targetAngle) {
            this.targetAngle = targetAngle;
        }

        public double getTargetAngle() {
            return targetAngle;
        }
    }

    public enum RollerState {
        INTAKE, OUTTAKE;
    }

    public ArmPosition getArmCurrentPosition() {
        return armCurrentPosition;
    }

    public void setArmCurrentPosition(final ArmPosition position) {
        if (position == null || position == armCurrentPosition)
            return;
        armCurrentPosition = position;
    }

    public void setArmMotorForward() {
        setArmMotor(m_defaultArmMotorSpeed);
    }

    public void setArmMotorReverse() {
        setArmMotor(-m_defaultArmMotorSpeed);
    }

    public void setArmMotor(double speed) {
        if (getTopSwitchPressed() && speed > 0 || getBottomSwitchPressed() && speed < 0) {
            speed = 0;
        }
        System.out.println(limit(speed, m_armMaxOutput, -m_armMaxOutput));
        m_armMotor.set(limit(speed, m_armMaxOutput, -m_armMaxOutput));
    }

    public void stopArmMotor() {
        m_armMotor.stopMotor();
    }

    public double limit(double input, double max, double min) {
        if (input > max) return max;
        if (input < min) return min;
        return input;
    }

    public void setArmMotorMaxOutput(double maxOutput) {
        if (maxOutput < 0) {
            maxOutput = -maxOutput;
        }
        m_armMaxOutput = maxOutput;
    }

    public void setRollerMotorForward() {
        m_rollerMotor.set(m_defaultRollerMotorSpeed);
    }

    public void setRollerMotorReverse() {
        m_rollerMotor.set(-m_defaultRollerMotorSpeed);
    }

    public void setRollerMotor(double speed) {
        m_rollerMotor.set(speed);
    }
    
    public void stopRollerMotor() {
        m_rollerMotor.stopMotor();
    }

    public boolean getTopSwitchPressed() {
        return topSwitch.get();
    }

    public boolean getBottomSwitchPressed() {
        return bottomSwitch.get();
    }

    public String getIntakePos() {
        if(getBottomSwitchPressed()) {
            return "Low";
        }
        else if(getTopSwitchPressed()) {
            return "High";
        }
        else {
            return "Middle";
        }
    }

    // public double getPitch() {
    //     final double[] data = new double[3];
    //     m_gyro.getYawPitchRoll(data);
    //     return data[1];
    // }

    @Override
    public void periodic() {
        // System.out.println("Top Limit switch: " + isTopTachPressed());
        // System.out.println("Bottom Limit switch: " + isBottomTachPressed()); 
        System.out.println("ang: " + getArmAngleInPercentage());

        intakePositionWidget.setString(getIntakePos());
        lowSwitchWidget.setBoolean(getBottomSwitchPressed());
        toprSwitchWidget.setBoolean(getTopSwitchPressed());
    }
}