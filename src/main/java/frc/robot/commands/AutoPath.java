package frc.robot.commands;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;

public class AutoPath {
    /**
     * This is a list of the coordinates of all (guaranteed) balls and starting/ending
     * postions on our side of the field, where the origion is placed on the left-most
     * point of our initiation line. The points are listed from left-right on the field.
     */

    //Robot starting positions
    public static final double[] S0 = {94.5, 0};
    public static final double[] S1 = {160.3, 0};
    public static final double[] S2 = {234, 0};
    public static final double[] S3 = {304.7, 0};

    //Ball positions
    public static final double[] B0 = {27.6, 122.6};
    public static final double[] B1 = {27.6, 158.6};
    public static final double[] B2 = {27.6, 194.6};
    public static final double[] B3 = {214.3, 130.3};
    public static final double[] B4 = {120.6, 115};
    public static final double[] B5 = {145, 107.8};
    public static final double[] B6 = {160.3, 114.2};
    public static final double[] B7 = {175.6, 120.5};
    public static final double[] B8 = {286.2, 130.4};
    public static final double[] B9 = {304.7, 130.4};

    //Robot ending positions
    public static final double[] preE0 = {94.5, -90};
    public static final double[] E0 = {94.5, -120 + 32.31/2};
    public static final double[] E1 = {234, 146};

    //Pathweaver trajectories
    public static final String S0B0 = "paths/S0B0.wpilib.json";

    private Drivetrain m_drivetrain;

    //TODO: Actually use this for something
    private boolean ferry;

    //Auto path options
    public enum AutoPaths {
        //Basic routes for tuning, testing trajectories, etc.
        TEST,

        //Go straight to the lower port and dispense preloaded power cells
        //Points: 11
        QUICKSCORE,

        //The previous, but moving to the trench first and collecting cells 0-1
        //Points: 15
        TRENCHSCORE,

        //Move forward to the shield generator and collect 2 balls off the edge
        //Points: 15
        GENERATORSCORE,

        //Move to the other side of the field to be in position to defend
        //Points: 5
        YEET,

        //Steal balls 8-9 from the opponents control panel and score to the lower port
        //Points: 15
        TRENCHSTEAL,
        
        //Drives straight for 10 ft
        DRIVE_STRAIGHT_FOR_10_FT;
    }
    /**
     * Constructor.
     */
    public AutoPath(Drivetrain drivetrain) {
        this.m_drivetrain = drivetrain;
    }
    
    /**
     * Uses the enumerator chosen in RobotContainer to decide which path is used
     * during the autonomous phase.
     * 
     * @param path the route chosen for auto
     * @param ferry whether or not to ferry power cells to another robot at lower port
     * @return the SequentialCommandGroup that will follow the chosen route when executed
     */
    public Command getPath(AutoPaths path, boolean ferry) {
        this.ferry = ferry;
        Command command;
        switch (path) {
            case QUICKSCORE:
                command = quickScore(ferry);
                break;
            case TRENCHSCORE:
                command = trenchScore(ferry);
                break;
            case GENERATORSCORE:
                command = generatorScore(ferry);
                break;
            case YEET:
                command = yeet(ferry);
                break;
            case TRENCHSTEAL:
                command = trenchSteal(ferry);
                break;
            case DRIVE_STRAIGHT_FOR_10_FT:
                command = new AutoDrive(m_drivetrain, 10*12);
            default:
                return null;
        }
        return new SequentialCommandGroup(
            new UpdateShuffleboardCommand(true),
            command,
            new UpdateShuffleboardCommand(false));
    }

    /**
     * Static calculator method that finds the angle between two points on the field, relative 
     * to the robot.
     * 
     * @param currentAngle the current angle of the robot on the field (m_drivetrain.getYaw())
     * @param pointA the current position of the robot
     * @param pointB where the robot will move to next
     * @return the angle by which to turn the robot
     */
    public static double getAngle(double currentAngle, double[] pointA, double[] pointB) {
        double delX = pointB[0]-pointA[0];
        double delY = pointB[1]-pointA[1];

        double angle = Math.atan2(delY, delX)*180/3.14159265358979 - 90 - currentAngle;
        if(angle < -180) {
            angle += 360;
        }
        return angle;
    }

    /**
     * Another static calculator method to find the distance between two points on the field.
     * 
     * @param pointA the current position of the robot
     * @param pointB where the robot will move to next
     * @return the distance to move the robot forward
     */
    public static double getDistance(double[] pointA, double[] pointB) {
        double delX = pointB[0]-pointA[0];
        double delY = pointB[1]-pointA[1];

        return Math.sqrt(delX * delX + delY * delY);
    }

    /**
     * Outputs a RamseteCommand that follows a given trajectory during autonomous.
    */
    private RamseteCommand RamseteTrenchScore(boolean ferry) {
        //converts a PathWeaver path json from deploy into a trajectory
        Trajectory trajectory = null;
        try {
            Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(S0B0);
            trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        } catch(IOException ex) {
            DriverStation.reportError("Unable to open trajectory: " + S0B0, ex.getStackTrace());
        }

        //Creates the RamseteCommand to follow this trajectory
        RamseteCommand ramseteCommand = new RamseteCommand(
            trajectory,
            this.m_drivetrain::getPose,
            new RamseteController(AutoConstants.kRamseteB, AutoConstants.kRamseteZeta),
            new SimpleMotorFeedforward(DriveConstants.ksVolts,
                                    DriveConstants.kvVoltSecondsPerMeter,
                                    DriveConstants.kaVoltSecondsSquaredPerMeter),
            DriveConstants.kDriveKinematics,
            this.m_drivetrain::getWheelSpeeds,
            new PIDController(AutoConstants.L_RAM_kP, AutoConstants.L_RAM_kI, AutoConstants.L_RAM_kD),
            new PIDController(AutoConstants.R_RAM_kP, AutoConstants.R_RAM_kI, AutoConstants.R_RAM_kD),
            // RamseteCommand passes volts to the callback
            this.m_drivetrain::setTankDriveVolt,
            this.m_drivetrain
        );
        return ramseteCommand;  
    }

    /**
     * A consolidated method that makes it easier to change AutoPaths, only requiring
     * two points. It will only add an AutoTurn command if needed.
     */
    private SequentialCommandGroup turnAndMove(double[] pointA, double[] pointB) {
        if(Math.abs(getAngle(this.m_drivetrain.getYaw(), pointA, pointB)) < 0.05) {
            return new SequentialCommandGroup(
                new AutoDrive(this.m_drivetrain, getDistance(pointA, pointB))
            );
        }
        else {
            return new SequentialCommandGroup(
                new AutoTurn(this.m_drivetrain, getAngle(this.m_drivetrain.getYaw(), pointA, pointB)),
                new AutoDrive(this.m_drivetrain, getDistance(pointA, pointB))
            );
        }
    }

    private class UpdateShuffleboardCommand extends InstantCommand {
        private boolean m_start;
        
        public UpdateShuffleboardCommand(boolean start) {
            m_start = start;
        }

        @Override
        public void initialize() {
            RobotContainer.isRunningEntry.setBoolean(m_start);
        }
    }

    /**
     * Assembles all of the commands together into the desired path sequence
     */
    private SequentialCommandGroup quickScore(boolean ferry) {
        return new SequentialCommandGroup(
            turnAndMove(S0, E0)
        );
    }
    private SequentialCommandGroup trenchScore(boolean ferry) {
        return new SequentialCommandGroup(
            turnAndMove(S0, B0),
            turnAndMove(B0, B2),
            turnAndMove(B2, preE0),
            turnAndMove(preE0, E0)
        );
    }
    private SequentialCommandGroup generatorScore(boolean ferry) {
        return new SequentialCommandGroup(
            turnAndMove(S1, B6),
            turnAndMove(B6, B5),
            turnAndMove(B5, preE0),
            turnAndMove(preE0, E0)
        );
    }
    private SequentialCommandGroup yeet(boolean ferry) {
        return new SequentialCommandGroup(
            turnAndMove(S2, E1)
        );
    }
    private SequentialCommandGroup trenchSteal(boolean ferry) {
        return new SequentialCommandGroup(
            turnAndMove(S3, B9),
            turnAndMove(B9, B8),
            turnAndMove(B8, preE0),
            turnAndMove(preE0, E0)
        );
    }
}
