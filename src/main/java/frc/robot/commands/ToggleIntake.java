// package frc.robot.commands;

// import edu.wpi.first.wpilibj2.command.CommandBase;
// import frc.robot.subsystems.Intake;

// public class ToggleIntake extends CommandBase {
//     private final Intake m_intake;

//     /**
//      * Toggles the arm position
//      * 
//      * @param intake the Intake subsystem used by the command
//      */
//     public ToggleIntake(Intake intake) {
//         m_intake = intake;
//         addRequirements(m_intake);
//     }

//     @Override
//     public void execute() { 
//         switch(m_intake.getCurrentIntakePosition()) {
//             case BOTTOM:
//                 m_intake.setIntakeMotorForward();
//                 break;
//             case MIDDLE:
//                 m_intake.setIntakeMotorReverse();
//                 break;
//             case TOP:
//                 m_intake.setIntakeMotorReverse();
//                 break;
//         }
//     }
        

//     @Override
//     public boolean isFinished() {
//         switch(m_intake.getCurrentIntakePosition()) {
//             case BOTTOM:
//                 return m_intake.isTopTachPressed();
//             case TOP:
//                 return m_intake.isBottomTachPressed();
//             default:
//                 return true;
//         }
//     }

//     @Override
//     public void end(boolean iterrupted) {
//         m_intake.stopIntakeMotor();

//         m_intake.setCurrentIntakePosition(IntakePosition.);
//     }
// }
