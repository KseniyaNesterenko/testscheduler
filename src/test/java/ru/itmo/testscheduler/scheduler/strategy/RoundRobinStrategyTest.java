package ru.itmo.testscheduler.scheduler.strategy;

import org.junit.jupiter.api.Test;
import ru.itmo.testscheduler.scheduler.ScheduledTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoundRobinStrategyTest {

    private final RoundRobinStrategy strategy = new RoundRobinStrategy();

    @Test
    void shouldDistributeRoundRobin() {

        List<ScheduledTest> tests = List.of(
                ScheduledTest.builder().expectedTime(1).build(),
                ScheduledTest.builder().expectedTime(2).build(),
                ScheduledTest.builder().expectedTime(3).build(),
                ScheduledTest.builder().expectedTime(4).build()
        );

        var result = strategy.schedule(tests, 2);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTests()).isNotEmpty();
    }
}