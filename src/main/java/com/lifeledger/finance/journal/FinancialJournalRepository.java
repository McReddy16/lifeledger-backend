package com.lifeledger.finance.journal;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FinancialJournalRepository
        extends JpaRepository<FinancialJournalEntity, Long> {

    List<FinancialJournalEntity> findByUserEmail(String userEmail);

    // For TABLE (pagination)
    Page<FinancialJournalEntity> findByUserEmailAndEntryDateBetween(
            String userEmail,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    // For REPORTS (no pagination)
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
