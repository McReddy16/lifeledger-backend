package com.lifeledger.finance.journal;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

/** Payload for unified search filtering */
@Getter
@Setter
public class JournalSearchRequest {

    private LocalDate startDate;
    private LocalDate endDate;

    private MoneyFlowEnum moneyFlow;

    private ExpenseGroupEnum categoryGroup;

    private List<String> categories;
}
