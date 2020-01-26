package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

public class AutoSequence {
    /*  This is a list of the coordinates of all (guaranteed) balls and starting/ending
        postions on our side of the field, where the origion is placed on the left-most
        point of our initiation line. The points are listed from left-right on the field. */

    //Robot starting positions
    public final double[] S0 = {94.5, 0};
    public final double[] S1 = {160.3, 0};
    public final double[] S2 = {234, 0};
    public final double[] S3 = {304.7, 0};

    //Ball positions
    public final double[] B0 = {27.6, 122.6};
    public final double[] B1 = {27.6, 158.6};
    public final double[] B2 = {27.6, 194.6};
    public final double[] B3 = {214.3, 130.3};
    public final double[] B4 = {120.6, 115};
    public final double[] B5 = {145, 107.8};
    public final double[] B6 = {160.3, 114.2};
    public final double[] B7 = {175.6, 120.5};
    public final double[] B8 = {286.2, 130.4};
    public final double[] B9 = {304.7, 130.4};

    //Robot ending positions
    public final double[] E1 = {94.5, -120};
    public final double[] E2 = {234, 146};

    public AutoSequence() {
    }
    
    public Command getCommand(AutoPath path, boolean dispense) {
        switch (path) {
            case QUICKSCORE:
                break;
            case TRENCHSCORE:
                break;
            case GENERATORSCORE:
                break;
            case YEET:
                break;
            case TRENCHSTEAL:
        }
        return null;
    }

    //Auto path options
    public enum AutoPath {
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
}
