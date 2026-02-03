package com.lifeledger.tasks.timeBlocking;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.lifeledger.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class TimeBlockingService {

    private final TimeBlockingRepository timeBlockingRepository;

    // Convert TimeBlockingEntity to ResponseTimeBlockingDTO
    private ResponseTimeBlockingDTO mapToResponse(TimeBlockingEntity timeBlock) {

        ResponseTimeBlockingDTO dto = new ResponseTimeBlockingDTO();
        dto.setId(timeBlock.getId());
        dto.setTaskText(timeBlock.getTaskText());
        dto.setCompleted(timeBlock.isCompleted());
        dto.setCreatedAt(timeBlock.getCreatedAt());
        return dto;
    }

    // Fetch all active time blocks for a logged-in user
    public List<ResponseTimeBlockingDTO> getTimeBlocksForUser(String userEmail) {

        return timeBlockingRepository
                .findByUserEmailAndDeletedFalse(userEmail)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Create a new time block
    public ResponseTimeBlockingDTO createTimeBlock(
            String userEmail,
            RequestTimeBlockingDTO request
    ) {

        TimeBlockingEntity timeBlock = new TimeBlockingEntity();
        timeBlock.setUserEmail(userEmail);
        timeBlock.setTaskText(request.getTaskText());

        TimeBlockingEntity saved = timeBlockingRepository.save(timeBlock);
        return mapToResponse(saved);
    }

    // Toggle completed flag
    public ResponseTimeBlockingDTO toggleCompleted(Long id, String userEmail) {

        TimeBlockingEntity timeBlock =
                timeBlockingRepository.findByIdAndUserEmail(id, userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Time block not found"));

        timeBlock.setCompleted(!timeBlock.isCompleted());

        TimeBlockingEntity updated = timeBlockingRepository.save(timeBlock);
        return mapToResponse(updated);
    }

    // Soft delete (do not remove from DB)
    public void deleteTimeBlock(Long id, String userEmail) {

        TimeBlockingEntity timeBlock =
                timeBlockingRepository.findByIdAndUserEmail(id, userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Time block not found"));

        timeBlock.setDeleted(true);
        timeBlockingRepository.save(timeBlock);
    }
}
