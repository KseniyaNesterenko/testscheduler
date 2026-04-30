package ru.itmo.testscheduler.schedulehistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.testscheduler.schedulehistory.model.ScheduleResult;

public interface ScheduleResultRepository extends JpaRepository<ScheduleResult, Long> {
}