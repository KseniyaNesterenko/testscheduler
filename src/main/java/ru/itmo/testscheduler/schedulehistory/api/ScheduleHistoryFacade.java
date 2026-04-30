package ru.itmo.testscheduler.schedulehistory.api;

public interface ScheduleHistoryFacade {
    void save(String runId, int containerCount, String strategy, String resultJson);
}