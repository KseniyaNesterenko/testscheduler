package ru.itmo.testscheduler.testexecution.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_executions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String testName;

    @Column(nullable = false)
    private Double duration;

    @Column(nullable = false)
    private LocalDateTime executionTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestStatus status;

    @Column(nullable = false)
    private String runId;
}
