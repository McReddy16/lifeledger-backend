package com.lifeledger.tasks.timeBlocking;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateTimeBlockingDTO {
	

	 private String description;
     private LocalDate taskDate;
	 private LocalTime startTime;
	 private LocalDate EndTaskDate;
	 private LocalTime endTime;

}
