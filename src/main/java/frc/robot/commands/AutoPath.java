package frc.robot.commands;

import java.io.IOException;
import java.lang.Math;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.commands.AutoDrive;
import frc.robot.subsystems.Drivetrain;
import frc.robot.Constants;
import frc.robot.Constants.RamseteConstants;

import java.nio.file.Path;
import java.nio.file.FileSystem;
import edu.wpi.first.wpilibj.Filesystem;

import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.controller.PIDController;

public class AutoPath {
    /*  This is a list of the coordinates of all (guaranteed) balls and starting/ending
        postions on our side of the field, where the origion is placed on the left-most
        point of our initiation line. The points are listed from left-right on the field. */

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
    public static final double[] E0 = {94.5, -120};
    public static final double[] E1 = {234, 146};

    //Pathweaver trajectories
    public static final String S0B0 = "paths/S0B0.wpilib.json";

    private Drivetrain m_drivetrain;

    private double yaw;

    public AutoPath(Drivetrain drivetrain) {
        m_drivetrain = drivetrain;
        yaw = m_drivetrain.getYaw();
    }
    
    //Returns the command associated with an enumerator
    public SequentialCommandGroup getPath(AutoPaths paths, boolean ferry) {
        switch (paths) {
            case TEST:
                // return testScore(m_drivetrain, ferry);
                break;
            case QUICKSCORE:
                return quickScore(m_drivetrain, ferry);
            case TRENCHSCORE:
                return trenchScore(m_drivetrain, ferry);
            case GENERATORSCORE:
                // return generatorScore(m_drivetrain, ferry);
                break;
            case YEET:
                return yeet(m_drivetrain, ferry);
            case TRENCHSTEAL:
                return trenchSteal(m_drivetrain, ferry);
        }
        return null;
    }

    //Auto path options
    public static enum AutoPaths {
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
        TRENCHSTEAL;
    }

    //basic math/trig functions
    public static double getAngle(double currentAngle, double[] pointA, double[] pointB) {
        double delX = pointB[0]-pointA[0];
        double delY = pointB[1]-pointA[1];

        double angle;
        if(delY > 0) {
            angle = 90 - Math.atan(delY/delX)*180/3.14159265358979 - currentAngle;
        }
        else {
            angle = 0-90 - Math.atan(delY/delX)*180/3.14159265358979 - currentAngle;
        }
        if(angle > 180) {
            return angle - 360;
        }
        else if(angle < 180) {
            return angle + 360;
        }
        return angle;
    }
    public static double getDistance(double[] pointA, double[] pointB) {
        double delX = pointB[0]-pointA[0];
        double delY = pointB[1]-pointA[1];

        return Math.sqrt(delX * delX + delY * delY);
    }
    /*
        This is WHACK
        don't use it at scrims
    */
    // private SequentialCommandGroup testScore(Drivetrain drivetrain, boolean ferry) {
    //     Trajectory trajectory = null;
    //     try {
    //         Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(S0B0);
    //         trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
    //     } catch(IOException ex) {
    //         DriverStation.reportError("Unable to open trajectory: " + S0B0, ex.getStackTrace());
    //     }
    //     //stuff

    //     RamseteCommand ramseteCommand = new RamseteCommand(
    //         trajectory,
    //         m_drivetrain::getPose,
    //         new RamseteController(RamseteConstants.kRamseteB, RamseteConstants.kRamseteZeta),
    //         new SimpleMotorFeedforward(RamseteConstants.ksVolts,
    //                                 RamseteConstants.kvVoltSecondsPerMeter,
    //                                 RamseteConstants.kaVoltSecondsSquaredPerMeter),
    //         RamseteConstants.kDriveKinematics,
    //         m_drivetrain::getWheelSpeeds,
    //         new PIDController(0.4, 0.5, 0),
    //         new PIDController(0.4, 0.5, 0),
    //         // RamseteCommand passes volts to the callback
    //         m_drivetrain::tankDriveVolts,
    //         m_drivetrain
    //     );
    //     return new SequentialCommandGroup(new AutoDrive(m_drivetrain, getDistance(S0, E1), .35));  
    // }

    private SequentialCommandGroup turnAndMove(Drivetrain drivetrain, boolean ferry, double angle, double distance, double speed) {
        return new SequentialCommandGroup(
            new AutoTurn(m_drivetrain, getAngle(m_drivetrain.getYaw(), S0, E1)),
            new AutoDrive(m_drivetrain, getDistance(S0, E1), .35)
            );
    }

    private SequentialCommandGroup quickScore(Drivetrain drivetrain, boolean ferry) {
        return new SequentialCommandGroup(new AutoDrive(m_drivetrain, getDistance(S0, E1), .35));        
    }

    private SequentialCommandGroup trenchScore(Drivetrain drivetrain, boolean ferry) {
        return new SequentialCommandGroup(
            new AutoTurn(m_drivetrain, getAngle(m_drivetrain.getYaw(), S0, B0)),
            new AutoDrive(m_drivetrain, getDistance(S0, B0), .35),
            new AutoTurn(m_drivetrain, getAngle(m_drivetrain.getYaw(), B0, B1)),
            new AutoDrive(m_drivetrain, getDistance(B0, B2), .35),
            new AutoTurn(m_drivetrain, getAngle(m_drivetrain.getYaw(), B2, E0)),
            new AutoDrive(m_drivetrain, getDistance(B2, E0)-12, .35)
        );
    }

    private SequentialCommandGroup generatorScore(Drivetrain drivetrain, boolean ferry) {
        return new SequentialCommandGroup();
    }

    private SequentialCommandGroup yeet(Drivetrain drivetrain, boolean ferry) {
        return new SequentialCommandGroup(new AutoDrive(m_drivetrain, getDistance(S2, E1)-12, .35));
    }
    
    private SequentialCommandGroup trenchSteal(Drivetrain drivetrain, boolean ferry) {
        return new SequentialCommandGroup(
            new AutoDrive(m_drivetrain, getDistance(S3, B9), .35),
            new AutoTurn(m_drivetrain, getAngle(m_drivetrain.getYaw(), B9, B8)),
            new AutoDrive(m_drivetrain, getDistance(B9, B8), .35),
            new AutoTurn(m_drivetrain, getAngle(m_drivetrain.getYaw(), B8, E0)),
            new AutoDrive(m_drivetrain, getDistance(B8, E0)-12, .35)
        );
    }
}
