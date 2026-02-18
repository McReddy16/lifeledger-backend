package com.lifeledger.finance.journal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_journal")
@Getter
@Setter
public class FinancialJournalEntity {

    // =========================
    // Primary Key
    // =========================
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // Ownership (from JWT/login)
    // =========================
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    // =========================
    // Business Date
    // =========================
    // WHEN the money actually happened
    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    // =========================
    // Journal Details
    // =========================
    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "description")
    private String description;

    // Always POSITIVE (validated in Service layer)
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    // =========================
    // Enums
    // =========================
    @Enumerated(EnumType.STRING)
    @Column(name = "money_flow", nullable = false)
    private MoneyFlowEnum moneyFlow;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentTypeEnum paymentType;

    // =========================
    // System Timestamps (Hibernate-managed)
    // =========================
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionTypeEnum transactionType;

} 
