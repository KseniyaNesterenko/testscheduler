package ru.itmo.testscheduler.scheduler;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TestTimeEstimator {

    public double estimate(List<Double> durations, List<Double> recentDurations) {

        if (durations != null && !durations.isEmpty()) {

            List<Double> filtered = filterOutliers(durations);

            return filtered.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0);
        }

        return median(recentDurations);
    }

    private double median(List<Double> values) {

        if (values == null || values.isEmpty()) {
            throw new IllegalStateException("No historical data available to estimate median");
        }

        List<Double> sorted = values.stream()
                .sorted()
                .toList();

        int n = sorted.size();

        if (n % 2 == 0) {
            return (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2.0;
        } else {
            return sorted.get(n / 2);
        }
    }

    private List<Double> filterOutliers(List<Double> values) {

        if (values.size() < 3) {
            return values;
        }

        double mean = values.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);

        double variance = values.stream()
                .mapToDouble(v -> Math.pow(v - mean, 2))
                .average()
                .orElse(0);

        double stdDev = Math.sqrt(variance);

        return values.stream()
                .filter(v -> Math.abs(v - mean) <= 2 * stdDev)
                .toList();
    }
}