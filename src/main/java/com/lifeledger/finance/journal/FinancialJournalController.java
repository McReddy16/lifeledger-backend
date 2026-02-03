package com.lifeledger.finance.journal;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Arrays;


@RestController
@RequestMapping("/api/finance/journal")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")


public class FinancialJournalController {

    private final FinancialJournalService journalService;
    private String getLoggedInUserEmail() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
    @PostMapping("/add")
    public ResponseEntity<ResponseJournalEntryDTO> addEntry(
            @RequestBody RequestJournalEntryDTO request)
    {
    
    String userEmail = getLoggedInUserEmail();
    ResponseJournalEntryDTO response =
            journalService.addEntry(request, userEmail);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
}
    @GetMapping("/range")
    public ResponseEntity<List<ResponseJournalEntryDTO>> getEntriesByDateRange(
            @RequestParam
            @Schema(
                example = "2026-01-28",
                description = "Start date in yyyy-MM-dd format"
            )
            LocalDate startDate,

            @RequestParam
            @Schema(
                example = "2026-01-28",
                description = "End date in yyyy-MM-dd format"
            )
            LocalDate endDate
    )

    {
        String userEmail = getLoggedInUserEmail();
        List<ResponseJournalEntryDTO> entries =
                journalService.getEntriesByDateRange(
                        userEmail,
                        startDate,
                        endDate
                );
        return ResponseEntity.ok(entries);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {

        List<String> categories = Arrays.asList(
                "FOOD",
                "SALARY",
                "TRAVEL",
                "RENT",
                "INVESTMENTS",
                "ONLINESHOPPING",
                "OFFLINESHOPPING",
                "HELPINGHANDS",
                "TEMPLE",
                "SUPPLEMENTARYCREDITS",
                "OTHER"
        );

        return ResponseEntity.ok(categories);
    }
    @GetMapping("/money-flows")
    public ResponseEntity<List<String>> getMoneyFlows() {

        List<String> flows = Arrays.stream(MoneyFlowEnum.values())
                                   .map(Enum::name)
                                   .toList();

        return ResponseEntity.ok(flows);
    }


    @GetMapping("/payment-types")
    public ResponseEntity<List<String>> getPaymentTypes() {

        List<String> paymentTypes = Arrays.stream(PaymentTypeEnum.values())
                                          .map(Enum::name)
                                          .toList();

        return ResponseEntity.ok(paymentTypes);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseJournalEntryDTO> updateEntry(
            @PathVariable Long id,
            @RequestBody RequestJournalEntryDTO request
    ) {
        String userEmail = getLoggedInUserEmail();

        ResponseJournalEntryDTO response =
                journalService.updateEntry(id, request, userEmail);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        String userEmail = getLoggedInUserEmail();

        journalService.deleteEntry(id, userEmail);

        return ResponseEntity.noContent().build();
    }


    }

   



