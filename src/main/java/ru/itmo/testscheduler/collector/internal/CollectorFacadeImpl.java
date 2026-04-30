package ru.itmo.testscheduler.collector.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.testscheduler.collector.api.CollectorFacade;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class CollectorFacadeImpl implements CollectorFacade {

    private final JUnitCollector collector;

    @Override
    public void collect(InputStream inputStream, String runId) throws Exception {
        collector.parse(inputStream, runId);
    }
}