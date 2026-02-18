package com.lifeledger.finance.journal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.lifeledger.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

@Service
@RequiredArgsConstructor
public class FinancialJournalService {

    private final FinancialJournalRepository journalRepository;

    // =====================================================
    // ADD ENTRY
    // =====================================================

    @Transactional
    public ResponseJournalEntryDTO addEntry(
            RequestJournalEntryDTO request,
            String userEmail
    ) {

        validateRequest(request);
        validateCategory(request.getTransactionType(), request.getCategory());

        FinancialJournalEntity entity = new FinancialJournalEntity();

        entity.setUserEmail(userEmail);
        entity.setEntryDate(request.getEntryDate());
        entity.setTransactionType(request.getTransactionType());
        entity.setCategory(request.getCategory());
        entity.setDescription(request.getDescription());
        entity.setAmount(request.getAmount());
        entity.setMoneyFlow(request.getMoneyFlow());
        entity.setPaymentType(request.getPaymentType());

        return mapToDTO(journalRepository.save(entity));
    }

    // =====================================================
    // SEARCH (Paginated)
    // =====================================================

    @Transactional(readOnly = true)
    public Page<ResponseJournalEntryDTO> search(
            String userEmail,
            JournalSearchRequest req,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = buildSort(sortBy, direction);
        PageRequest pageable = PageRequest.of(page, size, sort);

        // Reuse unified specification builder
        Specification<FinancialJournalEntity> spec =
                buildSpecification(userEmail, req);

        return journalRepository.findAll(spec, pageable)
                .map(this::mapToDTO);
    }

    // =====================================================
    // SEARCH ALL (No Pagination - For Charts)
    // =====================================================

    @Transactional(readOnly = true)
    public List<ResponseJournalEntryDTO> searchAll(
            String userEmail,
            JournalSearchRequest req,
            String sortBy,
            String direction
    ) {

        Sort sort = buildSort(sortBy, direction);

        // Reuse same filtering logic
        Specification<FinancialJournalEntity> spec =
                buildSpecification(userEmail, req);

        return journalRepository.findAll(spec, sort)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // =====================================================
    // SPECIFICATION BUILDER (Single Source of Truth)
    // =====================================================

    private Specification<FinancialJournalEntity> buildSpecification(
            String userEmail,
            JournalSearchRequest req
    ) {

        Specification<FinancialJournalEntity> spec =
                (root, q, cb) -> cb.equal(root.get("userEmail"), userEmail);

        // Date range filter
        if (req.getStartDate() != null)
            spec = spec.and((r, q, cb) ->
                    cb.greaterThanOrEqualTo(r.get("entryDate"), req.getStartDate()));

        if (req.getEndDate() != null)
            spec = spec.and((r, q, cb) ->
                    cb.lessThanOrEqualTo(r.get("entryDate"), req.getEndDate()));

        // Money flow filter
        if (req.getMoneyFlow() != null)
            spec = spec.and((r, q, cb) ->
                    cb.equal(r.get("moneyFlow"), req.getMoneyFlow()));

        // Expense group filter
        if (req.getCategoryGroup() != null) {

            List<String> groupCats =
                    Arrays.stream(ExpenseEnum.values())
                            .filter(e -> e.getGroup() == req.getCategoryGroup())
                            .map(Enum::name)
                            .toList();

            if (!groupCats.isEmpty())
                spec = spec.and((r, q, cb) ->
                        r.get("category").in(groupCats));
        }

        // Specific category filter
        if (req.getCategories() != null && !req.getCategories().isEmpty())
            spec = spec.and((r, q, cb) ->
                    r.get("category").in(req.getCategories()));

        return spec;
    }

    // =====================================================
    // META DATA
    // =====================================================

    @Transactional(readOnly = true)
    public JournalMetaResponse getMeta() {

        JournalMetaResponse meta = new JournalMetaResponse();

        meta.setTransactionTypes(
                Arrays.stream(TransactionTypeEnum.values())
                        .map(Enum::name)
                        .toList());

        meta.setMoneyFlows(
                Arrays.stream(MoneyFlowEnum.values())
                        .map(Enum::name)
                        .toList());

        meta.setPaymentTypes(
                Arrays.stream(PaymentTypeEnum.values())
                        .map(Enum::name)
                        .toList());

        meta.setExpenseGroups(
                Arrays.stream(ExpenseGroupEnum.values())
                        .map(Enum::name)
                        .toList());

        Map<String, List<String>> groupMap =
                Arrays.stream(ExpenseGroupEnum.values())
                        .collect(Collectors.toMap(
                                Enum::name,
                                g -> Arrays.stream(ExpenseEnum.values())
                                        .filter(e -> e.getGroup() == g)
                                        .map(Enum::name)
                                        .toList()
                        ));

        meta.setExpenseCategories(groupMap);

        meta.setIncomeCategories(
                Arrays.stream(IncomeCategoryEnum.values())
                        .map(Enum::name)
                        .toList());

        meta.setInvestmentCategories(
                Arrays.stream(InvestmentCategoryEnum.values())
                        .map(Enum::name)
                        .toList());

        return meta;
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public ResponseJournalEntryDTO updateEntry(
            Long id,
            RequestJournalEntryDTO request,
            String userEmail
    ) {

        FinancialJournalEntity entity =
                journalRepository.findByIdAndUserEmail(id, userEmail)
                        .orElseThrow(() ->
                                new BadRequestException("Entry not found"));

        validateRequest(request);
        validateCategory(request.getTransactionType(), request.getCategory());

        entity.setEntryDate(request.getEntryDate());
        entity.setTransactionType(request.getTransactionType());
        entity.setCategory(request.getCategory());
        entity.setDescription(request.getDescription());
        entity.setAmount(request.getAmount());
        entity.setMoneyFlow(request.getMoneyFlow());
        entity.setPaymentType(request.getPaymentType());

        return mapToDTO(journalRepository.save(entity));
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Transactional
    public void deleteEntry(Long id, String userEmail) {

        FinancialJournalEntity entity =
                journalRepository.findByIdAndUserEmail(id, userEmail)
                        .orElseThrow(() ->
                                new BadRequestException("Entry not found"));

        journalRepository.delete(entity);
    }

    // =====================================================
    // VALIDATION
    // =====================================================

    private void validateRequest(RequestJournalEntryDTO request) {

        if (request.getEntryDate() == null)
            throw new BadRequestException("Entry date is required");

        if (request.getTransactionType() == null)
            throw new BadRequestException("Transaction type is required");

        if (request.getCategory() == null || request.getCategory().isBlank())
            throw new BadRequestException("Category is required");

        if (request.getAmount() == null ||
                request.getAmount().compareTo(java.math.BigDecimal.ZERO) < 0)
            throw new BadRequestException("Amount cannot be negative");

        if (request.getMoneyFlow() == null)
            throw new BadRequestException("Money flow is required");

        if (request.getPaymentType() == null)
            throw new BadRequestException("Payment type is required");
    }

    private void validateCategory(TransactionTypeEnum type, String category) {

        boolean valid = switch (type) {

            case INCOME ->
                    Arrays.stream(IncomeCategoryEnum.values())
                            .anyMatch(e -> e.name().equals(category));

            case EXPENSE ->
                    Arrays.stream(ExpenseEnum.values())
                            .anyMatch(e -> e.name().equals(category));

            case INVESTMENT ->
                    Arrays.stream(InvestmentCategoryEnum.values())
                            .anyMatch(e -> e.name().equals(category));

            case OTHER -> true;

            default -> false;
        };

        if (!valid)
            throw new BadRequestException("Invalid category for selected transaction type");
    }

    private Sort buildSort(String sortBy, String direction) {

        if ("entryDate".equalsIgnoreCase(sortBy)) {
            return direction.equalsIgnoreCase("asc")
                    ? Sort.by(Sort.Order.asc("entryDate"), Sort.Order.asc("createdAt"))
                    : Sort.by(Sort.Order.desc("entryDate"), Sort.Order.desc("createdAt"));
        }

        return direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
    }

    private ResponseJournalEntryDTO mapToDTO(FinancialJournalEntity entity) {

        ResponseJournalEntryDTO dto = new ResponseJournalEntryDTO();

        dto.setId(entity.getId());
        dto.setEntryDate(entity.getEntryDate());
        dto.setTransactionType(entity.getTransactionType());
        dto.setCategory(entity.getCategory());
        dto.setDescription(entity.getDescription());
        dto.setAmount(entity.getAmount());
        dto.setMoneyFlow(entity.getMoneyFlow());
        dto.setPaymentType(entity.getPaymentType());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }
}
