package frc.robot.commands.autopaths;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutoChooser;
import frc.robot.commands.AutoDrive;
import frc.robot.subsystems.Drivetrain;

public class Yeet extends SequentialCommandGroup {
    //Required positions
    private double[] S2, E2;

    private boolean ferry;

    private Drivetrain m_drivetrain;

    public Yeet(Drivetrain drivetrain, boolean ferry) {
        this.ferry = ferry;
        this.m_drivetrain = drivetrain;

        //get positions
        this.S2 = AutoChooser.S2;
        this.E2 = AutoChooser.E2;
    }
}