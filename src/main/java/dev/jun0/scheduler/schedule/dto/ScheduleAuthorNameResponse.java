package dev.jun0.scheduler.schedule.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ScheduleAuthorNameResponse {
    private Long id;
    private String task;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


