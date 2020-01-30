package frc.robot.commands.autopaths;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutoChooser;
import frc.robot.commands.AutoDrive;
import frc.robot.subsystems.Drivetrain;

public class TrenchScore extends SequentialCommandGroup {
    //Required positions
    private double[] S0, B0, B1, B2, E1;

    private boolean ferry;

    private Drivetrain m_drivetrain;

    public TrenchScore(Drivetrain drivetrain, boolean ferry) {
        this.ferry = ferry;
        this.m_drivetrain = drivetrain;

        //get positions
        this.S0 = AutoChooser.S0;
        this.B0 = AutoChooser.B0;
        this.B1 = AutoChooser.B1;
        this.B2 = AutoChooser.B2;
        this.E1 = AutoChooser.E1;
    }
}