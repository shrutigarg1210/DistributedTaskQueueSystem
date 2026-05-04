package DistributedTaskQueueSystem.demo.Service;

import java.util.concurrent.ExecutorService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import DistributedTaskQueueSystem.demo.Enum.Status;
import DistributedTaskQueueSystem.demo.Repository.DeadLetterJobRepository;
import DistributedTaskQueueSystem.demo.Repository.JobRepository;
import lombok.extern.slf4j.Slf4j;
import DistributedTaskQueueSystem.demo.Model.Entity.DeadLetterJob;
import DistributedTaskQueueSystem.demo.Model.Entity.Job;
import java.util.List;
import lombok.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder
public class JobProcessService {
    
    private final JobRepository jobRepository;
    private final DeadLetterJobRepository deadLetterJobRepository;
    private final ExecutorService jobExecutorService;

    @Scheduled(fixedDelay = 5000) // Run every 5 seconds

    public void processPendingJobs(){
        List<Job> pendingJobs = jobRepository.findByStatusOrderByPriorityDescCreatedAtAsc(Status.PENDING);
        
        if(pendingJobs.isEmpty()){
            log.debug("No Pending Jobs");
            return;
        }
        log.info("Found {} pending jobs to process", pendingJobs.size());

        for(Job job: pendingJobs){

            jobExecutorService.submit(()-> processJob(job));
        }
    }

    @Transactional

    public void processJob(Job job){

        log.info("Processing Job ID: {}, Type: {}", job.getId(), job.getType());
          
        job.setStatus(Status.IN_PROGRESS);
        jobRepository.save(job);

        try{

            executeJob(job);
            job.setStatus(Status.COMPLETED);
            jobRepository.save(job);

            log.info(" Job {} completed successfully", job.getId());
        }catch(Exception e){
            log.error("Job {}  failed {}", job.getId(), e.getMessage());
            handleFailure(job, e.getMessage());
           
        }
    }

    public void executeJob(Job job) throws Exception{
        switch (job.getType()) {
            case "EMAIL":
                // Process email job
                log.info("Processing email job with payload: {}", job.getPayload());
                Thread.sleep(500);
                break;
            case "SMS":
                // Process SMS job
                log.info("Processing SMS job with payload: {}", job.getPayload());
                Thread.sleep(500);
                break;
            case "REPORT":
                // Process report generation job
                log.info("Processing report generation job with payload: {}", job.getPayload());
                Thread.sleep(500);
                break;
            case "FAIL_TEST":
                throw new RuntimeException("Simulated failure for testing");
            default:
                log.info("Processing default job with payload: {}", job.getPayload());
                break;
        }
    }

    @Transactional
    public void handleFailure(Job job, String reason){

        job.setAttemptCount(job.getAttemptCount() + 1);

        if(job.getAttemptCount() >= job.getMaxAttempts()){
          
            job.setStatus(Status.FAILED);
            jobRepository.save(job);

            DeadLetterJob deadLetterJob = DeadLetterJob.builder()
            .job(job)
            .failureReason(reason)
            .build();

            deadLetterJobRepository.save(deadLetterJob);

              log.warn("Job ID: {} has reached max retry attempts. Moving to dead letter queue.", job.getId());
            return;
        }
        else{
             job.setStatus(Status.PENDING);
            jobRepository.save(job);
            log.info("Job {} will be retried. Attempt {}/{}",
            job.getId(), job.getAttemptCount(), job.getMaxAttempts());
        }
       
    }

}
