package DistributedTaskQueueSystem.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import DistributedTaskQueueSystem.demo.Model.DTO.DeadLetterJobResponse;
import DistributedTaskQueueSystem.demo.Model.DTO.JobResponse;
import DistributedTaskQueueSystem.demo.Service.DeadLetterJobService;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RestController
@RequestMapping("api/dead-letter-jobs")
@RequiredArgsConstructor
public class DeadLetterJobController {
    private final DeadLetterJobService deadLetterJobService;

    @GetMapping

    public ResponseEntity<List<DeadLetterJobResponse>> getAllDeadLetterJobs(){
        return ResponseEntity.ok(deadLetterJobService.getAllDeadLetterJobs());
    }

    @PostMapping("/{id}/retry")
    public ResponseEntity<JobResponse> retryDeadLetterJob(@PathVariable UUID id){
        return ResponseEntity.ok(deadLetterJobService.retryDeadLetterJob(id));
    }

}
