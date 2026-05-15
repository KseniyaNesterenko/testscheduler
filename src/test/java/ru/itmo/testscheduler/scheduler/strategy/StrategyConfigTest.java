package ru.itmo.testscheduler.scheduler.strategy;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class StrategyConfigTest {

    private final StrategyConfig config = new StrategyConfig();

    @Test
    void shouldMapAllStrategies() {
        SchedulingStrategy classic = mock(LptClassicStrategy.class);
        SchedulingStrategy rr = mock(RoundRobinStrategy.class);
        SchedulingStrategy chunk = mock(ChunkingStrategy.class);
        SchedulingStrategy balanced = mock(LptBalancedStrategy.class);

        Map<StrategyType, SchedulingStrategy> result = config.strategyMap(
                List.of(classic, rr, chunk, balanced)
        );

        assertThat(result)
                .containsEntry(StrategyType.LPT_CLASSIC, classic)
                .containsEntry(StrategyType.ROUNDROBIN, rr)
                .containsEntry(StrategyType.CHUNKING, chunk)
                .containsEntry(StrategyType.LPT_BALANCED, balanced);

        assertThat(result).hasSize(4);
    }
}