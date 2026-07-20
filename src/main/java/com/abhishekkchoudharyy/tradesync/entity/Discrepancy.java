package com.abhishekkchoudharyy.tradesync.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "descrepancies")
@Data
@NoArgsConstructor
public class Discrepancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id",nullable = false)
    private ReconciliationJob job;

    @Column(nullable = false,name = "mismatched_field")
    private String fieldName;

    @Column(name = "quotation_value")
    private String quotationValue;

    @Column(name = "invoice_value")
    private String invoiceValue;

    @Column(columnDefinition = "TEXT",name = "audit_reasoning")
    private String reasonForMismatch;

}
