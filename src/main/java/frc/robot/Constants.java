/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
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

        public static final int LEFT_ENCODER_A = 0;
        public static final int LEFT_ENCODER_B = 1;
        public static final int RIGHT_ENCODER_A = 2;
        public static final int RIGHT_ENCODER_B = 3;
    }

    // in inches
    public static final double WHEEL_CIRCUMFERENCE = 6d * Math.PI;
    public static final double ENCODER_COUNTS_PER_REVOLUTION = 4096;
    public static final double kEncoderDistancePerPulse = WHEEL_CIRCUMFERENCE / ENCODER_COUNTS_PER_REVOLUTION;
}

