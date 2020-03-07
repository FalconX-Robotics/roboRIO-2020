package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.RollerState;

public class SetRollers extends CommandBase {
    private final Intake m_intake;
    private RollerState m_rollerState;

    /**
     * Sets the direction in which the intake subsystem motors are turning. Forward
     * for intake and reverse for outtake.
     * 
     * @param intake the intake subsystem used by the command
     * @param rollerState the direction enum in which to move the rollers
     */
    public SetRollers(Intake intake, RollerState rollerState) {
        m_intake = intake;
        m_rollerState = rollerState;
        addRequirements(m_intake);
    }

    // private static final ShuffleboardTab robotStatustab = Shuffleboard.getTab("Robot Status");
    // private static final ShuffleboardLayout intakeLayout = robotStatustab.getLayout("Intake", BuiltInLayouts.kList);
    // private final NetworkTableEntry rollerStateWidget = intakeLayout
        // .add("Roller State", "Stopped").getEntry();


    @Override
    public void execute() {
        switch(m_rollerState) {                               
            case INTAKE:
                m_intake.setRollerMotorReverse();
                // rollerStateWidget.setString("Intaking");
                break;
            case OUTTAKE:
                m_intake.setRollerMotorForward();
                // rollerStateWidget.setString("Outtakeing");
                break;
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_intake.stopRollerMotor();
        // rollerStateWidget.setString("Stopped");
    }
    
}