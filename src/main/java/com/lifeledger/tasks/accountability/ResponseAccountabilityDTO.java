package com.lifeledger.tasks.accountability;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class ResponseAccountabilityDTO {
	
    private Long id;
    private String taskText;
    private boolean completed;
    private LocalDateTime createdAt;
}