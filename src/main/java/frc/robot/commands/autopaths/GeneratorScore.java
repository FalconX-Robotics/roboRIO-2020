package frc.robot.commands.autopaths;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutoChooser;
import frc.robot.commands.AutoDrive;
import frc.robot.subsystems.Drivetrain;

public class GeneratorScore extends SequentialCommandGroup {
    //Required positions
    private double[] S0, S1, B5, B6, E1;

    private boolean ferry;

    private Drivetrain m_drivetrain;

    public GeneratorScore(Drivetrain drivetrain, boolean ferry) {
        this.ferry = ferry;
        this.m_drivetrain = drivetrain;

        //get positions
        this.S0 = AutoChooser.S0;
        this.S1 = AutoChooser.S1;
        this.B5 = AutoChooser.B5;
        this.B6 = AutoChooser.B6;
        this.E1 = AutoChooser.E1;
    }
}