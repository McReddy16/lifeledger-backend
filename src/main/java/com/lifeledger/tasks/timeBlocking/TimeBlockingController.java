package com.lifeledger.tasks.timeBlocking;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/timeBlocking")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TimeBlockingController {

    private final TimeBlockingService timeBlockingService;

    // GET all active time blocks for the logged-in user
    @GetMapping
    public List<ResponseTimeBlockingDTO> getUserTimeBlocks() {

        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return timeBlockingService.getTimeBlocksForUser(userEmail);
    }

    // POST create a new time block
    @PostMapping
    public ResponseTimeBlockingDTO createTimeBlock(
            @RequestBody RequestTimeBlockingDTO request
    ) {
        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return timeBlockingService.createTimeBlock(userEmail, request);
    }

    // PUT toggle completed status of a time block
    @PutMapping("/{id}/toggle")
    public ResponseTimeBlockingDTO toggleTimeBlockCompleted(
            @PathVariable Long id
    ) {
        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return timeBlockingService.toggleCompleted(id, userEmail);
    }

    // DELETE (soft delete) a time block
    @DeleteMapping("/{id}")
    public void deleteTimeBlock(
            @PathVariable Long id
    ) {
        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        timeBlockingService.deleteTimeBlock(id, userEmail);
    }
}
