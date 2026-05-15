package ru.itmo.testscheduler.schedulerhistory.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itmo.testscheduler.schedulehistory.internal.ScheduleHistoryCleanupJob;
import ru.itmo.testscheduler.schedulehistory.repository.ScheduleResultRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScheduleHistoryCleanupJobTest {
    @Mock
    private ScheduleResultRepository repository;
    @InjectMocks
    private ScheduleHistoryCleanupJob job;

    @Test
    void shouldCallDeleteOnCleanup() {
        job.cleanup();
        verify(repository).deleteOlderThan(any());
    }
}