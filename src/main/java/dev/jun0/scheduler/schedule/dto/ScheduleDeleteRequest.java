package dev.jun0.scheduler.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleDeleteRequest {
    private String task;
    private String author;
    private String password;
}
