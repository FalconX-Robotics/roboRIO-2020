/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by  the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants.Ports;
import frc.robot.commands.AutoChooser;
import frc.robot.commands.AutoChooser.AutoPath;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Drivetrain.EncoderBrand;

/**
* This class is where the bulk of the robot should be declared. Since
* Command-based is a "declarative" paradigm, very little robot logic should
* actually be handled in the {@link Robot} periodic methods (other than the
* scheduler calls). Instead, the structure of the robot (including subsystems,
* commands, and button mappings) should be declared here.
*/
public class RobotContainer {
	// The robot's subsystems and commands are defined here...
	private final Drivetrain m_drivetrain = new Drivetrain(Drivetrain.EncoderBrand.NEO);
	private final AutoChooser m_autoChooser = new AutoChooser(m_drivetrain);
	private final Climber m_climber = new Climber();

	/**
	* The container for the robot. Contains subsystems, OI devices, and commands.
	*/
	public RobotContainer() {
		m_drivetrain.setDefaultCommand(new FunctionalCommand(
				() -> {},
				// () -> m_drivetrain.tankDrive(driver.getY(Hand.kLeft), driver.getY(Hand.kRight)), 
				() -> {
					m_drivetrain.arcadeDrive(-m_joystickDriver.getY(), m_joystickDriver.getZ());
					m_drivetrain.setMaxOutput(1-m_joystickDriver.getThrottle());
				},
				// (interrupted) -> m_drivetrain.tankDrive(0, 0),
				(interrupted) -> m_drivetrain.stopMotor(),
				() -> false,
				m_drivetrain));

		m_climber.setDefaultCommand(new FunctionalCommand(
			() -> {},
			() -> {
				m_climber.moveGondola(m_driver.getTriggerAxis(Hand.kRight)-m_driver.getTriggerAxis(Hand.kLeft));
			},
			(interrupted) -> m_climber.stopGondola(),
			() -> false,
			m_climber));

		InstantCommand resetGyroCommand = new InstantCommand(m_drivetrain::resetGyro, m_drivetrain);
		resetGyroCommand.setName("Reset gyro");
		Shuffleboard.getTab("Sensor Info").getLayout("Gyro").add("Resets gyro yaw", resetGyroCommand);

		InstantCommand resetEncoderCommand = new InstantCommand(
			() -> {
				m_drivetrain.resetEncoders(EncoderBrand.NEO);
				m_drivetrain.resetEncoders(EncoderBrand.SRX);
			}, m_drivetrain);
		resetEncoderCommand.setName("Reset Encoder");
		Shuffleboard.getTab("Sensor Info").getLayout("Encoder").add("Reset encoder", resetEncoderCommand);

		// Configure the button bindings
		configureButtonBindings();
	}
	
	private final XboxController m_driver = new XboxController(Ports.XBOX_CONTROLLER_PORT);
	private final Joystick m_joystickDriver = new Joystick(Ports.XBOX_CONTROLLER_PORT);
	
	/**
	* Use this method to define your button->command mappings. Buttons can be
	* created by instantiating a {@link GenericHID} or one of its subclasses
	* ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
	* passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
	*/
	private void configureButtonBindings() {
		// new JoystickButton(joystickDriver, 5).toggleWhenPressed(resetGyroCommand);
	}
	
	/**
	* Use this to pass the autonomous command to the main {@link Robot} class.
	*
	* @return the command to run in autonomous
	*/
	public Command getAutonomousCommand() {
		//TODO add Networktable to change the auto command from shuffleboard
		return m_autoChooser.getCommand(AutoPath.QUICKSCORE, false);
	}

	/**
	 * Resets gyro yaw; neo and srx encoders.
	 */
	public void resetSensors() {
		m_drivetrain.resetEncoders(EncoderBrand.NEO);
		m_drivetrain.resetEncoders(EncoderBrand.SRX);
		m_drivetrain.resetGyro();
	}
}
