package ru.itmo.testscheduler.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TestExecutionRequest {
    @NotBlank
    private String testName;
    @Positive
    private double duration;
    @Pattern(regexp = "PASSED|FAILED|SKIPPED")
    private String status;
    @NotBlank
    private String runId;
    @NotBlank
    private String affinityKey;
}