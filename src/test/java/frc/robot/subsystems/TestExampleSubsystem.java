package frc.robot.subsystems;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.revrobotics.CANSparkMax;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestExampleSubsystem {
    @Mock
    CANSparkMax motor;

    private ExampleSubsystem exampleSubsystem;

    @BeforeEach
    public void setup() {
        exampleSubsystem = new ExampleSubsystem(motor);
    }

    @Test
    public void runMotorCorrectlySetsSpeed() {
        exampleSubsystem.runMotor(100.);

        ArgumentCaptor<Double> argument = ArgumentCaptor.forClass(Double.class);
        verify(motor, times(1)).set(argument.capture());
        assertEquals(1., argument.getValue(), 1e-12);
    }


    @ParameterizedTest
    @CsvSource({
        "100., 1.",
        "50., 0.5",
        "30., 0.3"
    })
    public void runMotorCorrectlySetsSpeedParameterized(double inputSpeed, double outputSpeed) {
        exampleSubsystem.runMotor(inputSpeed);

        ArgumentCaptor<Double> argument = ArgumentCaptor.forClass(Double.class);
        verify(motor, times(1)).set(argument.capture());
        assertEquals(outputSpeed, argument.getValue(), 1e-12);

    }
}