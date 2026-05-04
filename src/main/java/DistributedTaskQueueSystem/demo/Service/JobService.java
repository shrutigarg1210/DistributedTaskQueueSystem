package DistributedTaskQueueSystem.demo.Service;

import org.springframework.stereotype.Service;

import DistributedTaskQueueSystem.demo.Model.DTO.JobRequest;
import DistributedTaskQueueSystem.demo.Model.DTO.JobResponse;
import DistributedTaskQueueSystem.demo.Model.Entity.Job;
import DistributedTaskQueueSystem.demo.Repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {
    
    private final JobRepository jobRepository;

    public JobResponse createJob(JobRequest JobRequest) {
        // Logic to create a new job and save it to the database

        try{
            String payloadString = JobRequest.getPayload().toString();

            Job job = Job.builder()
                .type(JobRequest.getType())
                .payload(payloadString)
                .priority(JobRequest.getPriority())
                .scheduledAt(JobRequest.getScheduledAt())
                .build();

                Job savedJob = jobRepository.save(job);
                log.info("Job created with ID: {}", savedJob.getId());
                return mapToResponse(savedJob);
        }

        catch(Exception e){
            log.error("Error creating job: ", e);
            throw new RuntimeException("Failed to create job" + e.getMessage());
        }
    }

    public JobResponse getJob(UUID jobId) {
        // Logic to retrieve a job by its ID
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));
        
        return mapToResponse(job);
    }

    JobResponse mapToResponse(Job job) {
        // Logic to map Job entity to JobResponse DTO
        return JobResponse.builder()
            .id(job.getId())
            .type(job.getType())
            .payload(job.getPayload())
            .status(job.getStatus())
            .priority(job.getPriority())
            .attemptCount(job.getAttemptCount())
            .maxAttempts(job.getMaxAttempts())
            .scheduledAt(job.getScheduledAt())
            .createdAt(job.getCreatedAt())
            .build();
    }

    public List<JobResponse> getAllJobs() {
    return jobRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
}
}
