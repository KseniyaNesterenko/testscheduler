package ru.itmo.testscheduler.testexecution.internal;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.itmo.testscheduler.testexecution.model.TestStatus;
import ru.itmo.testscheduler.testexecution.repository.TestExecutionRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestExecutionFacadeImplTest {

    @Mock
    private TestExecutionRepository repository;

    @InjectMocks
    private TestExecutionFacadeImpl facade;

    @Test
    void shouldSaveExecution() {

        facade.save("t1", 1.0, TestStatus.PASSED, "r1", "a");

        verify(repository, times(1)).save(any());
    }

    @Test
    void shouldDeleteAll() {

        TestExecutionRepository repo = mock(TestExecutionRepository.class);

        TestExecutionFacadeImpl facade = new TestExecutionFacadeImpl(repo);

        facade.deleteAll();

        verify(repo).deleteAll();
    }

    @Test
    void shouldReturnTestNames() {

        TestExecutionRepository repo = mock(TestExecutionRepository.class);
        when(repo.findAllTestNames()).thenReturn(List.of("t1"));

        TestExecutionFacadeImpl facade = new TestExecutionFacadeImpl(repo);

        var result = facade.findAllTestNames();

        assertThat(result).contains("t1");
    }

    @Test
    void shouldReturnClassNameAsAffinityKey_whenHashPresent() {
        String key = facade.getAffinityKey("ru.itmo.MyTest#testMethod");
        assertThat(key).isEqualTo("ru.itmo.MyTest");
    }

    @Test
    void shouldReturnDefaultGroup_whenHashMissing() {
        String key = facade.getAffinityKey("SimpleTestName");
        assertThat(key).isEqualTo("default_group");
    }
}