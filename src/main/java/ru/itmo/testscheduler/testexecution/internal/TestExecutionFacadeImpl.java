package ru.itmo.testscheduler.testexecution.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itmo.testscheduler.testexecution.api.TestExecutionFacade;
import ru.itmo.testscheduler.testexecution.model.TestExecution;
import ru.itmo.testscheduler.testexecution.model.TestStatus;
import ru.itmo.testscheduler.testexecution.repository.TestExecutionRepository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TestExecutionFacadeImpl implements TestExecutionFacade {

    private final TestExecutionRepository repository;

    @Override
    public void save(String testName, double duration, TestStatus status, String runId) {
        repository.save(TestExecution.builder()
                .testName(testName)
                .duration(duration)
                .executionTime(LocalDateTime.now())
                .status(status)
                .runId(runId)
                .build());
        log.debug("Saved test execution: testName={}, runId={}", testName, runId);
    }

    @Override
    public List<Double> getDurations(String testName) {
        return repository.findTop10ByTestNameOrderByExecutionTimeDesc(testName)
                .stream()
                .map(TestExecution::getDuration)
                .toList();
    }

    @Override
    public List<Double> getRecentDurations() {
        return repository.findTop1000ByOrderByExecutionTimeDesc()
                .stream()
                .map(TestExecution::getDuration)
                .toList();
    }
}