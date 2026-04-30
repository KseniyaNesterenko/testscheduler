package ru.itmo.testscheduler.scheduler;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImbalanceCalculator {

    public double calculate(List<Container> containers) {
        double max = containers.stream().mapToDouble(Container::getTotalTime).max().orElse(0);
        double min = containers.stream().mapToDouble(Container::getTotalTime).min().orElse(0);
        if (max == 0) return 0;
        return (max - min) / max;
    }
}