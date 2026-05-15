package ru.itmo.testscheduler.schedulehistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.itmo.testscheduler.schedulehistory.model.ScheduleResult;

import java.time.LocalDateTime;

public interface ScheduleResultRepository extends JpaRepository<ScheduleResult, Long> {
    @Modifying
    @Query("""
DELETE FROM ScheduleResult s
WHERE s.createdAt < :threshold
""")
    void deleteOlderThan(LocalDateTime threshold);
}