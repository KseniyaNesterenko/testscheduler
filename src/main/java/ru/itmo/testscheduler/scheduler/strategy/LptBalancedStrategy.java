package ru.itmo.testscheduler.scheduler.strategy;
import org.springframework.stereotype.Component;
import ru.itmo.testscheduler.scheduler.Container;
import ru.itmo.testscheduler.scheduler.ScheduledTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class LptBalancedStrategy implements SchedulingStrategy {

    private static final double BETA = 0.3;

    @Override
    public List<Container> schedule(List<ScheduledTest> tests, int containerCount) {

        List<Container> containers = new ArrayList<>();

        for (int i = 0; i < containerCount; i++) {
            containers.add(new Container());
        }

        List<ScheduledTest> sorted = new ArrayList<>(tests);
        sorted.sort(Comparator.comparingDouble(ScheduledTest::getExpectedTime).reversed());

        for (ScheduledTest test : sorted) {

            double avgLoad = containers.stream()
                    .mapToDouble(Container::getTotalTime)
                    .average()
                    .orElse(0);

            Container target = containers.stream()
                    .min(Comparator.comparingDouble(c ->
                            score(c.getTotalTime(), avgLoad)))
                    .orElseThrow();

            target.addTest(test);
        }

        return containers;
    }

    private double score(double load, double avgLoad) {
        return load + BETA * Math.abs(load - avgLoad);
    }
}