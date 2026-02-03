package com.lifeledger.tasks.reminders;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class ResponseReminderDTO {
	
    private Long id;
    private String taskText;
    private boolean completed;
    private LocalDateTime createdAt;





}
