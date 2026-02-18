package com.lifeledger.tasks.tracking;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TrackingController {

    private final TrackingService trackingService;

    // GET all active tracking tasks for the logged-in user
    @GetMapping
    public List<ResponseTrackingDTO> getUserTrackingTasks() {

        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return trackingService.getTrackingForUser(userEmail);
    }

    // POST create a new tracking task for the logged-in user
    @PostMapping
    public ResponseTrackingDTO createTracking(
            @RequestBody RequestTrackingDTO request
    ) {
        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return trackingService.createTrackingTask(userEmail, request);
    }

    // PUT toggle completed status of a tracking task
    @PutMapping("/{id}/toggle")
    public ResponseTrackingDTO toggleTrackingCompleted(
            @PathVariable Long id
    ) {
        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return trackingService.toggleCompleted(id, userEmail);
    }

    // DELETE (soft delete) a tracking task for the logged-in user
    @DeleteMapping("/{id}")
    public void deleteTracking(
            @PathVariable Long id
    ) {
        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        trackingService.deleteTracking(id, userEmail);
    }
    // ===============================
    // PUT edit tracking description
    // ===============================
    @PutMapping("/{id}")
    public ResponseTrackingDTO updateTrackingDescription(
            @PathVariable Long id,
            @RequestBody UpdateTrackingDTO dto
    ) {
        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return trackingService.updateDescription(id, userEmail, dto);
    }

}
