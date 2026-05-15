package ru.itmo.testscheduler.scheduler;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ImbalanceCalculatorTest {

    private final ImbalanceCalculator calculator = new ImbalanceCalculator();

    @Test
    void shouldReturnZero_whenEmpty() {
        assertThat(calculator.calculate(List.of())).isEqualTo(0);
    }

    @Test
    void shouldComputeStdDev() {
        Container c1 = new Container();
        c1.setTotalTime(10);

        Container c2 = new Container();
        c2.setTotalTime(20);

        double result = calculator.calculate(List.of(c1, c2));

        assertThat(result).isGreaterThan(0);
    }

    @Test
    void shouldReturnZero_whenNullInput() {
        assertThat(calculator.calculate(null)).isEqualTo(0);
    }
}