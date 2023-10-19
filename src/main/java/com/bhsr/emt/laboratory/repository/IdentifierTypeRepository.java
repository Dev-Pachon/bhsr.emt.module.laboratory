package com.bhsr.emt.laboratory.repository;

import com.bhsr.emt.laboratory.domain.IdentifierType;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the IdentifierType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdentifierTypeRepository extends ReactiveMongoRepository<IdentifierType, UUID> {}
