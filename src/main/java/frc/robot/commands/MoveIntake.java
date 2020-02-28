// package frc.robot.commands;

// import edu.wpi.first.wpilibj.controller.PIDController;
// import edu.wpi.first.wpilibj2.command.PIDCommand;
// import frc.robot.Constants.AutoIntakeShuffleBoard;
// import frc.robot.subsystems.Intake;
// import frc.robot.subsystems.Intake.IntakePosition;

// public class MoveIntake extends PIDCommand {
//     private Intake m_intake;
//     private IntakePosition m_position;
//     private IntakePosition m_currentIntakePostion;

//     /**
//      * Moves the intake mechanism to a set position.
//      * 
//      * @param intake the intake subsystem used by the command
//      * @param position the position enum to move the mechanism towards
//      */
//     public MoveIntake(Intake intake, IntakePosition position) {
//         super(
//             new PIDController(AutoIntakeShuffleBoard.pEntry.getDouble(0.04), AutoIntakeShuffleBoard.iEntry.getDouble(0), AutoIntakeShuffleBoard.dEntry.getDouble(0)),
//             intake::getPitch,
//             position.getDesiredAngle(),
//             intake::setIntakeMotor,
//             intake);
//         getController().setSetpoint(position.getDesiredAngle());
//         getController().setTolerance(5.);

//         m_currentIntakePostion =  intake.getCurrentIntakePosition();

//         AutoIntakeShuffleBoard.currentAngle.setDouble(intake.getPitch());
//         AutoIntakeShuffleBoard.targetAngle.setDouble(0);
//         AutoIntakeShuffleBoard.isFinished.setBoolean(false);

//         addRequirements(intake);

//         m_intake = intake;
//         m_position = position;

//         m_intake.setIntakeMotorMaxOutput(.1);
//     }

//     @Override
//     public void initialize() {
//         super.initialize();
//         if (m_position == m_currentIntakePostion) cancel();
//     }

//     @Override
//     public void execute() {
//         super.execute();
//         AutoIntakeShuffleBoard.currentAngle.setDouble(m_measurement.getAsDouble());
//         AutoIntakeShuffleBoard.targetAngle.setDouble(m_position.getDesiredAngle());
//     }

//     @Override
//     public boolean isFinished() {
//         switch (m_position) {
//             case TOP:
//                 return m_intake.isTopTachPressed() || getController().atSetpoint();
//             case MIDDLE:
//                 return getController().atSetpoint();
//             case BOTTOM:
//                 return m_intake.isBottomTachPressed() || getController().atSetpoint();
//             default:
//                 return true;
//         }
//     }

//     @Override
//     public void end(boolean interrupted) {
//         super.end(interrupted);
//         AutoIntakeShuffleBoard.isFinished.setBoolean(true);
//         m_intake.setCurrentIntakePosition(m_position);
//     }
// }

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakePosition;

public class MoveIntake extends CommandBase {
    private final Intake m_intake;
    private IntakePosition m_intakePosition;
    
    /**
     * Manually adjusts intake position
     * 
     * @param intake the Intake subsystem used by the command
     * @param intakePosition the direction the intake will be moved
     */
    public MoveIntake(Intake intake, IntakePosition intakePosition) {
        m_intake = intake;
        m_intakePosition = intakePosition;
        addRequirements(m_intake);
    }

    @Override
    public void execute() {
        switch(m_intakePosition) {
            case TOP:
                m_intake.setIntakeMotorForward();
                break;
            case MIDDLE: 
                m_intake.setIntakeMotorReverse();
                break;
            case BOTTOM:
                m_intake.setIntakeMotorReverse();
                break;
        }

    }

    @Override
    public void end(boolean interrupted) {
        m_intake.stopIntakeMotor();
    }
    
}