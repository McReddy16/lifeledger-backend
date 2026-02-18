package com.lifeledger.tasks.accountability;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountabilityRepository
        extends JpaRepository<AccountabilityEntity, Long> {

    // Fetch all active accountability tasks for a user
    List<AccountabilityEntity> findByUserEmailAndDeletedFalse(String userEmail);

    // Fetch active accountability task by id + user (secure + soft delete safe)
    Optional<AccountabilityEntity> findByIdAndUserEmailAndDeletedFalse(
            Long id,
            String userEmail
    );

    // Optional: keep ONLY if needed elsewhere
    Optional<AccountabilityEntity> findByIdAndUserEmail(
            Long id,
            String userEmail
    );
}
