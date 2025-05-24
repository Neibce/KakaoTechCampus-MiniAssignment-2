package dev.jun0.scheduler.schedule;

import dev.jun0.scheduler.schedule.dto.ScheduleResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository {
    void save(Schedule schedule);

    Optional<ScheduleResponse> findById(Long id);
    List<ScheduleResponse> findAll(LocalDate modifiedAt, String author);
}
