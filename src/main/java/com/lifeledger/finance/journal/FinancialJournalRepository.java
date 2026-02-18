package com.lifeledger.finance.journal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository for FinancialJournalEntity
 * JpaSpecificationExecutor enables dynamic filtering using Specifications
 */
public interface FinancialJournalRepository
        extends JpaRepository<FinancialJournalEntity, Long>,
                JpaSpecificationExecutor<FinancialJournalEntity> {   // ✅ ADD THIS

    List<FinancialJournalEntity> findByUserEmail(String userEmail);

    Page<FinancialJournalEntity> findByUserEmailAndEntryDateBetween(
            String userEmail,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    List<FinancialJournalEntity> findByUserEmailAndEntryDateBetween(
            String userEmail,
            LocalDate startDate,
            LocalDate endDate
    );

    Optional<FinancialJournalEntity> findByIdAndUserEmail(
            Long id,
            String userEmail
    );
}
