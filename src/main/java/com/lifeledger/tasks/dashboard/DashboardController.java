package com.lifeledger.tasks.dashboard;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/*
 * ============================================================
 * DashboardController
 * ============================================================
 *
 * Secured endpoint:
 * GET /api/dashboard/tasks
 *
 * Requires JWT authentication.
 *
 * Filters Supported:
 * - startDate
 * - endDate
 * - taskName
 * - completed
 * - taskCategory
 *
 * User email is extracted from SecurityContext.
 * No email is accepted from client.
 * ============================================================
 */

@RestController
@RequestMapping("/api/dashboard")
@SecurityRequirement(name="bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<ResponseDashboardDTO>> getAllTasks(

            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String taskName,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) List<String> taskCategory) {

        /*
         * Extract authenticated user from SecurityContext
         * This ensures request is based on JWT token.
         */
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String userEmail = authentication.getName();

        // Build filter request object
        RequestDashboardDTO request = new RequestDashboardDTO();
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setTaskName(taskName);
        request.setCompleted(completed);
        request.setTaskCategories(taskCategory);

        return ResponseEntity.ok(
                dashboardService.getAllTasks(userEmail, request));
    }
}
