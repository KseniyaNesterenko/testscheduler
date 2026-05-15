package ru.itmo.testscheduler.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Path;

@Command(name = "upload-report")
public class UploadReportCommand implements Runnable {

    @Option(names = "--file", required = true)
    private Path file;

    @Option(names = "--run-id", required = true)
    private String runId;

    @Override
    public void run() {
        try {
            String baseUrl = System.getenv()
                    .getOrDefault("SCHEDULER_URL", "http://localhost:8080");

            SchedulerApiClient client = new SchedulerApiClient(baseUrl);

            client.uploadReport(runId, file);

            System.out.println("✔ Report uploaded");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}