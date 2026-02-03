package com.lifeledger.finance.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeeklyReport {

    private String weekStart;
    private String weekEnd;

    private Double totalIn;
    private Double totalOut;
}
