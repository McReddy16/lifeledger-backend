package com.lifeledger.tasks.timeBlocking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.lifeledger.exception.BadRequestException;
import com.lifeledger.exception.ResourceNotFoundException;
import com.lifeledger.tasks.tracking.TrackingEntity;

@Service
@RequiredArgsConstructor
public class TimeBlockingService {

    private final TimeBlockingRepository timeBlockingRepository;

    private ResponseTimeBlockingDTO mapToResponse(TimeBlockingEntity timeBlock) {

        ResponseTimeBlockingDTO dto = new ResponseTimeBlockingDTO();
        dto.setId(timeBlock.getId());
        dto.setTaskText(timeBlock.getTaskText());
        dto.setCompleted(timeBlock.isCompleted());
        dto.setCreatedAt(timeBlock.getCreatedAt());
        dto.setTaskDate(timeBlock.getTaskDate());
        dto.setEndTaskDate(timeBlock.getEndTaskDate());
        dto.setStartTime(timeBlock.getStartTime());
        dto.setEndTime(timeBlock.getEndTime());
        dto.setDeleted(timeBlock.isDeleted());

        return dto;
    }

    public List<ResponseTimeBlockingDTO> getTimeBlocksForUser(String userEmail) {

        return timeBlockingRepository
                .findByUserEmailAndDeletedFalse(userEmail)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /*
     * CREATE
     */
    public ResponseTimeBlockingDTO createTimeBlock(
            String userEmail,
            CreateTimeBlockingDTO request
    ) {

        if (request.getDescription() == null ||
            request.getDescription().trim().isEmpty()) {

            throw new BadRequestException("Description cannot be empty");
        }

        validateTimeBlock(
                request.getTaskDate(),
                request.getStartTime(),
                request.getEndTaskDate(),
                request.getEndTime()
        );

        TimeBlockingEntity timeBlock = new TimeBlockingEntity();
        timeBlock.setUserEmail(userEmail);
        timeBlock.setTaskText(request.getDescription());
        timeBlock.setTaskDate(request.getTaskDate());
        timeBlock.setEndTaskDate(request.getEndTaskDate());
        timeBlock.setStartTime(request.getStartTime());
        timeBlock.setEndTime(request.getEndTime());
        timeBlock.setCompleted(false);
        timeBlock.setDeleted(false);

        return mapToResponse(timeBlockingRepository.save(timeBlock));
    }

    /*
     * UPDATE
     */
    public ResponseTimeBlockingDTO updateTimeBlock(
            Long id,
            String userEmail,
            UpdateTimeBlockingDTO dto
    ) {

        TimeBlockingEntity task =
                timeBlockingRepository
                        .findByIdAndUserEmailAndDeletedFalse(id, userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Time block not found"));

        if (dto.getTaskDate() == null ||
            dto.getStartTime() == null ||
            dto.getEndTaskDate() == null ||
            dto.getEndTime() == null) {

            throw new BadRequestException(
                    "Start and end date/time are mandatory");
        }

        validateTimeBlock(
                dto.getTaskDate(),
                dto.getStartTime(),
                dto.getEndTaskDate(),
                dto.getEndTime()
        );

        if (dto.getDescription() != null &&
            !dto.getDescription().trim().isEmpty()) {

            task.setTaskText(dto.getDescription());
        }

        task.setTaskDate(dto.getTaskDate());
        task.setEndTaskDate(dto.getEndTaskDate());
        task.setStartTime(dto.getStartTime());
        task.setEndTime(dto.getEndTime());

        return mapToResponse(timeBlockingRepository.save(task));
    }

    /*
     * VALIDATION (Cross-date aware)
     */
    private void validateTimeBlock(
            LocalDate startDate,
            LocalTime startTime,
            LocalDate endDate,
            LocalTime endTime
    ) {

        if (startDate == null || startTime == null ||
            endDate == null || endTime == null) {

            throw new BadRequestException("All date/time fields are required");
        }

        LocalDateTime startDateTime =
                LocalDateTime.of(startDate, startTime);

        LocalDateTime endDateTime =
                LocalDateTime.of(endDate, endTime);

        if (!endDateTime.isAfter(startDateTime)) {
            throw new BadRequestException(
                    "End date/time must be after start date/time");
        }

        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException(
                    "Cannot schedule event in the past");
        }
    }

    public ResponseTimeBlockingDTO toggleCompleted(Long id, String userEmail) {

        TimeBlockingEntity block =
                timeBlockingRepository
                        .findByIdAndUserEmailAndDeletedFalse(id, userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Time block not found"));

        block.setCompleted(!block.isCompleted());

        return mapToResponse(timeBlockingRepository.save(block));
    }

    public void deleteTimeBlock(Long id, String userEmail) {

        TimeBlockingEntity block =
                timeBlockingRepository
                        .findByIdAndUserEmailAndDeletedFalse(id, userEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Time block not found"));

        block.setDeleted(true);
        timeBlockingRepository.save(block);
    }
    public List<TimeBlockingEntity> getAllForUser(String userEmail) {
        return timeBlockingRepository.findByUserEmailAndDeletedFalse(userEmail);
    }

}
