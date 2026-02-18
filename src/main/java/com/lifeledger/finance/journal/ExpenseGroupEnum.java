package com.lifeledger.finance.journal;

public enum ExpenseGroupEnum {

    LIVING,
    TRANSPORT,
    HEALTH,
    SHOPPING,
    SOCIAL,
    GIVING,
    GROWTH,
    OTHEREXPENSE;

    public TransactionTypeEnum getTransactionType() {
        return TransactionTypeEnum.EXPENSE;
    }
}
