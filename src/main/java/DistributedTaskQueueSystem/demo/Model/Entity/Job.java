package DistributedTaskQueueSystem.demo.Model.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import DistributedTaskQueueSystem.demo.Enum.Status;
import DistributedTaskQueueSystem.demo.Enum.Priority;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String type;

    // Store the payload field as a non-null large text column, typically used to 
    // save JSON data as a plain string for compatibility across databases (like H2).”
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    // In JPA/Hibernate, this is used when you have a Java enum field and you want to 
    // control how it is stored in the database.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Column(nullable = false)
    private int attemptCount;

    @Column(nullable = false)
    private int maxAttempts;

    private LocalDateTime scheduledAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        maxAttempts = 3; // Default max attempts
        attemptCount = 0; // Initialize attempt count
        if(status == null) {
            status = Status.PENDING; // Default status
        }   

        if(priority == null) {
            priority = Priority.NORMAL; // Default priority
        }

        if(maxAttempts <= 0) {
            maxAttempts = 3; // Ensure max attempts is positive
        }

    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
