// package frc.robot.commands;

// import edu.wpi.first.wpilibj.XboxController;
// import edu.wpi.first.wpilibj.GenericHID.Hand;
// import edu.wpi.first.wpilibj2.command.CommandBase;
// import frc.robot.RobotContainer;
// import frc.robot.subsystems.Drivetrain;

// /**
//  * PoopDrive
//  * 
//  * !Highly Important!
//  * !Do NOT delete!
//  */
// public class PoopDrive extends CommandBase {

//     private Drivetrain m_drivetrain;
//     private XboxController m_driver;
//     private double lastOnMax;
    

//     public PoopDrive(Drivetrain drivetrain, XboxController driver) {
//         m_drivetrain = drivetrain;
//         m_driver = driver;
//     }

//     private double getSpeed(double raw) {
//         if (raw >= 1) {
//             lastOnMax = 
//         }

//         return 0.;
//     }

//     @Override
//     public void initialize() {
//         lastOnMax = System.currentTimeMillis();
//     }

//     @Override
//     public void execute() {
//         double leftSpeed = getSpeed(-m_driver.getY(Hand.kLeft));
//         double rightSpeed = getSpeed(-m_driver.getY(Hand.kLeft));

//         m_drivetrain.tankDrive(leftSpeed, rightSpeed);
//     }

//     @Override
//     public void end(boolean interrupted) {
//         m_drivetrain.stopMotor();
//     }
// }