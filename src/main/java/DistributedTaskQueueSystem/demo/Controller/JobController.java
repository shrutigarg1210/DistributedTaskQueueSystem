package DistributedTaskQueueSystem.demo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import DistributedTaskQueueSystem.demo.Model.DTO.JobRequest;
import DistributedTaskQueueSystem.demo.Model.DTO.JobResponse;
import DistributedTaskQueueSystem.demo.Service.JobService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class JobController {
    
    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest jobRequest) {
        // Logic to handle job creation request
        JobResponse jobResponse = jobService.createJob(jobRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJob(@PathVariable UUID id) {
        // Logic to handle job retrieval request
        JobResponse jobResponse = jobService.getJob(id);
        return ResponseEntity.ok(jobResponse);
    }

    @GetMapping
public ResponseEntity<List<JobResponse>> getAllJobs() {
    List<JobResponse> jobs = jobService.getAllJobs();
    return ResponseEntity.ok(jobs);
}
}
