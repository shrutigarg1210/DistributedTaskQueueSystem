package DistributedTaskQueueSystem.demo.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import DistributedTaskQueueSystem.demo.Model.Entity.DeadLetterJob;

@Repository
public interface DeadLetterJobRepository extends JpaRepository<DeadLetterJob, UUID> {
    // Custom query methods (if needed) can be defined here
}