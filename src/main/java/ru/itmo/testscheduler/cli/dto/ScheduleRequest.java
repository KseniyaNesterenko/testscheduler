package ru.itmo.testscheduler.cli.dto;

import java.util.List;

public record ScheduleRequest(
        List<String> testNames,
        int containerCount,
        String runId
) {}