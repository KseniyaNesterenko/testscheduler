package ru.itmo.testscheduler.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itmo.testscheduler.scheduler.strategy.SchedulingStrategy;
import ru.itmo.testscheduler.scheduler.strategy.StrategyType;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final Map<String, SchedulingStrategy> strategies;
    private final ImbalanceCalculator imbalanceCalculator;

    public List<Container> schedule(List<ScheduledTest> tests, int containerCount, StrategyType strategyType) {

        log.info("Applying scheduling strategy: {}", strategyType);
        SchedulingStrategy strategy = strategies.get(strategyType.name().toLowerCase());
        log.debug("Strategy resolved: {}", strategyType);

        if (strategy == null) {
            log.error("Unknown scheduling strategy: {}", strategyType);
            throw new IllegalArgumentException("Unknown strategy: " + strategyType);
        }

        List<Container> containers = strategy.schedule(tests, containerCount);

        double imbalance = imbalanceCalculator.calculate(containers);
        log.info("Load imbalance calculated: {}", imbalance);

        return containers;
    }
}