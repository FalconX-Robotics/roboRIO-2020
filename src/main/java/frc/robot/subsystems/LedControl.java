package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LedControl extends SubsystemBase {
    public LedControl() {

    }

    private PWMSparkMax ledController = new PWMSparkMax(Constants.Ports.LED_CONTROLLER_PORT);

    public enum Pattern {
        kNone(0.0), kViolet(0.91), kRed(0.61), kBreathBlue(-0.15), kRainbow(-0.99), kBlue(0.87);

        private Double speed;

        private Pattern(Double speed) {
            this.speed = speed;
        }

        public Double getSpeed() {
            return this.speed;
        }
    }

    /**
     * Sets the led pattern according to values from: <p>
     * www.revrobotics.com/content/docs/REV-11-1105-UM.pdf
     * @param pattern the pattern to set the led to
     */ 
    public void setLed(Pattern pattern) {
        ledController.setSpeed(pattern.getSpeed());
    }
}