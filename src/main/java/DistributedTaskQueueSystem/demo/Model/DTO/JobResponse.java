package DistributedTaskQueueSystem.demo.Model.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

import DistributedTaskQueueSystem.demo.Enum.Priority;
import DistributedTaskQueueSystem.demo.Enum.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobResponse {
    
    private UUID id;
    private String type;
    private Object payload;
    private Status status;
    private Priority priority;
    private int attemptCount;
    private int maxAttempts;
    private LocalDateTime scheduledAt;
    private LocalDateTime createdAt;   
    private LocalDateTime updatedAt;
}
