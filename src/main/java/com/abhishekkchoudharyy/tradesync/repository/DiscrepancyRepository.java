package com.abhishekkchoudharyy.tradesync.repository;

import com.abhishekkchoudharyy.tradesync.entity.Discrepancy;
import com.abhishekkchoudharyy.tradesync.entity.ReconciliationJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscrepancyRepository extends JpaRepository<Discrepancy,Long> {

    List<Discrepancy> findByJob(ReconciliationJob job);
}
