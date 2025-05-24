package dev.jun0.scheduler.schedule;


import dev.jun0.scheduler.schedule.dto.ScheduleCreateRequest;
import dev.jun0.scheduler.schedule.dto.ScheduleResponse;
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
        ScheduleResponse scheduleResponse = scheduleService.createSchedule(request);
        return new ResponseEntity<>(scheduleResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSchedule(@PathVariable Long id) {
        ScheduleResponse scheduleResponse = scheduleService.getSchedule(id);
        return new ResponseEntity<>(scheduleResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getSchedules(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedDate,
                                          @RequestParam(required = false) String author) {
        List<ScheduleResponse> scheduleResponses = scheduleService.getSchedules(updatedDate, author);
        return new ResponseEntity<>(scheduleResponses, HttpStatus.OK);
    }
}
