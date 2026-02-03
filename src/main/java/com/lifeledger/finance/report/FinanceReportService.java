package com.lifeledger.finance.report;


import com.lifeledger.finance.journal.FinancialJournalEntity;
import com.lifeledger.finance.journal.FinancialJournalRepository;
import com.lifeledger.finance.journal.MoneyFlowEnum;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinanceReportService {

    private final FinancialJournalRepository journalRepository;
    @Transactional(readOnly = true)
    public DailyReportDTO getDailyReport(String userEmail, LocalDate date) {

        List<FinancialJournalEntity> entries =
                journalRepository.findByUserEmailAndEntryDateBetween(
                        userEmail, date, date
                );

        double totalIn = entries.stream()
                .filter(e -> e.getMoneyFlow() == MoneyFlowEnum.IN)
                .mapToDouble(FinancialJournalEntity::getAmount)
                .sum();

        double totalOut = entries.stream()
                .filter(e -> e.getMoneyFlow() == MoneyFlowEnum.OUT)
                .mapToDouble(FinancialJournalEntity::getAmount)
                .sum();

        DailyReportDTO dto = new DailyReportDTO();
        dto.setDate(date.toString());
        dto.setTotalIn(totalIn);
        dto.setTotalOut(totalOut);

        return dto;
    }
    @Transactional(readOnly = true)
    public RangeReport getRangeReport(
            String userEmail,
            LocalDate start,
            LocalDate end
    ) {

        List<FinancialJournalEntity> entries =
                journalRepository.findByUserEmailAndEntryDateBetween(
                        userEmail, start, end
                );

        Map<String, Double> categoryTotals =
                entries.stream()
                        .filter(e -> e.getMoneyFlow() == MoneyFlowEnum.OUT)
                        .collect(Collectors.groupingBy(
                                FinancialJournalEntity::getCategory,
                                Collectors.summingDouble(
                                        FinancialJournalEntity::getAmount
                                )
                        ));

        double totalIn = entries.stream()
                .filter(e -> e.getMoneyFlow() == MoneyFlowEnum.IN)
                .mapToDouble(FinancialJournalEntity::getAmount)
                .sum();

        double totalOut = entries.stream()
                .filter(e -> e.getMoneyFlow() == MoneyFlowEnum.OUT)
                .mapToDouble(FinancialJournalEntity::getAmount)
                .sum();

        RangeReport report = new RangeReport();
        report.setStartDate(start.toString());
        report.setEndDate(end.toString());
        report.setCategoryTotals(categoryTotals);
        report.setTotalIn(totalIn);
        report.setTotalOut(totalOut);

        return report;
    }
}


