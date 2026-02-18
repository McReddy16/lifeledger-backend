package com.lifeledger.finance.journal;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestJournalEntryDTO {

    private LocalDate entryDate;

    // INCOME / EXPENSE / INVESTMENT
    private TransactionTypeEnum transactionType;

    // Final selected category (SALARY, FOOD, etc.)
    private String category;

    private String description;

    private BigDecimal amount;

    private MoneyFlowEnum moneyFlow;

    private PaymentTypeEnum paymentType;
    
   
}
