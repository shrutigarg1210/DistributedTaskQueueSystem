package DistributedTaskQueueSystem.demo.Service;

import java.util.*;
import org.springframework.stereotype.Service;

import DistributedTaskQueueSystem.demo.Enum.Status;
import DistributedTaskQueueSystem.demo.Model.DTO.DeadLetterJobResponse;
import DistributedTaskQueueSystem.demo.Model.DTO.JobResponse;
import DistributedTaskQueueSystem.demo.Model.Entity.DeadLetterJob;
import DistributedTaskQueueSystem.demo.Model.Entity.Job;
import DistributedTaskQueueSystem.demo.Repository.DeadLetterJobRepository;
import DistributedTaskQueueSystem.demo.Repository.JobRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeadLetterJobService {
   private final DeadLetterJobRepository deadLetterJobRepository;
   private final JobRepository jobRepository;
   private final JobService jobService;

      // Get all dead letter jobs
   public List<DeadLetterJobResponse> getAllDeadLetterJobs() {

       return deadLetterJobRepository.findAll()
               .stream()
               .map(this::mapToResponse)
               .toList();
   }
// Retry a dead letter job — reset it back to PENDING and delete the dead letter record
    public JobResponse retryDeadLetterJob(UUID deadLetterJobId) {
        DeadLetterJob deadLetterJob = deadLetterJobRepository.findById(deadLetterJobId)
                .orElseThrow(() -> new RuntimeException("Dead letter job not found with id: " + deadLetterJobId));

        deadLetterJob.getJob().setStatus(Status.PENDING);
        deadLetterJob.getJob().setAttemptCount(0);
        jobRepository.save(deadLetterJob.getJob());

        deadLetterJobRepository.delete(deadLetterJob);

        log.info("Retried dead letter job with id: {}", deadLetterJob.getJob().getId());
        return jobService.mapToResponse(deadLetterJob.getJob());
    }

    private DeadLetterJobResponse mapToResponse(DeadLetterJob deadLetterJob) {
        return DeadLetterJobResponse.builder()
                .id(deadLetterJob.getId())
                .job_id(deadLetterJob.getJob().getId())
                .jobType(deadLetterJob.getJob().getType())
                .payload(deadLetterJob.getJob().getPayload())
                .failureReason(deadLetterJob.getFailureReason())
                .failedAt(deadLetterJob.getFailedAt())
                .build();
    }
}
