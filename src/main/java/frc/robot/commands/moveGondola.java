package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class moveGondola extends CommandBase {
    private double m_motorSpeed = 0;
    private Climber m_climber;
    public moveGondola(Climber climber, double motorSpeed) {
        m_motorSpeed = motorSpeed;
        m_climber = climber;
    }

    @Override
    public void execute() {
        m_climber.moveGondola(m_motorSpeed);
    }
    
}