package DistributedTaskQueueSystem.demo.Model.DTO;

import java.time.LocalDateTime;

import DistributedTaskQueueSystem.demo.Enum.Priority;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobRequest {

    @NotNull(message = "Job type is required")
    private String type;

    @NotNull(message = "Job payload is required")
    private Object payload;

    private Priority priority = Priority.NORMAL; // Default priority
    private LocalDateTime scheduledAt; // Optional scheduling time

}
