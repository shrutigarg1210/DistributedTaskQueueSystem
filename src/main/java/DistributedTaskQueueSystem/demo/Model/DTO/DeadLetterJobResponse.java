package DistributedTaskQueueSystem.demo.Model.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeadLetterJobResponse {
    private UUID id;
    private UUID job_id;
    private String jobType;
    private String payload;
    private String failureReason;
    private LocalDateTime failedAt;
}
