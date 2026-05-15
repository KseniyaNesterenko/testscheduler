package ru.itmo.testscheduler.scheduler.strategy;

import org.springframework.stereotype.Component;
import ru.itmo.testscheduler.scheduler.Container;
import ru.itmo.testscheduler.scheduler.ScheduledTest;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoundRobinStrategy implements SchedulingStrategy {

    @Override
    public List<Container> schedule(List<ScheduledTest> tests, int containerCount) {

        List<Container> containers = new ArrayList<>();

        for (int i = 0; i < containerCount; i++) {
            containers.add(new Container());
        }

        int index = 0;

        for (ScheduledTest test : tests) {
            containers.get(index % containerCount).addTest(test);
            index++;
        }

        return containers;
    }
}