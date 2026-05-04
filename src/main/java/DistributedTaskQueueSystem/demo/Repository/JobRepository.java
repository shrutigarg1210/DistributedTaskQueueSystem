package DistributedTaskQueueSystem.demo.Repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import DistributedTaskQueueSystem.demo.Enum.Status;
import DistributedTaskQueueSystem.demo.Model.Entity.Job;

import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {
    // Custom query methods (if needed) can be defined here

    @Query("SELECT j FROM Job j WHERE j.status = :status ORDER BY " +
     "CASE j.priority WHEN 'HIGH' THEN 1 WHEN 'NORMAL' THEN 2 WHEN 'LOW' THEN 3 END, " + "j.createdAt ASC")
    List<Job> findByStatusOrderByPriorityDescCreatedAtAsc(Status status);
}
