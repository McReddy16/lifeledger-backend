package com.lifeledger.tasks.tracking;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class ResponseTrackingDTO {
	
    private Long id;
    private String taskText;
    private boolean completed;
    private LocalDateTime createdAt;





}
