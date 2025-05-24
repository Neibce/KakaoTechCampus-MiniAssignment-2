package dev.jun0.scheduler.schedule;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository {
    void save(Schedule schedule);

    Optional<Schedule> findById(Long id);
    List<Schedule> findAll(LocalDate modifiedAt, String author);
}
