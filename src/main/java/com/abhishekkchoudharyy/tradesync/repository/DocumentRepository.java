package com.abhishekkchoudharyy.tradesync.repository;

import com.abhishekkchoudharyy.tradesync.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
}
