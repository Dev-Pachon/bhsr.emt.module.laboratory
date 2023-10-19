package com.bhsr.emt.laboratory.repository;

import com.bhsr.emt.laboratory.domain.Patient;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Patient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientRepository extends ReactiveMongoRepository<Patient, String> {}
