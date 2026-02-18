package com.lifeledger.tasks.tracking;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lifeledger.tasks.reminders.ReminderEntity;

public interface TrackingRepository extends JpaRepository<TrackingEntity,Long> {

	// Fetch all reminder tasks for a specific user
	// where deleted = false (soft-deleted tasks are excluded)
	List<TrackingEntity> findByUserEmailAndDeletedFalse(String userEmail);


    // Fetch a single reminder by id and user email (for security)
    Optional<TrackingEntity> findByIdAndUserEmailAndDeletedFalse(Long id, String userEmail);
    
    // (Optional) keep this if you still need it elsewhere
    Optional<TrackingEntity> findByIdAndUserEmail(Long id, String userEmail);

}
