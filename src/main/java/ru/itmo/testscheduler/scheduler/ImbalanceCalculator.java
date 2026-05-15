package ru.itmo.testscheduler.scheduler;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImbalanceCalculator {

    public double calculate(List<Container> containers) {
        if (containers == null || containers.isEmpty()) {
            return 0;
        }

        double mean = containers.stream()
                .mapToDouble(Container::getTotalTime)
                .average()
                .orElse(0);

        double variance = containers.stream()
                .mapToDouble(c -> Math.pow(c.getTotalTime() - mean, 2))
                .average()
                .orElse(0);

        return Math.sqrt(variance);
    }
}