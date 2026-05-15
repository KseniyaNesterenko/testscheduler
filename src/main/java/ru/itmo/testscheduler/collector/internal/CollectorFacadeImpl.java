package ru.itmo.testscheduler.collector.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.testscheduler.collector.api.CollectorFacade;
import ru.itmo.testscheduler.collector.api.ReportCollector;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectorFacadeImpl implements CollectorFacade {

    private final List<ReportCollector> collectors;

    @Override
    public void collect(InputStream inputStream,
                        String runId,
                        String format) throws Exception {

        ReportCollector collector = collectors.stream()
                .filter(c -> c.supports(format))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unsupported report format: " + format
                        ));

        collector.parse(inputStream, runId);
    }
}