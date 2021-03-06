package frc.robot.subsystems;

import static frc.robot.RobotContainer.teleopElevatorLayout;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Ports;

public class Elevator extends SubsystemBase {
    private final CANSparkMax m_motorFront = new CANSparkMax(Ports.ELEVATOR_MOTOR_FRONT_PORT, MotorType.kBrushless);
    private final CANSparkMax m_motorBack = new CANSparkMax(Ports.ELEVATOR_MOTOR_BACK_PORT, MotorType.kBrushless);

    private final SpeedControllerGroup m_motorGroup = new SpeedControllerGroup(m_motorFront, m_motorBack);

    private final DigitalInput m_switchBottom = new DigitalInput(Ports.ELEVATOR_LIMIT_SWITCH_BOTTOM_PORT);
    private final DigitalInput m_switchTop = new DigitalInput(Ports.ELEVATOR_LIMIT_SWITCH_TOP_PORT);

    private final double defaultElevatorSpeed = 1;

    // private static final ShuffleboardTab robotStatustab =
    // Shuffleboard.getTab("Robot Status");
    // private static final ShuffleboardLayout elevatorLayout =
    // robotStatustab.getLayout("Elevator", BuiltInLayouts.kList);
    // private final NetworkTableEntry elevatorStateWidget = elevatorLayout
    // .add("Elevator state", this.getElevatorState()).getEntry();
    // private final NetworkTableEntry lowLimitSwitchWidget = elevatorLayout
    // .add("Lower limit switch", false).getEntry();
    // private final NetworkTableEntry highLimitSwitchWidget = elevatorLayout
    // .add("Upper limit switch", false).getEntry();

    /**
     * Creates an elevator instance that can be moved up or down.
     */
    public Elevator() {
        m_motorFront.setIdleMode(IdleMode.kBrake);
        m_motorBack.setIdleMode(IdleMode.kBrake);
    }
    
    /**
     * Controls which position the lift will automatically move to
     */
    public enum ElevatorState {
        HIGH, LOW;
    }

    /**
     * Controls direction when manually adjusting elevator height
     */
    public enum ElevatorDirection {
        UP, DOWN;
    }

    public void setMotorForward() {
        if (getUpperSwitchPressed() != true) {
            setMotor(defaultElevatorSpeed);
        }
    }

    public void setMotorReverse() {
        if (getLowerSwitchPressed() != true) {
            setMotor(-defaultElevatorSpeed);
        }
    }

    public void setMotor(final double elevatorSpeed) {
        m_motorGroup.set(elevatorSpeed);
    }

    public void stopMotor() {
        m_motorGroup.stopMotor();
    }

    public boolean getLowerSwitchPressed() {
        return m_switchBottom.get();
    }

    public boolean getUpperSwitchPressed() {
        return m_switchTop.get();
    }

    public String getElevatorState() {
        if(getLowerSwitchPressed()) {
            return "Low";
        }
        else {
            return "High";
        }
    }

    @Override
    public void periodic() {
        // elevatorStateWidget.setString(getElevatorState());
        // lowLimitSwitchWidget.setBoolean(getLowerSwitchPressed());
        // highLimitSwitchWidget.setBoolean(getUpperSwitchPressed());
    }

}