package ru.itmo.testscheduler.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import ru.itmo.testscheduler.cli.dto.ScheduleRequest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Command(
        name = "schedule",
        description = "Schedule tests across containers",
        mixinStandardHelpOptions = true
)
public class ScheduleCommand implements Runnable {

    @Option(names = "--tests", split = ",")
    private List<String> tests;

    @Option(names = "--tests-file")
    private Path testsFile;

    @Option(names = "--containers", required = true)
    private int containerCount;

    @Option(names = "--strategy", required = true)
    private String strategy;

    @Option(names = "--run-id", required = true)
    private String runId;

    @Option(names = "--format", defaultValue = "json")
    private String format;

    @Override
    public void run() {
        try {
            List<String> finalTests = resolveTests();

            String baseUrl = System.getenv()
                    .getOrDefault("SCHEDULER_URL", "http://localhost:8080");

            SchedulerApiClient client = new SchedulerApiClient(baseUrl);

            ScheduleRequest request = new ScheduleRequest(
                    finalTests,
                    containerCount,
                    runId
            );

            String response = client.schedule(request, strategy, format);

            System.out.println("✔ Scheduling completed");
            System.out.println(response);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private List<String> resolveTests() throws Exception {
        if (tests != null && !tests.isEmpty()) return tests;
        if (testsFile != null) return Files.readAllLines(testsFile);

        throw new IllegalArgumentException("Provide --tests or --tests-file");
    }
}