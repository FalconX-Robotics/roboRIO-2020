package frc.robot.commands.autopaths;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutoChooser;
import frc.robot.commands.AutoDrive;
import frc.robot.subsystems.Drivetrain;

public class QuickScore extends SequentialCommandGroup {
    //Required positions
    private double[] S0, E1;

    private boolean ferry;

    private Drivetrain m_drivetrain;

    public QuickScore(Drivetrain drivetrain, boolean ferry) {
        this.ferry = ferry;
        this.m_drivetrain = drivetrain;

        //get positions
        this.S0 = AutoChooser.S0;
        this.E1 = AutoChooser.E1;

        if(!this.ferry) {
            addCommands(
                new AutoDrive(m_drivetrain, AutoChooser.getDistance(S0, E1), .35)
            );
        }
    }
}