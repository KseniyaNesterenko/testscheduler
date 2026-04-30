package ru.itmo.testscheduler.scheduler.api;

import ru.itmo.testscheduler.scheduler.model.ScheduleCommand;

public interface SchedulerFacade {
    String scheduleAndGenerate(ScheduleCommand command, String strategy, String format);
}