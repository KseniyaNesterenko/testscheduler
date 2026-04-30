package ru.itmo.testscheduler.scheduler.strategy;

import ru.itmo.testscheduler.scheduler.Container;
import ru.itmo.testscheduler.scheduler.ScheduledTest;

import java.util.List;

public interface SchedulingStrategy {
    List<Container> schedule(List<ScheduledTest> tests, int containerCount);
}