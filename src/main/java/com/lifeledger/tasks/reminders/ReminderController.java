package com.lifeledger.tasks.reminders;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.lifeledger.tasks.reminders.RequestReminderDTO;
import com.lifeledger.tasks.reminders.ResponseReminderDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;






@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReminderController {
	
	private final ReminderService reminderService;
	
	
	// GET all active reminder tasks for the logged-in user
	@GetMapping
	public List<ResponseReminderDTO> getUserReminders() {

	    String userEmail = SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getName();

	    return reminderService.getRemindersForUser(userEmail);
	}

	// POST create a new reminder task for the logged-in user
	@PostMapping
	public ResponseReminderDTO createReminder(
	        @RequestBody RequestReminderDTO request
	) {
	    String userEmail = SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getName();

	    return reminderService.createReminder(userEmail, request);
	}



	// PUT toggle completed status of a reminder (checkbox click)
	@PutMapping("/{id}/toggle")
	public ResponseReminderDTO toggleReminderCompleted(
	        @PathVariable Long id
	) {
	    String userEmail = SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getName();

	    return reminderService.toggleCompleted(id, userEmail);
	}

	// DELETE (soft delete) a reminder for the logged-in user
	@DeleteMapping("/{id}")
	public void deleteReminder(
	        @PathVariable Long id
	) {
	    String userEmail = SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getName();

	    reminderService.deleteReminder(id, userEmail);
	}



}
