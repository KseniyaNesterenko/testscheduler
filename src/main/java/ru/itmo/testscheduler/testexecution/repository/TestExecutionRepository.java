package ru.itmo.testscheduler.testexecution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.testscheduler.testexecution.model.TestExecution;

import java.util.List;

public interface TestExecutionRepository extends JpaRepository<TestExecution, Long> {
    List<TestExecution> findByTestName(String testName);
    List<TestExecution> findTop10ByTestNameOrderByExecutionTimeDesc(String testName);
    List<TestExecution> findTop1000ByOrderByExecutionTimeDesc();
}