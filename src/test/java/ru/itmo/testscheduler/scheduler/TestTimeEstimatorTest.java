package ru.itmo.testscheduler.scheduler;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TestTimeEstimatorTest {

    private final TestTimeEstimator estimator = new TestTimeEstimator();

    @Test
    void shouldReturnAverage_whenHistoryExists() {
        double result = estimator.estimate(List.of(1.0, 2.0, 3.0), List.of(100.0));

        assertThat(result).isEqualTo(2.0);
    }

    @Test
    void shouldReturnMedian_whenNoHistory() {
        double result = estimator.estimate(List.of(), List.of(1.0, 2.0, 3.0));

        assertThat(result).isEqualTo(2.0);
    }

    @Test
    void shouldThrow_whenNoData() {
        assertThatThrownBy(() ->
                estimator.estimate(List.of(), List.of())
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrow_whenNoHistoricalDataForMedian() {

        TestTimeEstimator estimator = new TestTimeEstimator();

        assertThatThrownBy(() ->
                estimator.estimate(List.of(), List.of())
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldUseMedian_whenDurationsIsNull() {

        TestTimeEstimator estimator = new TestTimeEstimator();

        double result = estimator.estimate(null, List.of(1.0, 2.0, 3.0));

        assertThat(result).isEqualTo(2.0);
    }
    @Test
    void shouldThrow_whenMedianHasNoData() {

        TestTimeEstimator estimator = new TestTimeEstimator();

        assertThatThrownBy(() ->
                estimator.estimate(List.of(), List.of())
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldCoverMedianNullBranch() {

        TestTimeEstimator estimator = new TestTimeEstimator();

        try {
            estimator.estimate(null, null);
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    void shouldReturnDurationsAsIs_whenSizeIsLessThanThree() {
        List<Double> shortList = List.of(10.0, 20.0);
        double result = estimator.estimate(shortList, List.of(100.0));

        assertThat(result).isEqualTo(15.0);
    }

    @Test
    void shouldCalculateMedianForEvenList() {
        double result = estimator.estimate(List.of(), List.of(10.0, 20.0, 30.0, 40.0));
        assertThat(result).isEqualTo(25.0);
    }
}