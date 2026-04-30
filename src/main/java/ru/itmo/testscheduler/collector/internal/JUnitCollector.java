package ru.itmo.testscheduler.collector.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itmo.testscheduler.testexecution.api.TestExecutionFacade;
import ru.itmo.testscheduler.testexecution.model.TestStatus;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class JUnitCollector {

    private final TestExecutionFacade testExecutionFacade;

    public void parse(InputStream xml, String runId) throws Exception {
        log.info("JUnit report parsing started: runId={}", runId);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setExpandEntityReferences(false);

        var doc = factory.newDocumentBuilder().parse(xml);

        NodeList testcases = doc.getElementsByTagName("testcase");

        for (int i = 0; i < testcases.getLength(); i++) {

            Node node = testcases.item(i);

            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element testcase = (Element) node;

            String name = testcase.getAttribute("name");
            double time = parseTime(testcase.getAttribute("time"));

            TestStatus status = resolveStatus(testcase);

            testExecutionFacade.save(
                    name,
                    time,
                    status,
                    runId
            );
        }
        log.info("JUnit report parsing finished: runId={}, testsProcessed={}",
                runId,
                testcases.getLength());
    }

    private TestStatus resolveStatus(Element testcase) {

        if (hasTag(testcase, "failure") || hasTag(testcase, "error")) {
            return TestStatus.FAILED;
        }

        if (hasTag(testcase, "skipped")) {
            return TestStatus.SKIPPED;
        }

        return TestStatus.PASSED;
    }

    private boolean hasTag(Element testcase, String tagName) {
        return testcase.getElementsByTagName(tagName).getLength() > 0;
    }

    private double parseTime(String value) {
        if (value == null || value.isBlank()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.warn("Invalid time format: {}", value);
            return 0.0;
        }
    }
}