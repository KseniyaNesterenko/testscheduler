package ru.itmo.testscheduler.scheduler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ScheduleCommand {
    private List<String> testNames;
    private int containerCount;
    private String runId;
}