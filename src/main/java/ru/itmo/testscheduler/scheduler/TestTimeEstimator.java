package ru.itmo.testscheduler.scheduler;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestTimeEstimator {

    public double estimate(List<Double> durations) {
        if (durations == null || durations.isEmpty()) {
            return 5.0; // временный fallback
        }

        double mean = durations.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);

        double variance = durations.stream()
                .mapToDouble(d -> Math.pow(d - mean, 2))
                .average()
                .orElse(0);

        double stdDev = Math.sqrt(variance);

        double alpha = calculateAlpha(durations.size());

        return mean + alpha * stdDev;
    }

    private double calculateAlpha(int sampleSize) {
        if (sampleSize < 3) return 1.5;
        if (sampleSize < 10) return 1.0;
        return 0.5;
    }
}