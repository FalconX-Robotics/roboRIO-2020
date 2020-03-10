/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by  the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.HashMap;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ProxyScheduleCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.Constants.Ports;
import frc.robot.commands.AutoDrive;
import frc.robot.commands.AutoPath;
import frc.robot.commands.AutoPath.AutoPaths;
import frc.robot.commands.AutoTurn;
import frc.robot.commands.Drive;
import frc.robot.commands.MoveElevator;
import frc.robot.commands.MoveGondola;
import frc.robot.commands.MoveIntakeArm;
import frc.robot.commands.SetRollers;
import frc.robot.commands.ToggleDrivetrainSpeed;
import frc.robot.commands.ToggleElevator;
import frc.robot.commands.ToggleQuickTurn;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Drivetrain.EncoderBrand;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Elevator.ElevatorDirection;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.ArmPosition;
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
	private final Intake m_intake = new Intake();
	private final AutoPath m_autoPaths = new AutoPath(m_drivetrain, m_intake);
	private final Climber m_climber = new Climber();
	private final Elevator m_elevator = new Elevator();

	// private static final ShuffleboardTab m_sensorInfoTab =
	// Shuffleboard.getTab("Sensor Info");

	private final XboxController m_driver = new XboxController(Ports.CONTROLLER_PORT);
	private DriveConsumerType m_consumerType = DriveConsumerType.kCurve;
	private DriveControllerType m_controllerType = DriveControllerType.kDualJoyStick;
	private DriveMod m_driveMod = DriveMod.kNormal;

	private final Joystick m_joystickDriverLeft = new Joystick(Ports.CONTROLLER_PORT);
	private final Joystick m_joystickDriverRight = new Joystick(Ports.CONTROLLERTWO_PORT);

	private static SendableChooser<AutoPaths> autoChooser;

	public enum DriveConsumerType {
		kTank, kArcade, kCurve;
	}

	private enum DriveControllerType {
		// except for dualJoyStick & xBox, all the other ones require xbox and tank drive
		kXbox, kDualJoyStick;
	}

	private enum DriveMod {
		kNormal, kStraight, kNoGoingBackwards, kNoGoingForwards,
		kCupcake, kCupcakeSSS, kReallyFast, kSlowAndSmooth, kSlowAndNotSmooth,
		kSine, kSineSSS, kCsc , kSqrt, kImaginary,
		kRandom, kNothing, kError;
	}

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {
		autoChooser = new SendableChooser<>();
		for (AutoPaths path : AutoPaths.values()) {
			if (path != AutoPaths.TEST && path != AutoPaths.QUICKSCORE) // QUICKSCORE as default
				autoChooser.addOption(path.name().toUpperCase(), path);
		}
		autoChooser.setDefaultOption("QUICKSCORE", AutoPaths.QUICKSCORE);
		Shuffleboard.getTab("Auto Path").add("Chooser", autoChooser).withWidget(BuiltInWidgets.kComboBoxChooser);

		final InstantCommand resetGyroCommand = new InstantCommand(m_drivetrain::resetGyro, m_drivetrain);
		resetGyroCommand.setName("Reset gyro");
		// Shuffleboard.getTab("Sensor Info").getLayout("Gyro").add("Resets gyro yaw",
		// resetGyroCommand);

		final InstantCommand resetEncoderCommand = new InstantCommand(() -> {
			m_drivetrain.resetEncoders(EncoderBrand.NEO);
			m_drivetrain.resetEncoders(EncoderBrand.SRX);
		}, m_drivetrain);
		resetEncoderCommand.setName("Reset Encoder");

		// m_sensorInfoTab.getLayout("Encoder").add("Reset encoder",
		// resetEncoderCommand);

		// AutoDrive autoDrive = new AutoDrive(m_drivetrain, 0);
		// final RunCommand autoDriveCommand = new RunCommand(() -> autoDrive.schedule(),
		// 		m_drivetrain);
		// autoDriveCommand.setName("Auto Drive Command");

		// AutoTurn autoTurn = new AutoTurn(m_drivetrain, 0);
		// final RunCommand autoTurnCommand = new RunCommand(() -> autoTurn.schedule(), m_drivetrain);
		// autoTurnCommand.setName("Auto Turn Command");

		configureDriveCommand(m_consumerType, m_controllerType, m_driveMod);

		for (DriveConsumerType consumer : DriveConsumerType.values()) {
			Shuffleboard.getTab("Drive").add(consumer.name(), new InstantCommand(() -> {
				configureDriveCommand(consumer, m_controllerType, m_driveMod);
				m_consumerType = consumer;
			}, m_drivetrain));
		}

		for (DriveControllerType controller : DriveControllerType.values()) {
			Shuffleboard.getTab("Drive").add(controller.name(), new InstantCommand(() -> {
				configureDriveCommand(m_consumerType, controller, m_driveMod);
				configureButtonBindings(controller);
				m_controllerType = controller;
			}, m_drivetrain));
		}

		for (DriveMod mod : DriveMod.values()) {
			Shuffleboard.getTab("Drive").add(mod.name(), new InstantCommand(() -> {
				configureDriveCommand(m_consumerType, m_controllerType, mod);
				m_driveMod = mod;
			}, m_drivetrain));
		}

		for (AutoPaths path : AutoPaths.values()) {
			Command command = m_autoPaths.getPath(path, false);
			if (command == null) {
				System.out.println(path.name() + " is null.");
				continue;
			}
			Shuffleboard.getTab("Auto Path").add(path.name(), new ProxyScheduleCommand(command));
		}

		Shuffleboard.getTab("Auto Path"
		).add("Auto Drive", new AutoDrive(m_drivetrain, 12));
		Shuffleboard.getTab("Auto Path"
		).add("Auto Turn", new AutoTurn(m_drivetrain, 45));
		Shuffleboard.getTab("Auto Path")
		.add("Move forward, and turn", new ProxyScheduleCommand(new SequentialCommandGroup(new AutoDrive(m_drivetrain, 36), new AutoTurn(m_drivetrain, 180))));

		// Configure the button bindings
		configureButtonBindings(m_controllerType);
	}

	// private final XboxController m_driverTwo = new
	// XboxController(Ports.XBOX_CONTROLLERTWO_PORT);

	/**
	 * Use this method to define your button->command mappings. Buttons can be
	 * created by instantiating a {@link GenericHID} or one of its subclasses
	 * ({@link edu.wpi.first.wpilibj.Joystick Joystick} or {@link XboxController}),
	 * and then passing it to a
	 * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton JoystickButton}.
	 */
	private void configureButtonBindings(DriveControllerType controllerType) {
		// new JoystickButton(joystickDriver, 5).toggleWhenPressed(resetGyroCommand);
		switch (controllerType) {
			case kDualJoyStick:
				configureJoystickBindings();
				break;
			default:
				configureXboxBindings();
		}
		
	}

	private void configureXboxBindings() {
		new JoystickButton(m_driver, Button.kA.value).whenPressed(new MoveIntakeArm(m_intake, ArmPosition.BOTTOM));
		new JoystickButton(m_driver, Button.kB.value).whenPressed(new ToggleDrivetrainSpeed(m_drivetrain, 0.05, 1.));
		new JoystickButton(m_driver, Button.kX.value).whenPressed(new ConditionalCommand(
				new MoveIntakeArm(m_intake, ArmPosition.TOP), new MoveIntakeArm(m_intake, ArmPosition.BOTTOM),
				() -> m_intake.getArmCurrentPosition() == Intake.ArmPosition.BOTTOM));
		new JoystickButton(m_driver, Button.kY.value).whenPressed(new MoveIntakeArm(m_intake, ArmPosition.TOP));

		new JoystickButton(m_driver, Button.kStart.value).whenPressed(new ToggleElevator(m_elevator));
		new POVButton(m_driver, 0).whenHeld(new MoveElevator(m_elevator, ElevatorDirection.UP), true);
		new POVButton(m_driver, 180).whenHeld(new MoveElevator(m_elevator, ElevatorDirection.DOWN), true);
		new JoystickButton(m_driver, Button.kBumperLeft.value).whenHeld(new SetRollers(m_intake, RollerState.OUTTAKE));
		new JoystickButton(m_driver, Button.kBumperRight.value).whenHeld(new SetRollers(m_intake, RollerState.INTAKE));
		new TriggerButton(m_driver, Hand.kLeft, 0.5).whenHeld(new MoveGondola(m_climber, .75));
		new TriggerButton(m_driver, Hand.kRight, 0.5).whenHeld(new MoveGondola(m_climber, -.75));

		new JoystickButton(m_driver, Button.kBack.value).whenPressed(new ToggleQuickTurn(m_drivetrain));
	}

	private void configureJoystickBindings() {
		new JoystickButton(m_joystickDriverLeft, 3).whenPressed(new MoveIntakeArm(m_intake, ArmPosition.BOTTOM));
		new JoystickButton(m_joystickDriverLeft, 2).whenPressed(new ToggleDrivetrainSpeed(m_drivetrain, 0.05, 1.));
		new JoystickButton(m_joystickDriverLeft, 4).whenPressed(new ConditionalCommand(
				new MoveIntakeArm(m_intake, ArmPosition.TOP), new MoveIntakeArm(m_intake, ArmPosition.BOTTOM),
				() -> m_intake.getArmCurrentPosition() == Intake.ArmPosition.BOTTOM));
		new JoystickButton(m_joystickDriverLeft, 5).whenPressed(new MoveIntakeArm(m_intake, ArmPosition.TOP));

		new JoystickButton(m_joystickDriverRight, 3).whenPressed(new ToggleElevator(m_elevator));
		new JoystickButton(m_joystickDriverRight, 6).whenHeld(new MoveElevator(m_elevator, ElevatorDirection.UP), true);
		new JoystickButton(m_joystickDriverRight, 4).whenHeld(new MoveElevator(m_elevator, ElevatorDirection.DOWN), true);
		new JoystickButton(m_joystickDriverRight, 1).whenHeld(new SetRollers(m_intake, RollerState.OUTTAKE));
		new JoystickButton(m_joystickDriverLeft, 1).whenHeld(new SetRollers(m_intake, RollerState.INTAKE));
		new JoystickButton(m_joystickDriverLeft, 6).whenHeld(new MoveGondola(m_climber, .75));
		new JoystickButton(m_joystickDriverRight, 5).whenHeld(new MoveGondola(m_climber, -.75));

		new JoystickButton(m_joystickDriverRight, 2).whenPressed(new ToggleQuickTurn(m_drivetrain));
	}

	HashMap<String, Double> continuousData = new HashMap<>();
	private double continuous(String id, double input) {
		if (input > .05) {
			continuousData.put(id, continuousData.getOrDefault(id, 0.)+(1./50.));
		} else if (input < .05) {
			continuousData.put(id, continuousData.getOrDefault(id, 0.)-(1./50.));
		} else {
			return 0.;
		}
		return continuousData.get(id);
	}

	private double threshold(double input) {
		return input <= .1 || input >= .1 ? input : 0;
	}

	private void configureDriveCommand(final DriveConsumerType driveMode, final DriveControllerType controllerType, final DriveMod driveMod) {
		final Drive drive = new Drive(m_drivetrain, driveMode);
		switch (controllerType) {
		case kXbox:
			drive.setHIDs(m_driver, m_driver);
			break;
		case kDualJoyStick:
			drive.setHIDs(m_joystickDriverLeft, m_joystickDriverRight);
			break;
		}

		switch (driveMod) {
		case kStraight:
			drive.setMods((x, y) -> x, (x, y) -> x);
			break;
		case kNoGoingBackwards:
			drive.setMods((x, y) -> Math.abs(x), (x, y) -> Math.abs(y));
			break;
		case kNoGoingForwards:
			drive.setMods((x, y) -> -Math.abs(x), (x, y) -> -Math.abs(y));
			break;
		case kCupcake:
			drive.setMods((x, y) -> x, (x, y) -> 0.);
			break;
		case kCupcakeSSS:
			drive.setMods((x, y) -> x, (x, y) -> -x);
			break;
		case kReallyFast:
			drive.setMods((x, y) -> Math.signum(threshold(x)), (x, y) -> Math.signum(threshold(y)));
			break;
		case kSlowAndSmooth:
			drive.setMods((x, y) -> Math.pow(x, 3)/4, (x, y) -> Math.pow(y, 3)/4);
			break;
		case kSlowAndNotSmooth:
			drive.setMods((x, y) -> Math.floor(3.*continuous("slowAndNotSmoothX", x))/3., (x, y) -> Math.floor(3.*continuous("slowAndNotSmoothY", y))/3.);
			break;
		case kSine:
			drive.setMods((x, y) -> Math.sin(Math.PI * 2 * x) / 2, (x, y) -> Math.sin(Math.PI * 2 * y) / 2);
			break;
		case kSineSSS:
			drive.setMods((x, y) -> Math.sin(Math.PI * continuous("sineSSSX", x)) / 2, (x, y) -> Math.sin(Math.PI * continuous("sineSSSY", y)) / 2);
			break;
		case kCsc:
			drive.setMods((x, y) -> 1/(5*Math.sin(x)), (x, y) -> 1/(5*Math.sin(y)));
			break;
		case kSqrt:
			drive.setMods((x, y) -> Math.sqrt(Math.abs(x))*Math.signum(x), (x, y) -> Math.sqrt(Math.abs(y))*Math.signum(y));
			break;
		case kImaginary:
			drive.setMods((x, y) -> 0., (x, y) -> 0.);
			break;
		case kRandom:
			drive.setMods((x, y) -> Math.signum(threshold(x)) * Math.random() / 3, (x, y) -> Math.signum(threshold(y)) * Math.random() / 3);
			break;
		case kError:
			throw new RuntimeException("Error");
		default:
		}
		if (m_drivetrain.getDefaultCommand() != null)
			m_drivetrain.getDefaultCommand().end(true);
		m_drivetrain.setDefaultCommand(drive);
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		return m_autoPaths.getPath(autoChooser.getSelected(), false);
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
