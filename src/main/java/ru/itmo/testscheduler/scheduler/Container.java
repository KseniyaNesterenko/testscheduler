package ru.itmo.testscheduler.scheduler;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Container {
    private final List<ScheduledTest> tests = new ArrayList<>();
    private double totalTime = 0;

    public void addTest(ScheduledTest test) {
        tests.add(test);
        totalTime += test.getExpectedTime();
    }
}
