package ru.itmo.testscheduler.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itmo.testscheduler.scheduler.strategy.SchedulingStrategy;
import ru.itmo.testscheduler.scheduler.strategy.StrategyType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final Map<StrategyType, SchedulingStrategy> strategies;
    private final ImbalanceCalculator imbalanceCalculator;

    public List<Container> schedule(List<ScheduledTest> tests, int containerCount, StrategyType strategyType, String runId) {
        SchedulingStrategy strategy = strategies.get(strategyType);

        if (strategy == null) {
            log.error("Unknown scheduling strategy: {}", strategyType);
            throw new IllegalArgumentException("Unknown strategy: " + strategyType);
        }

        List<ScheduledTest> groupedByAffinity = tests;

        log.info("Grouping completed: original tests = {}, grouped entities = {}", tests.size(), groupedByAffinity.size());

        long start = System.currentTimeMillis();

        List<Container> containers = strategy.schedule(groupedByAffinity, containerCount);

        long algoTime = System.currentTimeMillis() - start;

        double imbalance = imbalanceCalculator.calculate(containers);

        log.info("ALGORITHM TIME: {} ms", algoTime);
        log.info("========== IMBALANCE METRIC ==========");
        log.info("Strategy: {}, Containers: {}, Groups: {}", strategyType, containerCount, groupedByAffinity.size());
        log.info("Imbalance (Std Dev): {}", imbalance);

        for (int i = 0; i < containers.size(); i++) {
            log.info("Container {}: total time = {}", i + 1, containers.get(i).getTotalTime());
        }
        log.info("=====================================");

        return containers;
    }
}