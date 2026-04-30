package ru.itmo.testscheduler.scheduler.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.testscheduler.api.dto.ContainerDto;
import ru.itmo.testscheduler.config.api.ConfigFacade;
import ru.itmo.testscheduler.schedulehistory.api.ScheduleHistoryFacade;
import ru.itmo.testscheduler.scheduler.*;
import ru.itmo.testscheduler.scheduler.api.SchedulerFacade;
import ru.itmo.testscheduler.scheduler.model.ScheduleCommand;
import ru.itmo.testscheduler.scheduler.strategy.StrategyType;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SchedulerFacadeImpl implements SchedulerFacade {

    private final SchedulerService schedulerService;
    private final TestTimeEstimator estimator;
    private final ConfigFacade configFacade;
    private final ScheduleHistoryFacade historyFacade;
    private final TestHistoryService historyService;

    @Override
    public String scheduleAndGenerate(ScheduleCommand command, String strategy, String format) {
        log.info("Scheduling started: runId={}, tests={}, containers={}, strategy={}",
                command.getRunId(),
                command.getTestNames().size(),
                command.getContainerCount(),
                strategy);

        List<ScheduledTest> tests = new ArrayList<>();

        for (String testName : command.getTestNames()) {
            List<Double> durations = historyService.getDurations(testName);

            double expectedTime;

            if (durations == null || durations.isEmpty()) {
                expectedTime = historyService.estimateForNewTest();
            } else {
                expectedTime = estimator.estimate(durations);
            }
            tests.add(new ScheduledTest(testName, expectedTime));
        }

        log.debug("Test estimation completed: count={}", tests.size());

        StrategyType strategyType;

        try {
            strategyType = StrategyType.valueOf(strategy.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown strategy: " + strategy);
        }

        List<Container> containers = schedulerService.schedule(
                tests,
                command.getContainerCount(),
                strategyType
        );

        log.info("Scheduling algorithm applied: containers={}, strategy={}",
                containers.size(),
                strategy);

        List<ContainerDto> dto = toDto(containers);

        String result = configFacade.generate(dto, format);
        log.info("Configuration generated: format={}", format);

        historyFacade.save(
                command.getRunId(),
                command.getContainerCount(),
                strategy,
                result
        );
        log.info("Schedule result saved: runId={}", command.getRunId());

        return result;
    }

    private List<ContainerDto> toDto(List<Container> containers) {
        return containers.stream()
                .map(c -> ContainerDto.builder()
                        .tests(c.getTests().stream()
                                .map(t -> t.getTestName())
                                .toList())
                        .totalTime(c.getTotalTime())
                        .build())
                .toList();
    }
}