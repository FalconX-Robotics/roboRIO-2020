package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Ports;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Intake extends SubsystemBase {
    
    private final CANSparkMax m_rollerMotor = new CANSparkMax(Ports.ROLLER_MOTOR_PORT, MotorType.kBrushless); //may be actually brushed not sure rn
    private final CANSparkMax m_intakeMechanismMotor = new CANSparkMax(Ports.INTAKE_MECHANISM_MOTOR_PORT, MotorType.kBrushless); //^^f

    public void moveUp(final int degrees) { 
    }

    public void moveDown(final int degrees) {
        
    }

    public void runRollers(final int rotations) {

    }

    // ^^ idk if those are the right arguments sorry 
}