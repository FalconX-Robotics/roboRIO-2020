/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by  the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.AutoDriveShuffleBoard;
import frc.robot.Constants.AutoTurnShuffleBoard;
import frc.robot.Constants.Ports;
import frc.robot.commands.AutoDrive;
import frc.robot.commands.AutoPath;
import frc.robot.commands.AutoTurn;
import frc.robot.commands.MoveElevator;
import frc.robot.commands.MoveGondola;
import frc.robot.commands.MoveIntake;
import frc.robot.commands.SetRollers;
import frc.robot.commands.ToggleElevator;
import frc.robot.commands.AutoPath.AutoPaths;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Drivetrain.EncoderBrand;
import frc.robot.subsystems.Elevator.ElevatorDirection;
import frc.robot.subsystems.Intake.IntakePosition;
import frc.robot.subsystems.Intake.RollerState;

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
	private final Intake m_intake = new Intake();
	private final Elevator m_elevator = new Elevator();

	private static final ShuffleboardTab m_sensorInfoTab = Shuffleboard.getTab("Sensor Info");
	
	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {
		Constants.AutoDriveShuffleBoard.init();
		Constants.AutoTurnShuffleBoard.init();
		Constants.AutoIntakeShuffleBoard.init();

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
			() -> new AutoDrive(m_drivetrain, AutoDriveShuffleBoard.distance.getDouble(0.))
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
	private final XboxController m_driverTwo = new XboxController(Ports.XBOX_CONTROLLERTWO_PORT);

	/**
	 * Use this method to define your button->command mappings. Buttons can be
	 * created by instantiating a {@link GenericHID} or one of its subclasses
	 * ({@link edu.wpi.first.wpilibj.Joystick Joystick} or {@link XboxController}), and then
	 * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton JoystickButton}.
	 */
	private void configureButtonBindings() {
		// new JoystickButton(joystickDriver, 5).toggleWhenPressed(resetGyroCommand);
		
		//Driver two controls climb
		new JoystickButton(m_driverTwo, Button.kA.value).whenPressed(new ToggleElevator(m_elevator));

		new JoystickButton(m_driverTwo, Button.kX.value).whenHeld(new MoveElevator(m_elevator, ElevatorDirection.DOWN), false);
		new JoystickButton(m_driverTwo, Button.kY.value).whenHeld(new MoveElevator(m_elevator, ElevatorDirection.UP), false);

		new JoystickButton(m_driverTwo, Button.kBumperLeft.value).whenHeld(new MoveGondola(m_climber, .75));
		new JoystickButton(m_driverTwo, Button.kBumperRight.value).whenHeld(new MoveGondola(m_climber, -.75));

		//Driver one controls drivetrain and intake
		new JoystickButton(m_driver, Button.kX.value).whenPressed(new MoveIntake(m_intake, IntakePosition.BOTTOM));
		new JoystickButton(m_driver, Button.kY.value).whenPressed(new MoveIntake(m_intake, IntakePosition.TOP));
		
		new JoystickButton(m_driver, Button.kBumperLeft.value).whenHeld(new SetRollers(m_intake, RollerState.INTAKE), false);
		new JoystickButton(m_driver, Button.kBumperRight.value).whenHeld(new SetRollers(m_intake, RollerState.OUTTAKE), false);

		new POVButton(m_driver, 0).whenHeld(new MoveElevator(m_elevator, ElevatorDirection.UP));
		new POVButton(m_driver, 180).whenHeld(new MoveElevator(m_elevator, ElevatorDirection.DOWN));
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		// TODO add Networktable to change the auto command from shuffleboard
		return m_autoPaths.getPath(AutoPaths.TRENCHSCORE, false);
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
