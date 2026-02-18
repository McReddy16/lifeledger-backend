package com.lifeledger.tasks.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lifeledger.tasks.accountability.AccountabilityEntity;
import com.lifeledger.tasks.accountability.AccountabilityService;
import com.lifeledger.tasks.reminders.ReminderEntity;
import com.lifeledger.tasks.reminders.ReminderService;
import com.lifeledger.tasks.timeBlocking.TimeBlockingEntity;
import com.lifeledger.tasks.timeBlocking.TimeBlockingService;
import com.lifeledger.tasks.tracking.TrackingEntity;
import com.lifeledger.tasks.tracking.TrackingService;

/*
 * ============================================================
 * DashboardService
 * ============================================================
 *
 * PURPOSE:
 * Aggregates tasks from all modules into one unified format
 * for dashboard visualization.
 *
 * MODULES INCLUDED:
 * - Tracking
 * - Time Blocking
 * - Reminder
 * - Accountability
 *
 * IMPORTANT:
 * - No DB changes
 * - No entity modifications
 * - No service logic modified
 * - Pure aggregation + filtering layer
 * ============================================================
 */

@Service
public class DashboardService {

    private final TrackingService trackingService;
    private final TimeBlockingService timeBlockingService;
    private final ReminderService reminderService;
    private final AccountabilityService accountabilityService;

    public DashboardService(
            TrackingService trackingService,
            TimeBlockingService timeBlockingService,
            ReminderService reminderService,
            AccountabilityService accountabilityService) {

        this.trackingService = trackingService;
        this.timeBlockingService = timeBlockingService;
        this.reminderService = reminderService;
        this.accountabilityService = accountabilityService;
    }

    /*
     * ============================================================
     * MAIN DASHBOARD METHOD
     * ============================================================
     *
     * Fetches user-specific tasks from all modules,
     * converts them into a unified structure,
     * and applies optional filters.
     */
    public List<ResponseDashboardDTO> getAllTasks(
            String userEmail,
            RequestDashboardDTO request) {

        List<ResponseDashboardDTO> allTasks = new ArrayList<>();

        // =========================
        // Fetch from all modules
        // =========================
        allTasks.addAll(
                mapTracking(trackingService.getAllForUser(userEmail)));

        allTasks.addAll(
                mapTimeBlocking(timeBlockingService.getAllForUser(userEmail)));

        allTasks.addAll(
                mapReminder(reminderService.getAllForUser(userEmail)));

        allTasks.addAll(
                mapAccountability(accountabilityService.getAllForUser(userEmail)));

        // =========================
        // Apply Filters
        // =========================
        return allTasks.stream()

                // Prevent NullPointerException for date
                .filter(task -> task.getTaskDate() != null)

                // Start date filter
                .filter(task -> request.getStartDate() == null
                        || !task.getTaskDate()
                        .isBefore(request.getStartDate()))

                // End date filter
                .filter(task -> request.getEndDate() == null
                        || !task.getTaskDate()
                        .isAfter(request.getEndDate()))

                // Task name search (case insensitive)
                .filter(task -> request.getTaskName() == null
                        || (task.getTaskName() != null
                        && task.getTaskName().toLowerCase()
                        .contains(request.getTaskName().toLowerCase())))

                // Completed filter
                .filter(task -> request.getCompleted() == null
                        || task.getCompleted()
                        .equals(request.getCompleted()))

                // Category filter
                .filter(task -> request.getTaskCategories() == null
                        || request.getTaskCategories().isEmpty()
                        || request.getTaskCategories()
                        .contains(task.getTaskType()))

                .collect(Collectors.toList());
    }

    /*
     * ============================================================
     * Tracking → Dashboard Mapping
     * Uses createdAt as taskDate
     * ============================================================
     */
    private List<ResponseDashboardDTO> mapTracking(
            List<TrackingEntity> list) {

        return list.stream().map(task -> {
            ResponseDashboardDTO dto = new ResponseDashboardDTO();

            dto.setId(task.getId());
            dto.setTaskName(task.getTaskText());

            dto.setTaskDate(
                    task.getCreatedAt() != null
                            ? task.getCreatedAt().toLocalDate()
                            : null);

            dto.setCompleted(task.isCompleted());
            dto.setTaskType("TRACKING");

            return dto;

        }).collect(Collectors.toList());
    }

    /*
     * ============================================================
     * TimeBlocking → Dashboard Mapping
     * Uses taskDate directly
     * ============================================================
     */
    private List<ResponseDashboardDTO> mapTimeBlocking(
            List<TimeBlockingEntity> list) {

        return list.stream().map(task -> {
            ResponseDashboardDTO dto = new ResponseDashboardDTO();

            dto.setId(task.getId());
            dto.setTaskName(task.getTaskText());
            dto.setTaskDate(task.getTaskDate());
            dto.setCompleted(task.isCompleted());
            dto.setTaskType("TIME_BLOCKING");

            return dto;

        }).collect(Collectors.toList());
    }

    /*
     * ============================================================
     * Reminder → Dashboard Mapping
     * Uses createdAt (ReminderEntity has no reminderDate field)
     * ============================================================
     */
    private List<ResponseDashboardDTO> mapReminder(
            List<ReminderEntity> list) {

        return list.stream().map(task -> {
            ResponseDashboardDTO dto = new ResponseDashboardDTO();

            dto.setId(task.getId());
            dto.setTaskName(task.getTaskText());

            dto.setTaskDate(
                    task.getCreatedAt() != null
                            ? task.getCreatedAt().toLocalDate()
                            : null);

            dto.setCompleted(task.isCompleted());
            dto.setTaskType("REMINDER");

            return dto;

        }).collect(Collectors.toList());
    }

    /*
     * ============================================================
     * Accountability → Dashboard Mapping
     * Uses createdAt
     * ============================================================
     */
    private List<ResponseDashboardDTO> mapAccountability(
            List<AccountabilityEntity> list) {

        return list.stream().map(task -> {
            ResponseDashboardDTO dto = new ResponseDashboardDTO();

            dto.setId(task.getId());
            dto.setTaskName(task.getTaskText());

            dto.setTaskDate(
                    task.getCreatedAt() != null
                            ? task.getCreatedAt().toLocalDate()
                            : null);

            dto.setCompleted(task.isCompleted());
            dto.setTaskType("ACCOUNTABILITY");

            return dto;

        }).collect(Collectors.toList());
    }
}
