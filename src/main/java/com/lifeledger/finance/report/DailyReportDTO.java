package com.lifeledger.finance.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyReportDTO {

    private String date;
    private Double totalIn;
    private Double totalOut;
    private Double net;
}
