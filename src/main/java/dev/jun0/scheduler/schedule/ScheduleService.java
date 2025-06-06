package dev.jun0.scheduler.schedule;

import dev.jun0.scheduler.exception.InvalidPasswordException;
import dev.jun0.scheduler.exception.ScheduleNotFoundException;
import dev.jun0.scheduler.schedule.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

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
                request.getAuthorUuid(),
                request.getPassword()
        );
    }

    @Transactional(readOnly = true)
    public ScheduleResponse getSchedule(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("일정을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<ScheduleAuthorNameResponse> getSchedules(LocalDate updatedDate, String authorUuid, Pageable pageable) {
        return scheduleRepository.findAll(updatedDate, authorUuid, pageable);
    }

    @Transactional
    public ScheduleResponse patchSchedules(Long id, SchedulePatchRequest request) {
        if (scheduleRepository.isPasswordValid(id, request.getPassword())) {
            return scheduleRepository.update(id, request.getTask(), request.getAuthorUuid());
        } else {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public void deleteSchedules(Long id, ScheduleDeleteRequest request) {
        if (scheduleRepository.isPasswordValid(id, request.getPassword())) {
            scheduleRepository.delete(id);
        } else {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }
}
