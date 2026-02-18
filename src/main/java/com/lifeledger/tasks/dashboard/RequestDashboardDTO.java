package com.lifeledger.tasks.dashboard;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*
 * This DTO holds all optional filters
 * If any field is null → filter will be ignored
 */
@Getter
@Setter
public class RequestDashboardDTO {

    // Filter by start date
    private LocalDate startDate;

    // Filter by end date
    private LocalDate endDate;

    // Search by task name (contains)
    private String taskName;

    // Filter by completion status
    private Boolean completed;

    // Filter by categories
    // Allowed values:
    // TRACKING, TIME_BLOCKING, REMINDER, ACCOUNTABILITY
    private List<String> taskCategories;
}
