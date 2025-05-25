package dev.jun0.scheduler.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class User {
    private UUID uuid;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
