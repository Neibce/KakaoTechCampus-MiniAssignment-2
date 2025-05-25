package dev.jun0.scheduler.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SchedulePatchRequest {
    private String task;
    private String authorUuid;
    private String password;
}
