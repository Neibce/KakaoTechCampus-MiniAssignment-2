package dev.jun0.scheduler.schedule.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ScheduleResponse {
    private Long id;
    private String task;
    private String authorUuid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

