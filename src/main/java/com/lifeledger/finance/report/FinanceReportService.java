package com.lifeledger.finance.report;

import com.lifeledger.finance.journal.FinancialJournalEntity;
import com.lifeledger.finance.journal.FinancialJournalRepository;
import com.lifeledger.finance.journal.MoneyFlowEnum;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinanceReportService {

    private final FinancialJournalRepository journalRepository;

    // ===============================
    // DAILY REPORT
    // ===============================

    @Transactional(readOnly = true)
    public DailyReportDTO getDailyReport(String userEmail, LocalDate date) {

        List<FinancialJournalEntity> entries =
                journalRepository.findByUserEmailAndEntryDateBetween(
                        userEmail, date, date
                );

        BigDecimal totalIn = entries.stream()
                .filter(e -> e.getMoneyFlow() == MoneyFlowEnum.IN)
                .map(FinancialJournalEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOut = entries.stream()
                .filter(e -> e.getMoneyFlow() == MoneyFlowEnum.OUT)
                .map(FinancialJournalEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        DailyReportDTO dto = new DailyReportDTO();
        dto.setDate(date.toString());
        dto.setTotalIn(totalIn);
        dto.setTotalOut(totalOut);
        dto.setNet(totalIn.subtract(totalOut));

        return dto;
    }

    // ===============================
    // RANGE REPORT
    // ===============================

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

        Map<String, BigDecimal> categoryTotals =
                entries.stream()
                        .filter(e -> e.getMoneyFlow() == MoneyFlowEnum.OUT)
                        .collect(Collectors.groupingBy(
                                FinancialJournalEntity::getCategory,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        FinancialJournalEntity::getAmount,
                                        BigDecimal::add
                                )
                        ));

        BigDecimal totalIn = entries.stream()
                .filter(e -> e.getMoneyFlow() == MoneyFlowEnum.IN)
                .map(FinancialJournalEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOut = entries.stream()
                .filter(e -> e.getMoneyFlow() == MoneyFlowEnum.OUT)
                .map(FinancialJournalEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        RangeReport report = new RangeReport();
        report.setStartDate(start.toString());
        report.setEndDate(end.toString());
        report.setCategoryTotals(categoryTotals);
        report.setTotalIn(totalIn);
        report.setTotalOut(totalOut);
        report.setNet(totalIn.subtract(totalOut));

        return report;
    }

    // ===============================
    // SUMMARY REPORT
    // ===============================

    @Transactional(readOnly = true)
    public FinanceSummaryDTO getSummary(
            String userEmail,
            LocalDate startDate,
            LocalDate endDate
    ) {

        List<FinancialJournalEntity> entries =
                journalRepository.findByUserEmailAndEntryDateBetween(
                        userEmail, startDate, endDate
                );

        BigDecimal totalIn = entries.stream()
                .filter(e -> e.getMoneyFlow() == MoneyFlowEnum.IN)
                .map(FinancialJournalEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOut = entries.stream()
                .filter(e -> e.getMoneyFlow() == MoneyFlowEnum.OUT)
                .map(FinancialJournalEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        FinanceSummaryDTO dto = new FinanceSummaryDTO();
        dto.setTotalIn(totalIn);
        dto.setTotalOut(totalOut);
        dto.setNet(totalIn.subtract(totalOut));

        return dto;
    }
}
