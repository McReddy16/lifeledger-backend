package com.lifeledger.tasks.reminders;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.*;
import com.lifeledger.exception.ResourceNotFoundException;
import java.util.stream.Collectors;
import com.lifeledger.tasks.reminders.RequestReminderDTO;
import com.lifeledger.tasks.reminders.ResponseReminderDTO;



@Service
@RequiredArgsConstructor
public class ReminderService {
	
	private final ReminderRepository reminderRepository;
	
	// Convert ReminderEntity to ResponseReminderDTO
	private ResponseReminderDTO mapToResponse(ReminderEntity reminder) {

	    ResponseReminderDTO dto = new ResponseReminderDTO();
	    dto.setId(reminder.getId());
	    dto.setTaskText(reminder.getTaskText());
	    dto.setCompleted(reminder.isCompleted());
	    dto.setCreatedAt(reminder.getCreatedAt());
	    return dto;
	}

	
	// Fetch all active reminders for a logged-in user (DTO response)
	public List<ResponseReminderDTO> getRemindersForUser(String userEmail) {

	    return reminderRepository
	            .findByUserEmailAndDeletedFalse(userEmail)
	            .stream()
	            .map(this::mapToResponse)
	            .collect(Collectors.toList());
	}

	// Create a new reminder and return DTO
	public ResponseReminderDTO createReminder(
	        String userEmail,
	        RequestReminderDTO request
	) {

	    ReminderEntity reminder = new ReminderEntity();
	    reminder.setUserEmail(userEmail);
	    reminder.setTaskText(request.getTaskText());

	    ReminderEntity saved = reminderRepository.save(reminder);
	    return mapToResponse(saved);
	}
	
	// Toggle completed flag and return updated DTO
	public ResponseReminderDTO toggleCompleted(Long id, String userEmail) {

	    ReminderEntity reminder =
	            reminderRepository.findByIdAndUserEmail(id, userEmail)
	                    .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));

	    reminder.setCompleted(!reminder.isCompleted());

	    ReminderEntity updated = reminderRepository.save(reminder);
	    return mapToResponse(updated);
	}

	
	// Soft delete a reminder for a user (do not remove from DB)
	public void deleteReminder(Long id, String userEmail) {

	    ReminderEntity reminder =
	            reminderRepository.findByIdAndUserEmail(id, userEmail)
	                    .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));

	    reminder.setDeleted(true);

	    reminderRepository.save(reminder);
	}




}
