/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by  the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.AutoDriveShuffleBoard;
import frc.robot.Constants.AutoTurnShuffleBoard;
import frc.robot.Constants.Ports;
import frc.robot.commands.AutoDrive;
import frc.robot.commands.AutoPath;
import frc.robot.commands.AutoTurn;
import frc.robot.commands.MoveGondola;
import frc.robot.commands.AutoPath.AutoPaths;
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
	private final AutoPath m_autoPaths = new AutoPath(m_drivetrain);
	private final Climber m_climber = new Climber();
	private final AutoDrive m_autoDrive = new AutoDrive(m_drivetrain, 12.0, 0.35);

	private static final ShuffleboardTab m_sensorInfoTab = Shuffleboard.getTab("Sensor Info");
	
	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {
		Constants.AutoDriveShuffleBoard.init();
		Constants.AutoTurnShuffleBoard.init();

		m_drivetrain.setDefaultCommand(new FunctionalCommand(() -> {
		},
				// () -> m_drivetrain.tankDrive(driver.getY(Hand.kLeft),
				// driver.getY(Hand.kRight), true),
				() -> {
					//m_drivetrain.arcadeDrive(-m_joystickDriver.getY(), m_joystickDriver.getZ());
					m_drivetrain.arcadeDrive(-m_driver.getY(Hand.kLeft), m_driver.getX(Hand.kRight), true);
					//m_drivetrain.setMaxOutput(1 - m_joystickDriver.getThrottle());
				},
				// (interrupted) -> m_drivetrain.tankDrive(0, 0),
				(interrupted) -> m_drivetrain.stopMotor(), () -> false, m_drivetrain));

		// m_climber.setDefaultCommand(new FunctionalCommand(
		// () -> {},
		// () -> {
		// m_climber.moveGondola(m_driver.getTriggerAxis(Hand.kRight)-m_driver.getTriggerAxis(Hand.kLeft));
		// },
		// (interrupted) -> m_climber.stopGondola(),
		// () -> false,
		// m_climber));

		InstantCommand resetGyroCommand = new InstantCommand(m_drivetrain::resetGyro, m_drivetrain);
		resetGyroCommand.setName("Reset gyro");
		Shuffleboard.getTab("Sensor Info").getLayout("Gyro").add("Resets gyro yaw", resetGyroCommand);

		InstantCommand resetEncoderCommand = new InstantCommand(() -> {
			m_drivetrain.resetEncoders(EncoderBrand.NEO);
			m_drivetrain.resetEncoders(EncoderBrand.SRX);
		}, m_drivetrain);
		resetEncoderCommand.setName("Reset Encoder");
	
		m_sensorInfoTab.getLayout("Encoder").add("Reset encoder", resetEncoderCommand);

		RunCommand autoDriveCommand = new RunCommand(
			() -> new AutoDrive(m_drivetrain, AutoDriveShuffleBoard.distance.getDouble(0.), AutoDriveShuffleBoard.speed.getDouble(0.))
					.schedule(),
			m_drivetrain);
		autoDriveCommand.setName("Auto Drive Command");
		Shuffleboard.getTab("Auto Drive").add("Auto drive", autoDriveCommand).withPosition(2, 3).withSize(2, 1);

		RunCommand autoTurnCommand = new RunCommand(
			() -> new AutoTurn(m_drivetrain, AutoTurnShuffleBoard.angle.getDouble(0.))
					.schedule(),
			m_drivetrain);
		autoTurnCommand.setName("Auto Turn Command");
		Shuffleboard.getTab("Auto Turn").add("Auto turn", autoTurnCommand).withPosition(2, 3).withSize(2, 1);

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
		// Toggles elevator pos on press of "a" button on Xbox controller
		// new JoystickButton(m_driver, XboxController.Button.kA.value).whenPressed(new
		// ToggleElevator(new Elevator()));

		new JoystickButton(m_driver, Button.kA.value).whenHeld(new MoveGondola(m_climber, .75));

		new JoystickButton(m_driver, Button.kB.value).whenHeld(new MoveGondola(m_climber, -.75));
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		// TODO add Networktable to change the auto command from shuffleboard
		return m_autoPaths.getPath(AutoPaths.QUICKSCORE, false);
		// return m_autoDrive;
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
