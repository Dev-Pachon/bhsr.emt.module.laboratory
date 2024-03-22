package com.bhsr.emt.laboratory.repository;

import com.bhsr.emt.laboratory.domain.DiagnosticReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticReportRepositoryAlternative extends MongoRepository<DiagnosticReport, String> {}
