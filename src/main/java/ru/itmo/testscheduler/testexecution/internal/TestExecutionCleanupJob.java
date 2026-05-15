package ru.itmo.testscheduler.testexecution.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itmo.testscheduler.testexecution.repository.TestExecutionRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TestExecutionCleanupJob {

    private final TestExecutionRepository repository;

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanup() {

        LocalDateTime threshold = LocalDateTime.now().minusDays(14);

        repository.deleteOlderThan(threshold);
    }
}