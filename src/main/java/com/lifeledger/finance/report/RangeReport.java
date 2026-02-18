package com.lifeledger.finance.report;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class RangeReport {

    private String startDate;
    private String endDate;

    private Map<String, BigDecimal> categoryTotals;

    private BigDecimal totalIn;
    private BigDecimal totalOut;
    private BigDecimal net;
}
