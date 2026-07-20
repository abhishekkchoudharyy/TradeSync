package com.abhishekkchoudharyy.tradesync.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AIReconciliationService {

    private final ChatClient chatClient;

    public AIReconciliationService(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder.build();
    }

    public record DiscrepancyReport(
            List<MismatchItem> mismatches
    ) {}

    public record MismatchItem(
            String fieldName,
            String quotationValue,
            String invoiceValue,
            String reason
    ){}
    public DiscrepancyReport compareDocuments(String quotationText, String invoiceText) {
        String systemPrompt = """
            You are a strict, highly accurate B2B financial auditor.
            Compare the provided Quotation (the baseline agreement) against the Final Invoice.
            Identify any discrepancies in pricing, quantities, or missing items.
            Do not guess. Base your findings STRICTLY on the text provided.
            """;

        String userPrompt = String.format("""
            --- QUOTATION TEXT ---
            %s
            
            --- FINAL INVOICE TEXT ---
            %s
            """, quotationText, invoiceText);

        // Call DeepSeek, forcing it to return the exact DiscrepancyReport structure
        return this.chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .entity(DiscrepancyReport.class);
    }
}
