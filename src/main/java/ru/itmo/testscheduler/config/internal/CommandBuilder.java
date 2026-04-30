package ru.itmo.testscheduler.config.internal;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandBuilder {

    public List<String> buildCommands(List<String> tests) {
        return tests.stream()
                .map(this::buildCommand)
                .toList();
    }

    private String buildCommand(String testName) {
        return "docker run test-runner --test " + testName;
    }
}