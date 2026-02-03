package com.lifeledger.tasks.accountability;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/accountability")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AccountabilityController {

    private final AccountabilityService accountabilityService;

    // GET all active accountability tasks for the logged-in user
    @GetMapping
    public List<ResponseAccountabilityDTO> getUserAccountabilityTasks() {

        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return accountabilityService.getAccountabilityForUser(userEmail);
    }

    // POST create a new accountability task
    @PostMapping
    public ResponseAccountabilityDTO createAccountability(
            @RequestBody RequestAccountabilityDTO request
    ) {
        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return accountabilityService.createAccountability(userEmail, request);
    }

    // PUT toggle completed status
    @PutMapping("/{id}/toggle")
    public ResponseAccountabilityDTO toggleAccountabilityCompleted(
            @PathVariable Long id
    ) {
        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return accountabilityService.toggleCompleted(id, userEmail);
    }

    // DELETE (soft delete)
    @DeleteMapping("/{id}")
    public void deleteAccountability(
            @PathVariable Long id
    ) {
        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        accountabilityService.deleteAccountability(id, userEmail);
    }
}
