package ru.itmo.testscheduler.scheduler.strategy;

import org.springframework.stereotype.Component;
import ru.itmo.testscheduler.scheduler.Container;
import ru.itmo.testscheduler.scheduler.ScheduledTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class LptClassicStrategy implements SchedulingStrategy {

    @Override
    public List<Container> schedule(List<ScheduledTest> tests, int containerCount) {

        List<Container> containers = new ArrayList<>();

        for (int i = 0; i < containerCount; i++) {
            containers.add(new Container());
        }

        List<ScheduledTest> sorted = new ArrayList<>(tests);
        sorted.sort(Comparator.comparingDouble(ScheduledTest::getExpectedTime).reversed());

        for (ScheduledTest test : sorted) {
            Container target = containers.stream()
                    .min(Comparator.comparingDouble(Container::getTotalTime))
                    .orElseThrow();
            target.addTest(test);
        }

        return containers;
    }
}