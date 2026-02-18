package com.lifeledger.tasks.reminders;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.lifeledger.exception.ResourceNotFoundException;
import com.lifeledger.tasks.tracking.TrackingEntity;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;

    // Convert entity → response DTO
    private ResponseReminderDTO mapToResponse(ReminderEntity reminder) {

        ResponseReminderDTO dto = new ResponseReminderDTO();
        dto.setId(reminder.getId());
        dto.setTaskText(reminder.getTaskText());
        dto.setCompleted(reminder.isCompleted());
        dto.setCreatedAt(reminder.getCreatedAt());
        return dto;
    }

    // Fetch all active reminders for user
    public List<ResponseReminderDTO> getRemindersForUser(String userEmail) {

        return reminderRepository
                .findByUserEmailAndDeletedFalse(userEmail)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Create reminder
    public ResponseReminderDTO createReminder(
            String userEmail,
            RequestReminderDTO request
    ) {
        ReminderEntity reminder = new ReminderEntity();
        reminder.setUserEmail(userEmail);
        reminder.setTaskText(request.getTaskText());

        return mapToResponse(reminderRepository.save(reminder));
    }

    // Toggle completed
    public ResponseReminderDTO toggleCompleted(Long id, String userEmail) {

        ReminderEntity reminder =
                reminderRepository.findByIdAndUserEmailAndDeletedFalse(id, userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Reminder not found"));

        reminder.setCompleted(!reminder.isCompleted());

        return mapToResponse(reminderRepository.save(reminder));
    }

    // Soft delete
    public void deleteReminder(Long id, String userEmail) {

        ReminderEntity reminder =
                reminderRepository.findByIdAndUserEmailAndDeletedFalse(id, userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Reminder not found"));

        reminder.setDeleted(true);
        reminderRepository.save(reminder);
    }

    // ✅ EDIT task text (description)
    public ResponseReminderDTO updateDescription(
            Long id,
            String userEmail,
            UpdateReminderDTO dto
    ) {
        ReminderEntity reminder =
                reminderRepository.findByIdAndUserEmailAndDeletedFalse(id, userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Reminder not found"));

        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Task text cannot be empty");
        }

        // IMPORTANT: taskText is your description
        reminder.setTaskText(dto.getDescription());

        return mapToResponse(reminderRepository.save(reminder));
    }
    
    public List<ReminderEntity> getAllForUser(String userEmail) {
        return reminderRepository.findByUserEmailAndDeletedFalse(userEmail);
    }

}
