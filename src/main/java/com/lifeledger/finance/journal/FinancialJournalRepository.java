package com.lifeledger.finance.journal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FinancialJournalRepository
        extends JpaRepository<FinancialJournalEntity, Long> {

    List<FinancialJournalEntity> findByUserEmail(String userEmail);

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
