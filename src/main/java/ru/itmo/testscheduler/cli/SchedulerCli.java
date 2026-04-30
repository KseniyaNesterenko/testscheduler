//package ru.itmo.testscheduler.cli;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//import ru.itmo.testscheduler.scheduler.*;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class SchedulerCli implements ApplicationRunner {
//
//    private final SchedulerService schedulerService;
//    private final TestTimeEstimator estimator;
//    private final TestExecutionService testExecutionService;
//
//    @Override
//    public void run(ApplicationArguments args) {
//
//        if (args.getOptionNames().isEmpty()) return;
//
//        if (args.containsOption("help")) {
//            System.out.println("--tests t1,t2 --containers 2 --strategy lpt");
//            return;
//        }
//
//        List<String> testNames = Arrays.asList(
//                args.getOptionValues("tests").get(0).split(",")
//        );
//
//        int containers = Integer.parseInt(
//                args.getOptionValues("containers").get(0)
//        );
//
//        String strategy = args.containsOption("strategy")
//                ? args.getOptionValues("strategy").get(0)
//                : "lpt";
//
//        List<ScheduledTest> tests = testNames.stream()
//                .map(name -> new ScheduledTest(
//                        name,
//                        estimator.estimate(testExecutionService.getDurations(name))
//                ))
//                .toList();
//
//        var result = schedulerService.schedule(tests, containers, strategy);
//
//        result.forEach(c ->
//                System.out.println(c.getTests() + " -> " + c.getTotalTime())
//        );
//    }
//}