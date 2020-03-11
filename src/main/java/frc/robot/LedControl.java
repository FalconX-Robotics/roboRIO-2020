package frc.robot;

import java.util.HashMap;

import edu.wpi.first.wpilibj.PWMSparkMax;
import frc.robot.Constants;

public class LedControl {
    public LedControl() {
        setMapColors();
    }

    private PWMSparkMax ledController = new PWMSparkMax(Constants.Ports.LED_CONTROLLER_PORT);

    private HashMap<String, Double> patterns = new HashMap<>();

    /**
     * Sets the led pattern according to values from: (Follows same name format) <p>
     * www.revrobotics.com/content/docs/REV-11-1105-UM.pdf
     * @param pattern the pattern to set the led to
     */ 
    public void setLed(String pattern) {
        ledController.setSpeed(patterns.get(pattern));
    }

    private void setMapColors() {
        patterns.put("None", 0.0);
        patterns.put("Violet", 0.91);
        patterns.put("Red", 0.61);
        patterns.put("Rainbow, Rainbow Palette", -0.99);
        patterns.put("Breath, Blue", -0.15);
    }
}