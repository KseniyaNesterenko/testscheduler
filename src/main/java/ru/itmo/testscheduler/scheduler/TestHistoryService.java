package ru.itmo.testscheduler.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.testscheduler.testexecution.api.TestExecutionFacade;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestHistoryService {

    private final TestExecutionFacade testExecutionFacade;

    public List<Double> getDurations(String testName) {
        return testExecutionFacade.getDurations(testName);
    }

    public double estimateForNewTest() {
        List<Double> allDurations = testExecutionFacade.getRecentDurations();

        if (allDurations.isEmpty()) {
            return 5.0;
        }

        double median = calculateMedian(allDurations);

        return median * 1.2;
    }

    private double calculateMedian(List<Double> values) {
        List<Double> sorted = values.stream()
                .sorted()
                .toList();

        int size = sorted.size();

        if (size % 2 == 1) {
            return sorted.get(size / 2);
        } else {
            return (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2.0;
        }
    }
}