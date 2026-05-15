package ru.itmo.testscheduler.testexecution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.itmo.testscheduler.testexecution.model.TestExecution;
import ru.itmo.testscheduler.testexecution.model.TestStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface TestExecutionRepository extends JpaRepository<TestExecution, Long> {
    List<TestExecution> findByTestName(String testName);
    @Query("SELECT DISTINCT e.testName FROM TestExecution e")
    List<String> findAllTestNames();
    List<TestExecution> findTop10ByTestNameAndStatusOrderByExecutionTimeDesc(String testName, TestStatus status);
    List<TestExecution> findTop1000ByStatusOrderByExecutionTimeDesc(TestStatus status);
    @Modifying
    @Query("""
DELETE FROM TestExecution e
WHERE e.executionTime < :threshold
""")
    void deleteOlderThan(LocalDateTime threshold);
}