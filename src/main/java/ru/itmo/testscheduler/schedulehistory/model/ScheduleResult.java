package ru.itmo.testscheduler.schedulehistory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedule_results")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String runId;

    private int containerCount;

    private String strategy;

    @Column(columnDefinition = "TEXT")
    private String resultJson;

    private LocalDateTime createdAt;
}