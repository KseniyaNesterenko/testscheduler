package ru.itmo.testscheduler.collector;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.itmo.testscheduler.collector.api.ReportCollector;
import ru.itmo.testscheduler.collector.internal.CollectorFacadeImpl;
import ru.itmo.testscheduler.collector.internal.JUnitCollector;
import ru.itmo.testscheduler.testexecution.api.TestExecutionFacade;
import ru.itmo.testscheduler.testexecution.model.TestStatus;

import javax.swing.text.html.parser.Element;
import java.io.ByteArrayInputStream;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class JUnitCollectorTest {

    @Test
    void shouldParsePassedTest() throws Exception {

        TestExecutionFacade facade = mock(TestExecutionFacade.class);

        JUnitCollector collector = new JUnitCollector(facade);

        String xml =
                "<testsuite>" +
                        "<testcase classname='c1' name='t1' time='1.0'/>" +
                        "</testsuite>";

        collector.parse(new ByteArrayInputStream(xml.getBytes()), "run1");

        verify(facade, times(1))
                .save(any(), anyDouble(), eq(TestStatus.PASSED), eq("run1"), any());
    }

    @Test
    void shouldHandleInvalidTime() throws Exception {

        JUnitCollector collector = new JUnitCollector(mock(TestExecutionFacade.class));

        var xml = "<testsuite><testcase classname='c' name='t' time='bad'/></testsuite>";

        collector.parse(new ByteArrayInputStream(xml.getBytes()), "run1");
    }

    @Test
    void shouldDetectFailureAndSkippedTags() {

        String xml =
                "<testcase classname='c' name='t'>" +
                        "<failure/>" +
                        "</testcase>";
    }

    @Test
    void shouldDelegateToCollector() throws Exception {

        JUnitCollector collector = mock(JUnitCollector.class);

        when(collector.supports("junit")).thenReturn(true);

        CollectorFacadeImpl facade =
                new CollectorFacadeImpl(List.of(collector));

        facade.collect(
                new ByteArrayInputStream("<xml/>".getBytes()),
                "r1",
                "junit"
        );

        verify(collector).parse(any(), eq("r1"));
    }

    @Test
    void shouldMarkFailedTest_whenFailureTagExists() throws Exception {

        TestExecutionFacade facade = mock(TestExecutionFacade.class);

        JUnitCollector collector = new JUnitCollector(facade);

        String xml =
                "<testsuite>" +
                        "<testcase classname='c1' name='t1' time='1.0'>" +
                        "<failure/>" +
                        "</testcase>" +
                        "</testsuite>";

        collector.parse(new ByteArrayInputStream(xml.getBytes()), "run1");

        verify(facade).save(any(), anyDouble(), any(), eq("run1"), any());
    }

    @Test
    void shouldMarkSkippedTest() throws Exception {

        TestExecutionFacade facade = mock(TestExecutionFacade.class);

        JUnitCollector collector = new JUnitCollector(facade);

        String xml =
                "<testsuite>" +
                        "<testcase classname='c1' name='t1' time='1.0'>" +
                        "<skipped/>" +
                        "</testcase>" +
                        "</testsuite>";

        collector.parse(new ByteArrayInputStream(xml.getBytes()), "run1");

        verify(facade).save(any(), anyDouble(), any(), eq("run1"), any());
    }

    @Test
    void shouldHandleMissingTimeAttribute() throws Exception {

        TestExecutionFacade facade = mock(TestExecutionFacade.class);
        JUnitCollector collector = new JUnitCollector(facade);

        String xml =
                "<testsuite>" +
                        "<testcase classname='c1' name='t1'/>" +
                        "</testsuite>";

        collector.parse(new ByteArrayInputStream(xml.getBytes()), "run1");

        verify(facade).save(any(), eq(0.0), any(), eq("run1"), any());
    }

    @Test
    void shouldSkipTextNodesInDomTraversal() throws Exception {

        TestExecutionFacade facade = mock(TestExecutionFacade.class);
        JUnitCollector collector = new JUnitCollector(facade);

        String xml =
                "<?xml version='1.0'?>" +
                        "<testsuite>" +
                        "   " +
                        "<testcase classname='c1' name='t1' time='1.0'/>" +
                        "   " +
                        "</testsuite>";

        collector.parse(new ByteArrayInputStream(xml.getBytes()), "run1");

        verify(facade).save(any(), anyDouble(), any(), eq("run1"), any());
    }

    @Test
    void shouldMarkErrorTest() throws Exception {

        TestExecutionFacade facade = mock(TestExecutionFacade.class);
        JUnitCollector collector = new JUnitCollector(facade);

        String xml =
                "<testsuite>" +
                        "<testcase classname='c1' name='t1'>" +
                        "<error/>" +
                        "</testcase>" +
                        "</testsuite>";

        collector.parse(new ByteArrayInputStream(xml.getBytes()), "run1");

        verify(facade).save(any(), anyDouble(), eq(TestStatus.FAILED), eq("run1"), any());
    }

    @Test
    void shouldHandleNullTimeAttribute() throws Exception {

        TestExecutionFacade facade = mock(TestExecutionFacade.class);
        JUnitCollector collector = new JUnitCollector(facade);

        String xml =
                "<testsuite>" +
                        "<testcase classname='c1' name='t1'/>" +
                        "</testsuite>";

        collector.parse(new ByteArrayInputStream(xml.getBytes()), "run1");

        verify(facade).save(any(), eq(0.0), any(), eq("run1"), any());
    }

    @Test
    void shouldHandleInvalidTimeFormat() throws Exception {

        TestExecutionFacade facade = mock(TestExecutionFacade.class);
        JUnitCollector collector = new JUnitCollector(facade);

        String xml =
                "<testsuite>" +
                        "<testcase classname='c1' name='t1' time='abc'/>" +
                        "</testsuite>";

        collector.parse(new ByteArrayInputStream(xml.getBytes()), "run1");

        verify(facade).save(any(), eq(0.0), any(), eq("run1"), any());
    }

    @Test
    void shouldHandleBlankTimeAttribute() throws Exception {

        TestExecutionFacade facade = mock(TestExecutionFacade.class);
        JUnitCollector collector = new JUnitCollector(facade);

        String xml =
                "<testsuite>" +
                        "<testcase classname='c1' name='t1' time=''/>" +
                        "</testsuite>";

        collector.parse(new ByteArrayInputStream(xml.getBytes()), "run1");

        verify(facade).save(
                any(),
                eq(0.0),
                any(),
                eq("run1"),
                any()
        );
    }

    @Test
    void shouldHandleNullTimeExplicitly() throws Exception {

        TestExecutionFacade facade = mock(TestExecutionFacade.class);
        JUnitCollector collector = new JUnitCollector(facade);

        var method = JUnitCollector.class.getDeclaredMethod("parseTime", String.class);
        method.setAccessible(true);

        double result = (double) method.invoke(collector, (Object) null);

        assertThat(result).isEqualTo(0.0);
    }

    @Test
    void shouldThrowException_whenFormatIsUnsupported() {
        ReportCollector mockCollector = mock(ReportCollector.class);
        when(mockCollector.supports("junit")).thenReturn(true);

        CollectorFacadeImpl facade = new CollectorFacadeImpl(List.of(mockCollector));

        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                        facade.collect(new java.io.ByteArrayInputStream(new byte[0]), "run1", "unknown_format")
                ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported report format: unknown_format");
    }

    @Test
    void shouldSupportOnlyJUnitFormat() {
        JUnitCollector collector = new JUnitCollector(mock(TestExecutionFacade.class));

        assertThat(collector.supports("junit")).isTrue();
        assertThat(collector.supports("JUNIT")).isTrue();
        assertThat(collector.supports("testng")).isFalse();
    }
}