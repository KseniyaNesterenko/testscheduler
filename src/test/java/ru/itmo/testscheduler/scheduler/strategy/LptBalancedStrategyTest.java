package ru.itmo.testscheduler.scheduler.strategy;

import org.junit.jupiter.api.Test;
import ru.itmo.testscheduler.scheduler.ScheduledTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LptBalancedStrategyTest {

    private final LptBalancedStrategy strategy = new LptBalancedStrategy();

    @Test
    void shouldBalanceWithPenalty() {

        List<ScheduledTest> tests = List.of(
                ScheduledTest.builder().expectedTime(10).build(),
                ScheduledTest.builder().expectedTime(9).build(),
                ScheduledTest.builder().expectedTime(1).build()
        );

        var result = strategy.schedule(tests, 2);

        assertThat(result).hasSize(2);
    }
}