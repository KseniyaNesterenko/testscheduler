package ru.itmo.testscheduler.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "testscheduler-cli",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "CLI for test scheduling system",
        subcommands = {
                ScheduleCommand.class,
                UploadReportCommand.class
        }
)
public class Main implements Runnable {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.println("Use --help to see available commands.");
    }
}