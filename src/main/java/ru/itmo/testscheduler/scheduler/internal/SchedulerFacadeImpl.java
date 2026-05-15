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
import ru.itmo.testscheduler.testexecution.api.TestExecutionFacade;

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
    private final TestExecutionFacade testExecutionFacade;

    @Override
    public String scheduleAndGenerate(ScheduleCommand command, StrategyType strategy, String format) {

        log.info("Scheduling started: runId={}, tests={}, containers={}, strategy={}",
                command.getRunId(),
                command.getTestNames().size(),
                command.getContainerCount(),
                strategy);

        List<ScheduledTest> tests = new ArrayList<>();
        List<Double> recentDurations = testExecutionFacade.getRecentDurations();

        for (String testName : command.getTestNames()) {
            List<Double> durations = testExecutionFacade.getDurations(testName);

            double expectedTime = estimator.estimate(durations, recentDurations);

            String affinityKey = testExecutionFacade.getAffinityKey(testName);

            tests.add(ScheduledTest.builder()
                    .testName(testName)
                    .affinityKey(affinityKey)
                    .expectedTime(expectedTime)
                    .build());
        }

        log.debug("Test estimation completed: count={}", tests.size());

        List<Container> containers = schedulerService.schedule(
                tests,
                command.getContainerCount(),
                strategy,
                command.getRunId()
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
                strategy.name(),
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