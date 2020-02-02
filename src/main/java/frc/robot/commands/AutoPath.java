package frc.robot.commands;

import java.lang.Math;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutoDrive;
import frc.robot.subsystems.Drivetrain;

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
    public static final double[] E1 = {94.5, -120};
    public static final double[] E2 = {234, 146};

    private AutoDrive autoDriver;
    private Drivetrain m_drivetrain;

    private double yaw;

    public AutoPath(Drivetrain m_drivetrain) {
        this.autoDriver = new AutoDrive(m_drivetrain, 0, 0);
        yaw = m_drivetrain.getYaw();
    }
    
    //Returns the command associated with an enumerator
    public SequentialCommandGroup getPath(AutoPaths paths, boolean dispense) {
        switch (paths) {
            case QUICKSCORE:
                return quickScore(m_drivetrain, dispense);
            case TRENCHSCORE:
                // return trenchScore(m_drivetrain, dispense);
                break;
            case GENERATORSCORE:
                // return generatorScore(m_drivetrain, dispense);
                break;
            case YEET:
                // return yeet(m_drivetrain, dispense);
                break;
            case TRENCHSTEAL:
                // return trenchSteal(m_drivetrain, dispense);
        }
        return null;
    }

    //Auto path options
    public enum AutoPaths {
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
    // private SequentialCommandGroup turnAndMove(Drivet rain drivetrain, boolean ferry, double angle, double distance, double speed) {
    //     return new SequentialCommandGroup(new AutoTurn(m_drivetrain, getAngle(m_drivetrain.getYaw(), E1), .35), new AutoDrive(m_drivetrain, getDistance(S0, E1), .35));
    // }

    private SequentialCommandGroup quickScore(Drivetrain drivetrain, boolean ferry) {
        return new SequentialCommandGroup(new AutoDrive(m_drivetrain, getDistance(S0, E1), .35));        
    }

    // private SequentialCommandGroup trenchScore(Drivetrain drivetrain, boolean ferry) {
    //     return new SequentialCommandGroup();
    // }

    // private SequentialCommandGroup generatorScore(Drivetrain drivetrain, boolean ferry) {
    //     return new SequentialCommandGroup();
    // }

    // private SequentialCommandGroup yeet(Drivetrain drivetrain, boolean ferry) {
    //     return new SequentialCommandGroup();
    // }
    
    // private SequentialCommandGroup trenchSteal(Drivetrain drivetrain, boolean ferry) {
    //     return new SequentialCommandGroup();
    // }
}
