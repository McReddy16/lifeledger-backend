package com.lifeledger.finance.journal;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RequestJournalEntryDTO {

	  private LocalDate entryDate;
	    private String category;
	    private String description;
	    private Double amount;
	    private MoneyFlowEnum moneyFlow;
	    private PaymentTypeEnum paymentType;
}
