package ru.itmo.testscheduler.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.itmo.testscheduler.api.dto.ScheduleRequest;
import ru.itmo.testscheduler.api.dto.TestExecutionRequest;
import ru.itmo.testscheduler.scheduler.model.ScheduleCommand;
import ru.itmo.testscheduler.testexecution.api.TestExecutionFacade;
import ru.itmo.testscheduler.scheduler.api.SchedulerFacade;
import ru.itmo.testscheduler.testexecution.model.TestStatus;

@Slf4j
@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class SchedulerController {

    private final SchedulerFacade schedulerFacade;
    private final TestExecutionFacade testExecutionFacade;

    @PostMapping
    public String scheduleTests(
            @Valid @RequestBody ScheduleRequest request,
            @RequestParam(defaultValue = "lpt") String strategy,
            @RequestParam(defaultValue = "json") String format
    ) {
        log.info("Received scheduling request: runId={}, tests={}, containers={}, strategy={}",
                request.getRunId(),
                request.getTestNames().size(),
                request.getContainerCount(),
                strategy);

        ScheduleCommand command = ScheduleCommand.builder()
                .testNames(request.getTestNames())
                .containerCount(request.getContainerCount())
                .runId(request.getRunId())
                .build();

        var result = schedulerFacade.scheduleAndGenerate(command, strategy, format);

        log.info("Scheduling completed: runId={}", request.getRunId());

        return result;
    }

    @PostMapping("/result")
    public void saveResult(@Valid @RequestBody TestExecutionRequest request) {

        log.info("Received test execution result: testName={}, runId={}, status={}",
                request.getTestName(),
                request.getRunId(),
                request.getStatus());

        testExecutionFacade.save(
                request.getTestName(),
                request.getDuration(),
                TestStatus.valueOf(request.getStatus().toUpperCase()),
                request.getRunId()
        );
    }
}