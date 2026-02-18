package com.lifeledger.finance.journal;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/finance/journal")
@RequiredArgsConstructor
@SecurityRequirement(name="bearerAuth")
public class FinancialJournalController {

    private final FinancialJournalService journalService;

    private String getUser(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // ================= CRUD =================

    @PostMapping
    public ResponseEntity<ResponseJournalEntryDTO> add(
            @RequestBody RequestJournalEntryDTO req){
        return new ResponseEntity<>(
                journalService.addEntry(req,getUser()),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseJournalEntryDTO> update(
            @PathVariable Long id,
            @RequestBody RequestJournalEntryDTO req){

        return ResponseEntity.ok(
                journalService.updateEntry(id,req,getUser())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        journalService.deleteEntry(id,getUser());
        return ResponseEntity.noContent().build();
    }

    // ================= SEARCH (MAIN API) =================

    /**
     * Unified filtering endpoint.
     * Used for:
     * - tables
     * - charts
     * - reports
     */
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody JournalSearchRequest request,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue="entryDate") String sortBy,
            @RequestParam(defaultValue="desc") String direction){

        if (size == null || size == 0) {
            return ResponseEntity.ok(
                    journalService.searchAll(getUser(), request, sortBy, direction)
            );
        }

        return ResponseEntity.ok(
                journalService.search(
                        getUser(), request, page, size, sortBy, direction
                )
        );
    }


    // ================= META DATA =================

    /**
     * Returns ALL dropdown info in one call.
     */
    @GetMapping("/meta")
    public ResponseEntity<JournalMetaResponse> meta(){
        return ResponseEntity.ok(journalService.getMeta());
    }
}
