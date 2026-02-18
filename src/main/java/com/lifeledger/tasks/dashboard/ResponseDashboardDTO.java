package com.lifeledger.tasks.dashboard;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/*
 * Unified DTO used for dashboard
 * All modules are converted into this format
 */
@Getter
@Setter
public class ResponseDashboardDTO {

    private Long id;
    private String taskName;
    private LocalDate taskDate;
    private Boolean completed;
    private String taskType; // TRACKING / TIME_BLOCKING / REMINDER / ACCOUNTABILITY
}
