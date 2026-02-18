package com.lifeledger.finance.journal;

public enum ExpenseEnum {

    // 🏠 Living
    RENT(ExpenseGroupEnum.LIVING),
    UTILITIES(ExpenseGroupEnum.LIVING),
    GROCERIES(ExpenseGroupEnum.LIVING),
    FOOD(ExpenseGroupEnum.LIVING),
    MAINTENANCE(ExpenseGroupEnum.LIVING),

    // 🚗 Transport
    TRAVEL(ExpenseGroupEnum.TRANSPORT),
    VEHICLE_REPAIRS(ExpenseGroupEnum.TRANSPORT),
    FUEL(ExpenseGroupEnum.TRANSPORT),

    // ❤️ Health
    FITNESS(ExpenseGroupEnum.HEALTH),
    MEDICAL(ExpenseGroupEnum.HEALTH),
    INSURANCE(ExpenseGroupEnum.HEALTH),

    // 🛍 Shopping
    SHOPPING(ExpenseGroupEnum.SHOPPING),
    ONLINE_SHOPPING(ExpenseGroupEnum.SHOPPING),
    OFFLINE_SHOPPING(ExpenseGroupEnum.SHOPPING),

    // 🎉 Social
    ENTERTAINMENT(ExpenseGroupEnum.SOCIAL),
    PARTY(ExpenseGroupEnum.SOCIAL),
    FRIENDS_OUTING(ExpenseGroupEnum.SOCIAL),
    FAMILY_OUTING(ExpenseGroupEnum.SOCIAL),

    // 🙏 Giving
    CHARITY(ExpenseGroupEnum.GIVING),
    HELPING_HANDS(ExpenseGroupEnum.GIVING),
    TEMPLE(ExpenseGroupEnum.GIVING),
    WORKERS_SALARY(ExpenseGroupEnum.GIVING),

    // 📚 Growth
    EDUCATION(ExpenseGroupEnum.GROWTH),
    SUBSCRIPTIONS(ExpenseGroupEnum.GROWTH),

    OTHEREXPENSE(ExpenseGroupEnum. OTHEREXPENSE);

    private final ExpenseGroupEnum group;

    ExpenseEnum(ExpenseGroupEnum group) {
        this.group = group;
    }

    public ExpenseGroupEnum getGroup() {
        return group;
    }

    public TransactionTypeEnum getTransactionType() {
        return TransactionTypeEnum.EXPENSE;
    }
}
