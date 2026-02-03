package com.lifeledger.finance.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthlyReport {

    private String month; // e.g. 2026-01
    private Double totalIn;
    private Double totalOut;
}
