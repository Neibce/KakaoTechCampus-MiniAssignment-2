package dev.jun0.scheduler.schedule;

import dev.jun0.scheduler.schedule.dto.ScheduleCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void createSchedule(ScheduleCreateRequest request) {
        Schedule schedule = new Schedule(
                request.getId(),
                request.getTask(),
                request.getAuthor(),
                request.getPassword(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public Schedule getSchedule(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다"));
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedules(LocalDate updatedDate, String author) {
        return scheduleRepository.findAll(updatedDate, author);
    }
}
