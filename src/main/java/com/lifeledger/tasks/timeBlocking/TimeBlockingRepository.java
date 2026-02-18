package com.lifeledger.tasks.timeBlocking;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lifeledger.tasks.accountability.AccountabilityEntity;

public interface TimeBlockingRepository extends JpaRepository<TimeBlockingEntity,Long> {

	// Fetch all reminder tasks for a specific user
	// where deleted = false (soft-deleted tasks are excluded)
	List<TimeBlockingEntity> findByUserEmailAndDeletedFalse(String userEmail);


    // Fetch a single reminder by id and user email (for security)
    Optional<TimeBlockingEntity> findByIdAndUserEmailAndDeletedFalse(Long id, String userEmail);
    
 // Optional: keep ONLY if needed elsewhere
    Optional<TimeBlockingEntity> findByIdAndUserEmail(
            Long id,
            String userEmail
    );
}






