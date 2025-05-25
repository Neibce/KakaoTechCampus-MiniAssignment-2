package dev.jun0.scheduler.schedule;


import dev.jun0.scheduler.schedule.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<?> createSchedule(@RequestBody @Valid ScheduleCreateRequest request) {
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
                                          @RequestParam(required = false) String author, Pageable pageable) {
        List<ScheduleAuthorNameResponse> scheduleAuthorNameResponses = scheduleService.getSchedules(updatedDate, author, pageable);
        return new ResponseEntity<>(scheduleAuthorNameResponses, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchSchedules(@PathVariable Long id, @RequestBody SchedulePatchRequest request) {
        ScheduleResponse scheduleResponse = scheduleService.patchSchedules(id, request);
        return new ResponseEntity<>(scheduleResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedules(@PathVariable Long id, @RequestBody ScheduleDeleteRequest request) {
        scheduleService.deleteSchedules(id, request);
        return new ResponseEntity<>("정상적으로 삭제되었습니다.", HttpStatus.OK);
    }
}
