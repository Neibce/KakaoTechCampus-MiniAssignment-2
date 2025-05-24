package dev.jun0.scheduler.schedule;

import dev.jun0.scheduler.schedule.dto.ScheduleCreateRequest;
import dev.jun0.scheduler.schedule.dto.ScheduleDeleteRequest;
import dev.jun0.scheduler.schedule.dto.SchedulePatchRequest;
import dev.jun0.scheduler.schedule.dto.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ScheduleResponse createSchedule(ScheduleCreateRequest request) {
        return scheduleRepository.save(
                request.getTask(),
                request.getAuthor(),
                request.getPassword()
        );
    }

    @Transactional(readOnly = true)
    public ScheduleResponse getSchedule(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다"));
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedules(LocalDate updatedDate, String author) {
        return scheduleRepository.findAll(updatedDate, author);
    }

    @Transactional
    public ScheduleResponse patchSchedules(Long id, SchedulePatchRequest request) {
        if (scheduleRepository.isPasswordValid(id, request.getPassword())) {
            return scheduleRepository.update(id, request.getTask(), request.getAuthor());
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public void deleteSchedules(Long id, ScheduleDeleteRequest request) {
        if (scheduleRepository.isPasswordValid(id, request.getPassword())) {
            scheduleRepository.delete(id);
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
