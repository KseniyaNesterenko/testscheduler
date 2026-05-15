package ru.itmo.testscheduler.collector.api;

import java.io.InputStream;

public interface ReportCollector {

    boolean supports(String format);

    void parse(InputStream inputStream, String runId) throws Exception;
}