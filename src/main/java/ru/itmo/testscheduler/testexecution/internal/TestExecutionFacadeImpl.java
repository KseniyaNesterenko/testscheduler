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
    public void save(String testName, double duration, TestStatus status, String runId, String affinityKey) {
        repository.save(TestExecution.builder()
                .testName(testName)
                .affinityKey(affinityKey)
                .duration(duration)
                .executionTime(LocalDateTime.now())
                .status(status)
                .runId(runId)
                .build());
        log.debug("Saved test execution: testName={}, runId={}", testName, runId);
    }

    @Override
    public List<Double> getDurations(String testName) {
        return repository.findTop10ByTestNameAndStatusOrderByExecutionTimeDesc(testName, TestStatus.PASSED)
                .stream()
                .map(TestExecution::getDuration)
                .toList();
    }

    @Override
    public List<Double> getRecentDurations() {
        return repository.findTop1000ByStatusOrderByExecutionTimeDesc(TestStatus.PASSED)
                .stream()
                .map(TestExecution::getDuration)
                .toList();
    }

    @Override
    public List<String> findAllTestNames() {
        return repository.findAllTestNames();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public String getAffinityKey(String testName) {
        int idx = testName.lastIndexOf("#");
        if (idx == -1) return "default_group";
        return testName.substring(0, idx);
    }
}