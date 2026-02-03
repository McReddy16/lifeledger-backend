package com.lifeledger.finance.report;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class RangeReport {

    private String startDate;
    private String endDate;

    // category -> total amount
    private Map<String, Double> categoryTotals;

    private Double totalIn;
    private Double totalOut;
}
