package ru.itmo.testscheduler.scheduler.strategy;

import org.junit.jupiter.api.Test;
import ru.itmo.testscheduler.scheduler.ScheduledTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChunkingStrategyTest {

    private final ChunkingStrategy strategy = new ChunkingStrategy();

    @Test
    void shouldSplitIntoChunks() {

        List<ScheduledTest> tests = List.of(
                ScheduledTest.builder().expectedTime(1).build(),
                ScheduledTest.builder().expectedTime(2).build(),
                ScheduledTest.builder().expectedTime(3).build()
        );

        var result = strategy.schedule(tests, 2);

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldReturnEmptyContainers_whenNoTests() {

        ChunkingStrategy strategy = new ChunkingStrategy();

        var result = strategy.schedule(List.of(), 3);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getTests()).isEmpty();
    }

    @Test
    void shouldHandleUnevenDistribution() {

        ChunkingStrategy strategy = new ChunkingStrategy();

        var tests = List.of(
                ScheduledTest.builder().expectedTime(1).build(),
                ScheduledTest.builder().expectedTime(2).build(),
                ScheduledTest.builder().expectedTime(3).build(),
                ScheduledTest.builder().expectedTime(4).build(),
                ScheduledTest.builder().expectedTime(5).build()
        );

        var result = strategy.schedule(tests, 2);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTests().size()).isGreaterThan(0);
        assertThat(result.get(1).getTests().size()).isGreaterThan(0);
    }
}