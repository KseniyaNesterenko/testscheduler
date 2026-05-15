package ru.itmo.testscheduler.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ContainerDto {
    private List<String> tests;
    private double totalTime;
}