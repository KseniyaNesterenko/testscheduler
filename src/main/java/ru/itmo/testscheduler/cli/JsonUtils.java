package ru.itmo.testscheduler.cli;

import java.util.List;
import java.util.stream.Collectors;

public class JsonUtils {

    public static String buildScheduleRequest(List<String> tests, int containers, String runId) {

        String testArray = tests.stream()
                .map(t -> "\"" + t + "\"")
                .collect(Collectors.joining(","));

        return """
                {
                  "testNames": [%s],
                  "containerCount": %d,
                  "runId": "%s"
                }
                """.formatted(testArray, containers, runId);
    }
}