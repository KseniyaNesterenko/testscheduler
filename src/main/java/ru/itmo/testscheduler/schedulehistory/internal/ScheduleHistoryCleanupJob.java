package ru.itmo.testscheduler.schedulehistory.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itmo.testscheduler.schedulehistory.repository.ScheduleResultRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScheduleHistoryCleanupJob {

    private final ScheduleResultRepository repository;

    @Scheduled(cron = "0 30 3 * * *")
    public void cleanup() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        repository.deleteOlderThan(threshold);
    }
}