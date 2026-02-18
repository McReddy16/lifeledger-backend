package com.lifeledger.finance.journal;

public enum InvestmentCategoryEnum {

    INVESTMENTS,
    SAVINGS,
    STOCKS,
    MUTUALFUNDS,
    FIXEDDEPOSIT,
    RECURRINGDEPOSIT,
    CRYPTO,
    OTHERINVESTMENT;

    public TransactionTypeEnum getTransactionType() {
        return TransactionTypeEnum.INVESTMENT;
    }
}
