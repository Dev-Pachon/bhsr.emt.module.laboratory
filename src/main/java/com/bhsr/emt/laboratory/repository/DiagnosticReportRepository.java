package com.bhsr.emt.laboratory.repository;

import com.bhsr.emt.laboratory.domain.DiagnosticReport;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the DiagnosticReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiagnosticReportRepository extends ReactiveMongoRepository<DiagnosticReport, String> {}
