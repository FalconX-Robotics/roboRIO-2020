/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Soure Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                              */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
    public final static class Ports {
        public static final int FRONT_LEFT_MOTOR_PORT = 4;
        public static final int FRONT_RIGHT_MOTOR_PORT = 1;
        public static final int REAR_LEFT_MOTOR_PORT = 3;
        public static final int REAR_RIGHT_MOTOR_PORT = 2;

        // public static final int FRONT_LEFT_MOTOR_PORT = 3;
        // public static final int FRONT_RIGHT_MOTOR_PORT = 5;
        // public static final int REAR_LEFT_MOTOR_PORT = 2;
        // public static final int REAR_RIGHT_MOTOR_PORT = 4;
      
        public static final int XBOX_CONTROLLER_PORT = 0;

        public static final int LEFT_ENCODER_MOTOR = 5;
        public static final int RIGHT_ENCODER_MOTOR = 6;
        public static final int RIGHT_ENCODER_A = 2;
        public static final int RIGHT_ENCODER_B = 3;

        public static final int GYRO_PORT = 7;
        public static final int INTAKE_GYRO_PORT = 48390;

        public static final int ROLLER_MOTOR_PORT = 11; //randomly chosen sorry
        public static final int INTAKE_MECHANISM_MOTOR_PORT = 12; //^^

        public static final int TALON_TACH_PORT = 9;

        public static final int TOP_TACH_PORT = 0;
        public static final int BOTTOM_TACH_PORT = 1;
        
        public static final int ELEVATOR_LIMIT_SWITCH_LOWER_PORT = 00;
        public static final int ELEVATOR_LIMIT_SWITCH_UPPER_PORT = 00;
        public static final int ELEVATOR_MOTOR_FRONT_PORT = 00; //TODO: Put correct motor ports
        public static final int ELEVATOR_MOTOR_BACK_PORT = 00; 
      
        public static final int GONDALA_MOTOR_PORT = 10; //TODO Use actual port
    }

    public static class RamseteConstants {
        public static final double kRamseteB = 78.74; //reccomended by wpilibs
        public static final double kRamseteZeta = 27.56; // ^
        public static final double ksVolts = 0;
        public static final double kvVoltSecondsPerMeter = 0;
        public static final double kaVoltSecondsSquaredPerMeter = 0;
        public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(0);
    }

    public static class AutoDriveShuffleBoard {
        public static void init() {} // don't delete, thx
        public static final ShuffleboardTab tab = Shuffleboard.getTab("Auto Drive");
        
        // public static final ShuffleboardLayout PIDLayout = tab.getLayout("PID", BuiltInLayouts.kList)
                // .withProperties(Map.of("Label Position", "RIGHT")).withSize(2, 3).withPosition(0, 0);
    
        public static final NetworkTableEntry pEntry = tab.add("P", 0.).withSize(2, 1).withPosition(0, 0).getEntry();
        public static final NetworkTableEntry iEntry = tab.add("I", 0.).withSize(2, 1).withPosition(0, 1).getEntry();
        public static final NetworkTableEntry dEntry = tab.add("D", 0.).withSize(2, 1).withPosition(0, 2).getEntry();
    
        public static final NetworkTableEntry isFinished = tab
                .add("Is finished", false)
                .withSize(2, 2).withPosition(2, 0).getEntry();
        public static final NetworkTableEntry targetDistance = tab.add("Target Distance", 0.)
                .withSize(2, 1).withPosition(2, 2).getEntry();
        public static final NetworkTableEntry currentDistance = tab.add("Current Distance", 0.)
                .withWidget(BuiltInWidgets.kGraph)
                .withSize(6, 4).withPosition(4, 0).getEntry();
        public static final NetworkTableEntry currentDistance2 = tab.add("Current distance", 0.)
                .withSize(2, 1).withPosition(4, 4).getEntry();

        public static final NetworkTableEntry distance = tab.add("Distance", 0.).withSize(2, 1).withPosition(0, 3).getEntry();
        public static final NetworkTableEntry speed = tab.add("Speed", 0.).withSize(2, 1).withPosition(0, 4).getEntry();
        static {
            System.out.println("hi");
        }

    }

    public static class AutoTurnShuffleBoard {
        public static void init() {} // don't delete, thx
        public static final ShuffleboardTab tab = Shuffleboard.getTab("Auto Turn");
        
        // public static final ShuffleboardLayout PIDLayout = tab.getLayout("PID", BuiltInLayouts.kList)
                // .withProperties(Map.of("Label Position", "RIGHT")).withSize(2, 3).withPosition(0, 0);
    
        public static final NetworkTableEntry pEntry = tab.add("P", 0.).withSize(2, 1).withPosition(0, 0).getEntry();
        public static final NetworkTableEntry iEntry = tab.add("I", 0.).withSize(2, 1).withPosition(0, 1).getEntry();
        public static final NetworkTableEntry dEntry = tab.add("D", 0.).withSize(2, 1).withPosition(0, 2).getEntry();
    
        public static final NetworkTableEntry isFinished = tab
                .add("Is finished", false)
                .withSize(2, 2).withPosition(2, 0).getEntry();
        public static final NetworkTableEntry targetAngle = tab.add("Target Angle", 0.)
                .withSize(2, 1).withPosition(2, 2).getEntry();
        public static final NetworkTableEntry currentAngle = tab.add("Current Angle", 0.)
                .withWidget(BuiltInWidgets.kGraph)
                .withSize(6, 4).withPosition(4, 0).getEntry();
        public static final NetworkTableEntry currentAngle2 = tab.add("Current angle", 0.)
                .withSize(2, 1).withPosition(4, 4).getEntry();
        
        public static final NetworkTableEntry angle = tab.add("Angle", 0.).withSize(2, 1).withPosition(0, 3).getEntry();
    }

    public static class AutoIntakeShuffleBoard {
        public static void init() {} // don't delete, thx
        public static final ShuffleboardTab tab = Shuffleboard.getTab("Auto Intake");
        
        // public static final ShuffleboardLayout PIDLayout = tab.getLayout("PID", BuiltInLayouts.kList)
                // .withProperties(Map.of("Label Position", "RIGHT")).withSize(2, 3).withPosition(0, 0);
    
        public static final NetworkTableEntry pEntry = tab.add("P", 0.).withSize(2, 1).withPosition(0, 0).getEntry();
        public static final NetworkTableEntry iEntry = tab.add("I", 0.).withSize(2, 1).withPosition(0, 1).getEntry();
        public static final NetworkTableEntry dEntry = tab.add("D", 0.).withSize(2, 1).withPosition(0, 2).getEntry();
    
        public static final NetworkTableEntry isFinished = tab
                .add("Is finished", false)
                .withSize(2, 2).withPosition(2, 0).getEntry();
        public static final NetworkTableEntry targetAngle = tab.add("Target Angle", 0.)
                .withSize(2, 1).withPosition(2, 2).getEntry();
        public static final NetworkTableEntry currentAngle = tab.add("Current Angle", 0.)
                .withWidget(BuiltInWidgets.kGraph)
                .withSize(6, 4).withPosition(4, 0).getEntry();
        public static final NetworkTableEntry currentAngle2 = tab.add("Current angle", 0.)
                .withSize(2, 1).withPosition(4, 4).getEntry();
        
        public static final NetworkTableEntry angle = tab.add("Angle", 0.).withSize(2, 1).withPosition(0, 3).getEntry();
    }
}
