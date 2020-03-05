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
import frc.robot.subsystems.Intake.ArmPosition;

public class MoveIntakeArm extends CommandBase {
    private final Intake m_intake;
    private final ArmPosition m_targetPosition;

    private final double m_minimumOutput = 0.3;

    /**
     * Manually adjusts intake position
     * 
     * @param intake         the Intake subsystem used by the command
     * @param targetPosition the direction the intake will be moved
     */
    public MoveIntakeArm(final Intake intake, final ArmPosition targetPosition) {
        m_intake = intake;
        m_targetPosition = targetPosition;
        addRequirements(m_intake);
    }

    private void moveToMiddle() {
        double to = m_targetPosition.getTargetAngle();
        double speed = to - m_intake.getArmAngleInPercentage();
        if (speed > 0 && speed < m_minimumOutput) {
            speed = m_minimumOutput;
        } else if (speed < -m_minimumOutput) {
            speed = -m_minimumOutput;
        }

        m_intake.setArmMotor(speed);
    }


    @Override
    public void execute() {
        switch (m_targetPosition) {
        case TOP:
            if (m_intake.getTopSwitchPressed()) {
                m_intake.stopArmMotor();
                break;
            }
            m_intake.setArmMotorForward();
            break;
        case MIDDLE:
            moveToMiddle();
            break;
        case BOTTOM:
            if (m_intake.getBottomSwitchPressed()) {
                m_intake.stopArmMotor();
                break;
            }
            m_intake.setArmMotorReverse();
            break;
        }

    }

    @Override
    public void end(final boolean interrupted) {
        m_intake.stopArmMotor();
        m_intake.setArmCurrentPosition(m_targetPosition);
    }

    @Override
    public boolean isFinished() {
        return false;

        // switch(m_targetPosition) {
        //     case TOP:
        //         return m_intake.getTopSwitchPressed();
        //     case MIDDLE:
        //         return false;
        //         // return m_intake.getBottomSwitchPressed();
        //     case BOTTOM:
        //         return m_intake.getBottomSwitchPressed();
        //     default: 
        //         return true;
        // }
    }
    
}