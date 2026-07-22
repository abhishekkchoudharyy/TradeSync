package com.abhishekkchoudharyy.tradesync.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id",nullable = false)
    private User uploader;

    @Column(nullable = false,length = 50)
    private String documentType;

    @Column(nullable = false,length = 512)
    private String filePath;

    @Column(nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();
}
