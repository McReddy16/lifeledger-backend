package com.lifeledger.finance.report;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/finance/report")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FinanceReportController {

    private final FinanceReportService reportService;
    private String userEmail() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
    @GetMapping("/daily")
    public DailyReportDTO daily(
            @RequestParam LocalDate date
    ) {
        return reportService.getDailyReport(userEmail(), date);
    }
    @GetMapping("/summary")
    public FinanceSummaryDTO summary(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return reportService.getSummary(
                userEmail(), startDate, endDate
        );
    }

}
    


