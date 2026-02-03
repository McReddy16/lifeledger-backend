package com.lifeledger.finance.journal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.lifeledger.exception.BadRequestException;
import com.lifeledger.exception.ConflictException;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor

public class FinancialJournalService {

    private final FinancialJournalRepository journalRepository;

    @Transactional
    public ResponseJournalEntryDTO addEntry(
    		RequestJournalEntryDTO request,
            String userEmail
    ) {
        if (request.getEntryDate() == null) {
            throw new BadRequestException("Entry date is required");
        }

        if (request.getCategory() == null || request.getCategory().isBlank()) {
            throw new BadRequestException("Category is required");
        }

        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new BadRequestException("Amount must be greater than zero");
        }

        if (request.getMoneyFlow() == null) {
            throw new BadRequestException("Money flow (IN/OUT) is required");
        }

        if (request.getPaymentType() == null) {
            throw new BadRequestException("Payment type is required");
        }

        FinancialJournalEntity entity = new FinancialJournalEntity();

        entity.setUserEmail(userEmail);
        entity.setEntryDate(request.getEntryDate());
        entity.setCategory(request.getCategory());
        entity.setDescription(request.getDescription());
        entity.setAmount(request.getAmount());
        entity.setMoneyFlow(request.getMoneyFlow());
        entity.setPaymentType(request.getPaymentType());
        
        FinancialJournalEntity savedEntity = journalRepository.save(entity);
        ResponseJournalEntryDTO response = new ResponseJournalEntryDTO();

        response.setId(savedEntity.getId());
        response.setEntryDate(savedEntity.getEntryDate());
        response.setCategory(savedEntity.getCategory());
        response.setDescription(savedEntity.getDescription());
        response.setAmount(savedEntity.getAmount());
        response.setMoneyFlow(savedEntity.getMoneyFlow());
        response.setPaymentType(savedEntity.getPaymentType());
        response.setCreatedAt(savedEntity.getCreatedAt());

        return response;
    }

    @Transactional(readOnly = true)
    public List<ResponseJournalEntryDTO> getEntriesByDateRange(
            String userEmail,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate == null || endDate == null) {
            throw new BadRequestException("Start date and end date are required");
        }

        if (startDate.isAfter(endDate)) {
            throw new ConflictException("Start date cannot be after end date");
        }
        List<FinancialJournalEntity> entities =
                journalRepository.findByUserEmailAndEntryDateBetween(
                        userEmail,
                        startDate,
                        endDate
                );

        return entities.stream()
                .map(entity -> {
                	ResponseJournalEntryDTO dto = new ResponseJournalEntryDTO();
                    dto.setId(entity.getId());
                    dto.setEntryDate(entity.getEntryDate());
                    dto.setCategory(entity.getCategory());
                    dto.setDescription(entity.getDescription());
                    dto.setAmount(entity.getAmount());
                    dto.setMoneyFlow(entity.getMoneyFlow());
                    dto.setPaymentType(entity.getPaymentType());
                    dto.setCreatedAt(entity.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseJournalEntryDTO updateEntry(
            Long id,
            RequestJournalEntryDTO request,
            String userEmail
    ) {
        // Fetch entry belonging to logged-in user
        FinancialJournalEntity entity =
                journalRepository.findByIdAndUserEmail(id, userEmail)
                        .orElseThrow(() ->
                                new BadRequestException("Entry not found"));

        // Update fields
        entity.setEntryDate(request.getEntryDate());
        entity.setCategory(request.getCategory());
        entity.setDescription(request.getDescription());
        entity.setAmount(request.getAmount());
        entity.setMoneyFlow(request.getMoneyFlow());
        entity.setPaymentType(request.getPaymentType());

        // Save changes
        FinancialJournalEntity updated = journalRepository.save(entity);

        // Prepare response DTO
        ResponseJournalEntryDTO response = new ResponseJournalEntryDTO();
        response.setId(updated.getId());
        response.setEntryDate(updated.getEntryDate());
        response.setCategory(updated.getCategory());
        response.setDescription(updated.getDescription());
        response.setAmount(updated.getAmount());
        response.setMoneyFlow(updated.getMoneyFlow());
        response.setPaymentType(updated.getPaymentType());
        response.setCreatedAt(updated.getCreatedAt());

        return response;
    }
    @Transactional
    public void deleteEntry(Long id, String userEmail) {
        // Fetch entry belonging to logged-in user
        FinancialJournalEntity entity =
                journalRepository.findByIdAndUserEmail(id, userEmail)
                        .orElseThrow(() ->
                                new BadRequestException("Entry not found"));

        // Delete entry
        journalRepository.delete(entity);
    }


    }



