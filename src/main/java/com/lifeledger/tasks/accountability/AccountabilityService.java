package com.lifeledger.tasks.accountability;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.lifeledger.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class AccountabilityService {

    private final AccountabilityRepository accountabilityRepository;

    // Convert AccountabilityEntity to ResponseAccountabilityDTO
    private ResponseAccountabilityDTO mapToResponse(AccountabilityEntity accountability) {

        ResponseAccountabilityDTO dto = new ResponseAccountabilityDTO();
        dto.setId(accountability.getId());
        dto.setTaskText(accountability.getTaskText());
        dto.setCompleted(accountability.isCompleted());
        dto.setCreatedAt(accountability.getCreatedAt());
        return dto;
    }

    // Fetch all active accountability tasks for a logged-in user
    public List<ResponseAccountabilityDTO> getAccountabilityForUser(String userEmail) {

        return accountabilityRepository
                .findByUserEmailAndDeletedFalse(userEmail)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Create a new accountability task
    public ResponseAccountabilityDTO createAccountability(
            String userEmail,
            RequestAccountabilityDTO request
    ) {

        AccountabilityEntity accountability = new AccountabilityEntity();
        accountability.setUserEmail(userEmail);
        accountability.setTaskText(request.getTaskText());

        AccountabilityEntity saved = accountabilityRepository.save(accountability);
        return mapToResponse(saved);
    }

    // Toggle completed flag
    public ResponseAccountabilityDTO toggleCompleted(Long id, String userEmail) {

        AccountabilityEntity accountability =
                accountabilityRepository.findByIdAndUserEmail(id, userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Accountability task not found"));

        accountability.setCompleted(!accountability.isCompleted());

        AccountabilityEntity updated = accountabilityRepository.save(accountability);
        return mapToResponse(updated);
    }

    // Soft delete (do not remove from DB)
    public void deleteAccountability(Long id, String userEmail) {

        AccountabilityEntity accountability =
                accountabilityRepository.findByIdAndUserEmail(id, userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Accountability task not found"));

        accountability.setDeleted(true);
        accountabilityRepository.save(accountability);
    }
}
