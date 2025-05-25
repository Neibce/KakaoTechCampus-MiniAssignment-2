package dev.jun0.scheduler.schedule;

import dev.jun0.scheduler.schedule.dto.ScheduleAuthorNameResponse;
import dev.jun0.scheduler.schedule.dto.ScheduleResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository {
    ScheduleResponse save(String task, String authorUuid, String password);
    ScheduleResponse update(Long id, String task, String authorUuid);
    void delete(Long id);
    boolean isPasswordValid(Long id, String password);

    Optional<ScheduleResponse> findById(Long id);
    List<ScheduleAuthorNameResponse> findAll(LocalDate modifiedAt, String authorUuid, Pageable pageable);
}
