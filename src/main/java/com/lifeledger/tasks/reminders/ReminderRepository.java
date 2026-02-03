package com.lifeledger.tasks.reminders;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<ReminderEntity,Long> {

	// Fetch all reminder tasks for a specific user
	// where deleted = false (soft-deleted tasks are excluded)
	List<ReminderEntity> findByUserEmailAndDeletedFalse(String userEmail);


    // Fetch a single reminder by id and user email (for security)
    Optional<ReminderEntity> findByIdAndUserEmail(Long id, String userEmail);

}
