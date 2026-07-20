package com.abhishekkchoudharyy.tradesync.repository;

import com.abhishekkchoudharyy.tradesync.entity.ReconciliationJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReconciliationJobRepository extends JpaRepository<ReconciliationJob, Long> {
}