package ru.itmo.testscheduler.scheduler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledTest {
    private String testName;
    private String affinityKey;
    private double expectedTime;
}