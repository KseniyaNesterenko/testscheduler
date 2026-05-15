package ru.itmo.testscheduler.scheduler.internal;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itmo.testscheduler.scheduler.api.SchedulerFacade;
import ru.itmo.testscheduler.scheduler.strategy.StrategyType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SchedulerFacadeImplIntegrationTest {

    @Autowired
    private SchedulerFacade schedulerFacade;

    @Test
    @Disabled
    void shouldRunFullSchedulingPipeline() {

        var result = schedulerFacade.scheduleAndGenerate(
                new ru.itmo.testscheduler.scheduler.model.ScheduleCommand(
                        List.of("t1", "t2", "t3"),
                        2,
                        "run1"
                ),
                StrategyType.LPT_BALANCED,
                "json"
        );

        assertThat(result).isNotNull();
        assertThat(result).contains("containers");
    }
}