package ru.itmo.testscheduler.testexecution.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itmo.testscheduler.testexecution.repository.TestExecutionRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestExecutionCleanupJobTest {
    @Mock
    private TestExecutionRepository repository;
    @InjectMocks
    private TestExecutionCleanupJob job;

    @Test
    void shouldCallDeleteOnCleanup() {
        job.cleanup();
        verify(repository).deleteOlderThan(any());
    }
}