package com.lifeledger.tasks.reminders;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {

    // Fetch all active reminders for user
    List<ReminderEntity> findByUserEmailAndDeletedFalse(String userEmail);

    // Fetch active reminder by id + user (secure + soft delete safe)
    Optional<ReminderEntity> findByIdAndUserEmailAndDeletedFalse(Long id, String userEmail);

    // (Optional) keep this if you still need it elsewhere
    Optional<ReminderEntity> findByIdAndUserEmail(Long id, String userEmail);
}
