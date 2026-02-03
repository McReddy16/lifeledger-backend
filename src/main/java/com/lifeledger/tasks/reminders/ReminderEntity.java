package com.lifeledger.tasks.reminders;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="reminder_tasks")
@Setter
@Getter
public class ReminderEntity {
	
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
	
     

	
}

//completed = true, deleted = false → task is done and visible
//completed = false, deleted = false → task is pending and visible
//completed = true, deleted = true → task was done, then deleted
//completed = false, deleted = true → task was pending, then deleted