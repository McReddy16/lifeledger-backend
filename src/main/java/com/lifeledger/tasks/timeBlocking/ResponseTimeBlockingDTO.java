package com.lifeledger.tasks.timeBlocking;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class ResponseTimeBlockingDTO {
	
    private Long id;
    private String taskText;
    private boolean completed;
    private LocalDateTime createdAt;





}
