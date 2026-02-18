package com.lifeledger.finance.report;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FinanceSummaryDTO {

    private BigDecimal totalIn;
    private BigDecimal totalOut;
    private BigDecimal net;
}
