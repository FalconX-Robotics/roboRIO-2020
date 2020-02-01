package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Ports;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Intake extends SubsystemBase {

    private final CANSparkMax m_rollerMotor = new CANSparkMax(Ports.ROLLER_MOTOR_PORT, MotorType.kBrushed); // may be                                                                                                        // not sure                                                                                                          // rn
    private final CANSparkMax m_intakeMotor = new CANSparkMax(Ports.INTAKE_MECHANISM_MOTOR_PORT, MotorType.kBrushed); // ^^
    private double intakeMotorSpeed = 0.5; 
    private double rollerMotorSpeed = 0.5;
    public IntakePosition currentIntakePosition = IntakePosition.GROUND;


    public enum IntakePosition {
        GROUND, STORE, DISPENSE;
    }

    public IntakePosition getCurrentIntakePosition() {
        return currentIntakePosition;
    }

    public void setCurrentIntakePosition(final IntakePosition position) {
        if (position == null || position == currentIntakePosition)
            return;
            currentIntakePosition = position;
    }

    public void moveUp() {
        m_intakeMotor.set(intakeMotorSpeed);
    }

    public void moveDown() {
        m_intakeMotor.set(-intakeMotorSpeed);
    }
    //I want to change those to move [degrees] up and move [degrees] down and for that i will need to know things like rpm etc.
    
    public void toggleRollers(final boolean state) {
        if (state == true) {
            m_rollerMotor.set(rollerMotorSpeed);
        } else {
            m_rollerMotor.set(0);
        }
    }
}


//ok so do the thing on command by taking current pos and goal pos i jsut dont know how it will actuslyy move to do it and in end thingy i need to put set ohhh and im gonna make get and set methods and steal them from the drivetrain