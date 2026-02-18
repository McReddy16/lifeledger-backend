package com.lifeledger.finance.journal;

public enum IncomeCategoryEnum {

    SALARY,
    BONUS,
    BUSINESS,
    AGRICULTURE,
    COMMISSION,
    INTEREST,
    SUPPLEMENTARY_CREDITS,
    OTHERINCOME;

    public TransactionTypeEnum getTransactionType() {
        return TransactionTypeEnum.INCOME;
    }
}
