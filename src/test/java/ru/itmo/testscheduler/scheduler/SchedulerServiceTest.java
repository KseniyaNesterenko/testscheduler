package ru.itmo.testscheduler.scheduler;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.itmo.testscheduler.scheduler.strategy.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SchedulerServiceTest {

    @Test
    void shouldThrow_whenStrategyNotFound() {

        SchedulerService service = new SchedulerService(
                new EnumMap<>(StrategyType.class),
                mock(ImbalanceCalculator.class)
        );

        assertThatThrownBy(() ->
                service.schedule(List.of(), 2, StrategyType.LPT_CLASSIC, "r1")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldCallStrategyAndReturnContainers() {

        SchedulingStrategy mockStrategy = mock(SchedulingStrategy.class);

        when(mockStrategy.schedule(anyList(), anyInt()))
                .thenReturn(List.of(new Container()));

        Map<StrategyType, SchedulingStrategy> map = new EnumMap<>(StrategyType.class);
        map.put(StrategyType.LPT_CLASSIC, mockStrategy);

        SchedulerService service = new SchedulerService(
                map,
                mock(ImbalanceCalculator.class)
        );

        var result = service.schedule(
                List.of(ScheduledTest.builder().affinityKey("a").expectedTime(1).build()),
                1,
                StrategyType.LPT_CLASSIC,
                "r1"
        );

        assertThat(result).hasSize(1);
        verify(mockStrategy, times(1)).schedule(anyList(), anyInt());
    }
}