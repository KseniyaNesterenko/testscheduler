package ru.itmo.testscheduler.collector.api;

import java.io.InputStream;

public interface CollectorFacade {
    void collect(InputStream inputStream, String runId) throws Exception;
}