package com.lifeledger.finance.journal;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class JournalMetaResponse {

    private List<String> transactionTypes;
    private List<String> moneyFlows;
    private List<String> paymentTypes;
    private List<String> expenseGroups;

    private Map<String,List<String>> expenseCategories;

    private List<String> incomeCategories;
    private List<String> investmentCategories;
}
