package DistributedTaskQueueSystem.demo.Model.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dead_letter_jobs")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeadLetterJob {
    
    private String type;
    private String payload;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String failureReason;

    //Together they guarantee failedAt always has a value and the database enforces it as a hard rule.
    
    @Column(nullable = false)
    private LocalDateTime failedAt;

    @PrePersist
    protected void onCreate() {
        failedAt = LocalDateTime.now();
    }

}
