package ru.itmo.testscheduler.api.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldFail_whenRunIdIsBlank() {
        ScheduleRequest req = new ScheduleRequest();
        req.setRunId("");
        req.setContainerCount(1);
        req.setTestNames(List.of("test"));

        var violations = validator.validate(req);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void shouldFail_whenContainerCountLessThanOne() {
        ScheduleRequest req = new ScheduleRequest();
        req.setRunId("run1");
        req.setContainerCount(0);
        req.setTestNames(List.of("test"));

        var violations = validator.validate(req);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void shouldPass_whenValid() {
        ScheduleRequest req = new ScheduleRequest();
        req.setRunId("run1");
        req.setContainerCount(2);
        req.setTestNames(List.of("t1"));

        var violations = validator.validate(req);

        assertThat(violations).isEmpty();
    }
}