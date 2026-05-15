package ru.itmo.testscheduler.scheduler.strategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Configuration
public class StrategyConfig {

    @Bean
    public Map<StrategyType, SchedulingStrategy> strategyMap(
            List<SchedulingStrategy> strategyList) {

        Map<StrategyType, SchedulingStrategy> map = new EnumMap<>(StrategyType.class);

        for (SchedulingStrategy strategy : strategyList) {

            if (strategy instanceof LptClassicStrategy) {
                map.put(StrategyType.LPT_CLASSIC, strategy);
            } else if (strategy instanceof RoundRobinStrategy) {
                map.put(StrategyType.ROUNDROBIN, strategy);
            } else if (strategy instanceof ChunkingStrategy) {
                map.put(StrategyType.CHUNKING, strategy);
            } else if (strategy instanceof LptBalancedStrategy) {
                map.put(StrategyType.LPT_BALANCED, strategy);
            }
        }

        return map;
    }
}
