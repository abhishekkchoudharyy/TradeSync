package com.abhishekkchoudharyy.tradesync.controller;

import com.abhishekkchoudharyy.tradesync.entity.*;
import com.abhishekkchoudharyy.tradesync.repository.*;
import com.abhishekkchoudharyy.tradesync.service.*;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reconcile")
public class ReconciliationController {

    private final DocumentStorageService storageService;
    private final PdfTextExtractionService extractionService;
    private final AIReconciliationService aiService;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final ReconciliationJobRepository jobRepository;
    private final DiscrepancyRepository discrepancyRepository;

    public ReconciliationController(DocumentStorageService storageService, PdfTextExtractionService extractionService,
                                    AIReconciliationService aiService, UserRepository userRepository,
                                    DocumentRepository documentRepository, ReconciliationJobRepository jobRepository,
                                    DiscrepancyRepository discrepancyRepository) {
        this.storageService = storageService;
        this.extractionService = extractionService;
        this.aiService = aiService;
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.jobRepository = jobRepository;
        this.discrepancyRepository = discrepancyRepository;
    }

    // Multi upload
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> reconcileDocuments(
            @RequestParam("quotation") MultipartFile quotationFile,
            @RequestParam("invoice") MultipartFile invoiceFile,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails currentUser) {

        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Logged in auditor user context not found"));


        String poPath = storageService.storeDocument(quotationFile, "QUOTATION");
        String invPath = storageService.storeDocument(invoiceFile, "INVOICE");

        Document poDoc = new Document();
        poDoc.setUploader(user);
        poDoc.setDocumentType("QUOTATION");
        poDoc.setFilePath(poPath);
        documentRepository.save(poDoc);

        Document invDoc = new Document();
        invDoc.setUploader(user);
        invDoc.setDocumentType("INVOICE");
        invDoc.setFilePath(invPath);
        documentRepository.save(invDoc);

        // 4. Track current running pipeline state execution
        ReconciliationJob job = new ReconciliationJob();
        job.setQuotationDocument(poDoc);
        job.setInvoiceDocument(invDoc);
        job.setStatus("PENDING");
        jobRepository.save(job);

        try {
            String poText = extractionService.extractTextFromFile(poPath);
            String invText = extractionService.extractTextFromFile(invPath);


            AIReconciliationService.DiscrepancyReport report = aiService.compareDocuments(poText, invText);


            List<Discrepancy> savedDiscrepancies = new ArrayList<>();
            for (AIReconciliationService.MismatchItem item : report.mismatches()) {
                Discrepancy discrepancy = new Discrepancy();
                discrepancy.setJob(job);
                discrepancy.setFieldName(item.fieldName());
                discrepancy.setQuotationValue(item.quotationValue());
                discrepancy.setInvoiceValue(item.invoiceValue());
                discrepancy.setReasonForMismatch(item.reason());
                discrepancyRepository.save(discrepancy);
                savedDiscrepancies.add(discrepancy);
            }

            job.setStatus("COMPLETED");
            jobRepository.save(job);

            return ResponseEntity.ok(report);

        } catch (Exception e) {
            job.setStatus("FAILED");
            jobRepository.save(job);
            return ResponseEntity.internalServerError().body("Audit analysis failure sequence triggered: " + e.getMessage());
        }
    }
}