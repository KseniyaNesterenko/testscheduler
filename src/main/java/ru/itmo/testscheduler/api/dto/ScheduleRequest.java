package ru.itmo.testscheduler.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ScheduleRequest {
    @NotEmpty
    private List<String> testNames;
    @Min(value = 1, message = "containerCount must be >= 1")
    private int containerCount;
    @NotBlank
    private String runId;
}