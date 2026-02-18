package com.lifeledger.finance.report;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DailyReportDTO {

    private String date;
    private BigDecimal totalIn;
    private BigDecimal totalOut;
    private BigDecimal net;
}
