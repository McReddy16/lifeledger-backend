package com.lifeledger.tasks.accountability;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountabilityRepository extends JpaRepository<AccountabilityEntity,Long> {

	// Fetch all reminder tasks for a specific user
	// where deleted = false (soft-deleted tasks are excluded)
	List<AccountabilityEntity> findByUserEmailAndDeletedFalse(String userEmail);


    // Fetch a single reminder by id and user email (for security)
    Optional<AccountabilityEntity> findByIdAndUserEmail(Long id, String userEmail);

}
