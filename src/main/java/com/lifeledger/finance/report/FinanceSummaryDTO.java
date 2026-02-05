package com.lifeledger.finance.report;

import lombok.Data;

@Data
public class FinanceSummaryDTO {
    private double totalIn;
    private double totalOut;
    private double net;
}
