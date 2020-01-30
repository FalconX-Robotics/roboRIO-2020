package frc.robot.commands.autopaths;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutoChooser;
import frc.robot.commands.AutoDrive;
import frc.robot.subsystems.Drivetrain;

public class TrenchSteal extends SequentialCommandGroup {
    //Required positions
    private double[] S0, S3, B8, B9, E1;

    private boolean ferry;

    private Drivetrain m_drivetrain;

    public TrenchSteal(Drivetrain drivetrain, boolean ferry) {
        this.ferry = ferry;
        this.m_drivetrain = drivetrain;

        //get positions
        this.S0 = AutoChooser.S0;
        this.S3 = AutoChooser.S3;
        this.B8 = AutoChooser.B8;
        this.B9 = AutoChooser.B9;
        this.E1 = AutoChooser.E1;

    }
}