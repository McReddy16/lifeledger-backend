package com.lifeledger.finance.journal;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseJournalEntryDTO {

    private Long id;
    private LocalDate entryDate;
    private String category;
    private String description;
    private Double amount;
    private MoneyFlowEnum moneyFlow;
    private PaymentTypeEnum paymentType;
    private LocalDateTime createdAt;
    
    
}
