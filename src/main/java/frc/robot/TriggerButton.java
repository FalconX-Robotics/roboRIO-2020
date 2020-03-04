package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.button.Button;

import static edu.wpi.first.wpilibj.util.ErrorMessages.requireNonNullParam;

/**
 * A {@link Button} that gets its state from a trigger on a {@link XboxController}.
 */
public class TriggerButton extends Button {
  private final XboxController m_controller;
  private final Hand m_hand;
  private final double m_axisAmount;

  /**
   * Creates a trigger button for triggering commands.
   *
   * @param controller The XboxController object that has the triggers.
   * @param hand Side of controller whose value should be returned.
   * @param axisAmount The trigger axis value that triggers a command.
   */
  public TriggerButton(XboxController controller, Hand hand , double axisAmount) {
    requireNonNullParam(controller, "controller", "TriggerButton");
    requireNonNullParam(hand, "hand", "TriggerButton");

    m_controller = controller;
    m_hand = hand;
    m_axisAmount = axisAmount;
  }

  /**
   * Checks whether the current trigger axis value is at or further than the target value.
   *
   * @return Whether the current trigger axis value is at or further than the target value.
   */
  @Override
  public boolean get() {
    return m_controller.getTriggerAxis(m_hand) >= m_axisAmount;
  }
}
