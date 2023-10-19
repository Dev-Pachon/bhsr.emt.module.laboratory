package com.bhsr.emt.laboratory.web.rest;

import com.bhsr.emt.laboratory.domain.IdentifierType;
import com.bhsr.emt.laboratory.repository.IdentifierTypeRepository;
import com.bhsr.emt.laboratory.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.bhsr.emt.laboratory.domain.IdentifierType}.
 */
@RestController
@RequestMapping("/api")
public class IdentifierTypeResource {

    private final Logger log = LoggerFactory.getLogger(IdentifierTypeResource.class);

    private static final String ENTITY_NAME = "laboratoryIdentifierType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdentifierTypeRepository identifierTypeRepository;

    public IdentifierTypeResource(IdentifierTypeRepository identifierTypeRepository) {
        this.identifierTypeRepository = identifierTypeRepository;
    }

    /**
     * {@code POST  /identifier-types} : Create a new identifierType.
     *
     * @param identifierType the identifierType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new identifierType, or with status {@code 400 (Bad Request)} if the identifierType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/identifier-types")
    public Mono<ResponseEntity<IdentifierType>> createIdentifierType(@Valid @RequestBody IdentifierType identifierType)
        throws URISyntaxException {
        log.debug("REST request to save IdentifierType : {}", identifierType);
        if (identifierType.getId() != null) {
            throw new BadRequestAlertException("A new identifierType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        identifierType.setId(UUID.randomUUID());
        return identifierTypeRepository
            .save(identifierType)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/identifier-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /identifier-types/:id} : Updates an existing identifierType.
     *
     * @param id the id of the identifierType to save.
     * @param identifierType the identifierType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated identifierType,
     * or with status {@code 400 (Bad Request)} if the identifierType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the identifierType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/identifier-types/{id}")
    public Mono<ResponseEntity<IdentifierType>> updateIdentifierType(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody IdentifierType identifierType
    ) throws URISyntaxException {
        log.debug("REST request to update IdentifierType : {}, {}", id, identifierType);
        if (identifierType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identifierType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return identifierTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return identifierTypeRepository
                    .save(identifierType)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /identifier-types/:id} : Partial updates given fields of an existing identifierType, field will ignore if it is null
     *
     * @param id the id of the identifierType to save.
     * @param identifierType the identifierType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated identifierType,
     * or with status {@code 400 (Bad Request)} if the identifierType is not valid,
     * or with status {@code 404 (Not Found)} if the identifierType is not found,
     * or with status {@code 500 (Internal Server Error)} if the identifierType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/identifier-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IdentifierType>> partialUpdateIdentifierType(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody IdentifierType identifierType
    ) throws URISyntaxException {
        log.debug("REST request to partial update IdentifierType partially : {}, {}", id, identifierType);
        if (identifierType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identifierType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return identifierTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IdentifierType> result = identifierTypeRepository
                    .findById(identifierType.getId())
                    .map(existingIdentifierType -> {
                        if (identifierType.getName() != null) {
                            existingIdentifierType.setName(identifierType.getName());
                        }

                        return existingIdentifierType;
                    })
                    .flatMap(identifierTypeRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /identifier-types} : get all the identifierTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of identifierTypes in body.
     */
    @GetMapping("/identifier-types")
    public Mono<List<IdentifierType>> getAllIdentifierTypes() {
        log.debug("REST request to get all IdentifierTypes");
        return identifierTypeRepository.findAll().collectList();
    }

    /**
     * {@code GET  /identifier-types} : get all the identifierTypes as a stream.
     * @return the {@link Flux} of identifierTypes.
     */
    @GetMapping(value = "/identifier-types", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<IdentifierType> getAllIdentifierTypesAsStream() {
        log.debug("REST request to get all IdentifierTypes as a stream");
        return identifierTypeRepository.findAll();
    }

    /**
     * {@code GET  /identifier-types/:id} : get the "id" identifierType.
     *
     * @param id the id of the identifierType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the identifierType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/identifier-types/{id}")
    public Mono<ResponseEntity<IdentifierType>> getIdentifierType(@PathVariable UUID id) {
        log.debug("REST request to get IdentifierType : {}", id);
        Mono<IdentifierType> identifierType = identifierTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(identifierType);
    }

    /**
     * {@code DELETE  /identifier-types/:id} : delete the "id" identifierType.
     *
     * @param id the id of the identifierType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/identifier-types/{id}")
    public Mono<ResponseEntity<Void>> deleteIdentifierType(@PathVariable UUID id) {
        log.debug("REST request to delete IdentifierType : {}", id);
        return identifierTypeRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
