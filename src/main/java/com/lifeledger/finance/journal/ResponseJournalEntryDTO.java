package com.lifeledger.finance.journal;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseJournalEntryDTO {

    private Long id;

    private LocalDate entryDate;

    private TransactionTypeEnum transactionType;

    private String category;

    private String description;

    private BigDecimal amount;

    private MoneyFlowEnum moneyFlow;

    private PaymentTypeEnum paymentType;

    private LocalDateTime createdAt;
}
