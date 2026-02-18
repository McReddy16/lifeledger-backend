package com.lifeledger.tasks.timeBlocking;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class ResponseTimeBlockingDTO {
	
    private Long id;
    private String taskText;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDate taskDate;
    private LocalTime startTime;
    private LocalDate endTaskDate;
    private LocalTime endTime;
    private boolean deleted;




}
