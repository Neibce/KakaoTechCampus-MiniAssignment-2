package dev.jun0.scheduler.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleCreateRequest {
    @NotBlank
    @Size(max = 200)
    private String task;

    @NotBlank
    private String authorUuid;

    @NotBlank
    private String password;
}
