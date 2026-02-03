package com.lifeledger.tasks.tracking;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.lifeledger.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingRepository trackingRepository;

    // Convert TrackingEntity to ResponseTrackingDTO
    private ResponseTrackingDTO mapToResponse(TrackingEntity tracking) {

        ResponseTrackingDTO dto = new ResponseTrackingDTO();
        dto.setId(tracking.getId());
        dto.setTaskText(tracking.getTaskText());
        dto.setCompleted(tracking.isCompleted());
        dto.setCreatedAt(tracking.getCreatedAt());
        return dto;
    }

    // Fetch all active tracking tasks for a logged-in user
    public List<ResponseTrackingDTO> getTrackingForUser(String userEmail) {

        return trackingRepository
                .findByUserEmailAndDeletedFalse(userEmail)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Create a new tracking task
    public ResponseTrackingDTO createTrackingTask(
            String userEmail,
            RequestTrackingDTO request
    ) {

        TrackingEntity tracking = new TrackingEntity();
        tracking.setUserEmail(userEmail);
        tracking.setTaskText(request.getTaskText());

        TrackingEntity saved = trackingRepository.save(tracking);
        return mapToResponse(saved);
    }

    // Toggle completed flag
    public ResponseTrackingDTO toggleCompleted(Long id, String userEmail) {

        TrackingEntity tracking =
                trackingRepository.findByIdAndUserEmail(id, userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Tracking task not found"));

        tracking.setCompleted(!tracking.isCompleted());

        TrackingEntity updated = trackingRepository.save(tracking);
        return mapToResponse(updated);
    }

    // Soft delete (do not remove from DB)
    public void deleteTracking(Long id, String userEmail) {

        TrackingEntity tracking =
                trackingRepository.findByIdAndUserEmail(id, userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Tracking task not found"));

        tracking.setDeleted(true);
        trackingRepository.save(tracking);
    }
}
