package DistributedTaskQueueSystem.demo.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import DistributedTaskQueueSystem.demo.Model.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Custom query methods (if needed) can be defined here
    Optional<User> findByEmail(String email);
        boolean existsByEmail(String email);
}

