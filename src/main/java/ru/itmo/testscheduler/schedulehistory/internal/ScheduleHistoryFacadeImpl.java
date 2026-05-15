package ru.itmo.testscheduler.schedulehistory.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itmo.testscheduler.schedulehistory.api.ScheduleHistoryFacade;
import ru.itmo.testscheduler.schedulehistory.model.ScheduleResult;
import ru.itmo.testscheduler.schedulehistory.repository.ScheduleResultRepository;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleHistoryFacadeImpl implements ScheduleHistoryFacade {

    private final ScheduleResultRepository repository;

    @Override
    public void save(String runId, int containerCount, String strategy, String resultJson) {

        repository.save(ScheduleResult.builder()
                .runId(runId)
                .containerCount(containerCount)
                .strategy(strategy)
                .resultJson(resultJson)
                .createdAt(LocalDateTime.now())
                .build());
        log.info("Saved schedule history: runId={}, strategy={}",
                runId,
                strategy);
    }
}