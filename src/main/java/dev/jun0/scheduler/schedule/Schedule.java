package dev.jun0.scheduler.schedule;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Schedule {
    private Long id;
    private String task;
    private String author;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
