package com.bhsr.emt.laboratory.repository;

import com.bhsr.emt.laboratory.domain.DiagnosticReportFormat;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the DiagnosticReportFormat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiagnosticReportFormatRepository extends ReactiveMongoRepository<DiagnosticReportFormat, String> {}
