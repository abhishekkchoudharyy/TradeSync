package com.abhishekkchoudharyy.tradesync.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reconciliations_jobs")
@Data
@NoArgsConstructor
public class ReconciliationJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_document_id",nullable = false)
    private Document quotationDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_document_id",nullable = false)
    private Document invoiceDocument;

    @Column(nullable = false,length = 30)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();



}
