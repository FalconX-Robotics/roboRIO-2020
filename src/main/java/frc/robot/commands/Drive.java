package frc.robot.commands;

import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer.DriveConsumerType;
import frc.robot.subsystems.Drivetrain;

public class Drive extends CommandBase {
    private Drivetrain m_drivetrain;
    private DriveConsumerType m_mode;
    private BiFunction<Double, Double, Double> m_leftMod;
    private BiFunction<Double, Double, Double> m_rightMod;
    private DoubleSupplier m_leftSupplier;
    private DoubleSupplier m_rightSupplier;

    private BiConsumer<Double, Double> m_biConsumer;

    public Drive(Drivetrain drivetrain, DriveConsumerType mode, GenericHID leftHID, GenericHID rightHID,
            BiFunction<Double, Double, Double> leftMod, BiFunction<Double, Double, Double> rightMod) {
        setupVariables(drivetrain, mode, leftHID, rightHID, leftMod, rightMod);
    }

    public Drive(Drivetrain drivetrain, DriveConsumerType mode, GenericHID leftHID, GenericHID rightHID) {
        setupVariables(drivetrain, mode, leftHID, rightHID, (x, y) -> x, (x, y) -> y);
    }

    public Drive(Drivetrain drivetrain, DriveConsumerType mode) {
        setupVariables(drivetrain, mode, null, null, (x, y) -> x, (x, y) -> y);
    }

    private void setupVariables(Drivetrain drivetrain, DriveConsumerType mode, GenericHID leftHID, GenericHID rightHID,
            BiFunction<Double, Double, Double> leftMod, BiFunction<Double, Double, Double> rightMod) {
        m_drivetrain = drivetrain;
        m_mode = mode;
        setHIDs(leftHID, rightHID);
        m_leftMod = leftMod;
        m_rightMod = rightMod;
        addRequirements(drivetrain);
    }

    public void setHIDs(GenericHID leftHID, GenericHID rightHID) {
        if (leftHID == null)
            m_leftSupplier = () -> 0;
        if (rightHID == null)
            m_rightSupplier = () -> 0;
        if (leftHID != null && rightHID != null) {
            switch (m_mode) {
            case kTank:
                m_leftSupplier = () -> -leftHID.getY(kLeft);
                m_rightSupplier = () -> -rightHID.getY(kRight);
                break;
            case kArcade:
            case kCurve:
                m_leftSupplier = () -> -leftHID.getY(kLeft);
                if (m_rightSupplier.getClass().equals(Joystick.class)) {
                    m_rightSupplier = () -> ((Joystick) rightHID).getZ();
                } else {
                    m_rightSupplier = () -> rightHID.getX(kRight);

                }
                break;
            }
        }
    }

    public void setLeftMod(BiFunction<Double, Double, Double> mod) {
        if (mod == null) {
            mod = (x, y) -> x;
        }
        m_leftMod = mod;
    }

    public void setRightMod(BiFunction<Double, Double, Double> mod) {
        if (mod == null) {
            mod = (x, y) -> y;
        }
        m_rightMod = mod;
    }

    public void setMods(BiFunction<Double, Double, Double> leftMod, BiFunction<Double, Double, Double> rightMod) {
        setLeftMod(leftMod);
        setRightMod(rightMod);
    }

    @Override
    public void initialize() {
        switch (m_mode) {
        case kTank:
            m_biConsumer = (l, r) -> m_drivetrain.tankDrive(m_leftMod.apply(l, r), m_rightMod.apply(l, r), true);
            break;
        case kArcade:
            m_biConsumer = (l, r) -> m_drivetrain.arcadeDrive(m_leftMod.apply(l, r), m_rightMod.apply(l, r), true);
            break;
        case kCurve:
            m_biConsumer = (l, r) -> m_drivetrain.curvatureDrive(m_leftMod.apply(l, r), m_rightMod.apply(l, r));
            break;
        default:
            m_biConsumer = (l, r) -> m_drivetrain.tankDrive(0, 0);
        }
    }

    @Override
    public void execute() {
        // System.out.println(m_leftSupplier.getAsDouble());
        m_biConsumer.accept(m_leftSupplier.getAsDouble(), m_rightSupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        m_biConsumer.accept(0., 0.);
    }
}