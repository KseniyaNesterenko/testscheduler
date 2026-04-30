package ru.itmo.testscheduler.testexecution.api;

import ru.itmo.testscheduler.testexecution.model.TestStatus;

import java.util.List;

public interface TestExecutionFacade {

    void save(String testName, double duration, TestStatus status, String runId);

    List<Double> getDurations(String testName);
    List<Double> getRecentDurations();
}