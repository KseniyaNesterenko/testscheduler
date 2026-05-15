package ru.itmo.testscheduler.api.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestExecutionRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldFail_whenStatusInvalid() {
        TestExecutionRequest req = new TestExecutionRequest();
        req.setTestName("t1");
        req.setDuration(1.0);
        req.setStatus("UNKNOWN");
        req.setRunId("r1");
        req.setAffinityKey("a");

        var violations = validator.validate(req);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void shouldPass_whenValid() {
        TestExecutionRequest req = new TestExecutionRequest();
        req.setTestName("t1");
        req.setDuration(1.0);
        req.setStatus("PASSED");
        req.setRunId("r1");
        req.setAffinityKey("a");

        var violations = validator.validate(req);

        assertThat(violations).isEmpty();
    }
}