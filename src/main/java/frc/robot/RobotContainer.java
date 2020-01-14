/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by  the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.commands.AutoDriveForward;
import frc.robot.subsystems.Drivetrain;

import static frc.robot.Constants.Ports;

/**
* This class is where the bulk of the robot should be declared. Since
* Command-based is a "declarative" paradigm, very little robot logic should
* actually be handled in the {@link Robot} periodic methods (other than the
* scheduler calls). Instead, the structure of the robot (including subsystems,
* commands, and button mappings) should be declared here.
*/
public class RobotContainer {
	// The robot's subsystems and commands are defined here...
	private final Drivetrain m_drivetrain = new Drivetrain();
	private final AutoDriveForward m_autoDriveForward = new AutoDriveForward(m_drivetrain);
	
	/**
	* The container for the robot. Contains subsystems, OI devices, and commands.
	*/
	public RobotContainer() {
		m_drivetrain.setDefaultCommand(new FunctionalCommand(
				() -> {},
				//TODO remove
				() -> m_drivetrain.tankDrive(driver.getY(Hand.kLeft), driver.getY(Hand.kLeft)), 
				(interrupted) -> m_drivetrain.tankDrive(0, 0),
				() -> false,
				m_drivetrain));

		// Configure the button bindings
		configureButtonBindings();
	}
	
	private final XboxController driver = new XboxController(Ports.XBOX_CONTROLLER_PORT);
	
	/**
	* Use this method to define your button->command mappings. Buttons can be
	* created by instantiating a {@link GenericHID} or one of its subclasses
	* ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
	* passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
	*/
	private void configureButtonBindings() {
		
	}
	
	/**
	* Use this to pass the autonomous command to the main {@link Robot} class.
	*
	* @return the command to run in autonomous
	*/
	public Command getAutonomousCommand() {
		// An ExampleCommand will run in autonomous
		return m_autoDriveForward;
	}
}
