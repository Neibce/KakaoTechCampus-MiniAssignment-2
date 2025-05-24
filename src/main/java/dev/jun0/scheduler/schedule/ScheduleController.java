package dev.jun0.scheduler.schedule;


import dev.jun0.scheduler.schedule.dto.ScheduleCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleCreateRequest request) {
        scheduleService.createSchedule(request);
        return new ResponseEntity<>("일정이 생성되었습니다.", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSchedule(@PathVariable Long id) {
        Schedule schedule = scheduleService.getSchedule(id);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getSchedules(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedDate,
                                          @RequestParam(required = false) String author) {
        List<Schedule> schedules = scheduleService.getSchedules(updatedDate, author);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }
}
