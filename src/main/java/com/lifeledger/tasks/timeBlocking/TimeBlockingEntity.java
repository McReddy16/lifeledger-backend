package com.lifeledger.tasks.timeBlocking;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="time_blocking_tasks")
@Setter
@Getter
public class TimeBlockingEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private String userEmail;
	
	@Column(nullable=false)
	private String taskText;
	
	@Column(nullable=false)
	private boolean completed=false;
	
	@Column(nullable = false)
	private boolean deleted = false;
	

    @CreationTimestamp
	@Column(nullable=false,updatable=false)
	private LocalDateTime createdAt;
	
    @UpdateTimestamp
    @Column(nullable=false)
	private LocalDateTime updatedAt;
	
    @Column(name = "start_task_date", nullable = false)
    private LocalDate taskDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    
    @Column(name = "end_task_date", nullable = false)
    private LocalDate endTaskDate;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
  

	
}

//completed = true, deleted = false → task is done and visible
//completed = false, deleted = false → task is pending and visible
//completed = true, deleted = true → task was done, then deleted
//completed = false, deleted = true → task was pending, then deleted