package com.bhsr.emt.laboratory.web.rest;

import com.bhsr.emt.laboratory.domain.ValueSet;
import com.bhsr.emt.laboratory.repository.ValueSetRepository;
import com.bhsr.emt.laboratory.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.bhsr.emt.laboratory.domain.ValueSet}.
 */
@RestController
@RequestMapping("/api")
public class ValueSetResource {

    private final Logger log = LoggerFactory.getLogger(ValueSetResource.class);

    private static final String ENTITY_NAME = "laboratoryValueSet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValueSetRepository valueSetRepository;

    public ValueSetResource(ValueSetRepository valueSetRepository) {
        this.valueSetRepository = valueSetRepository;
    }

    /**
     * {@code POST  /value-sets} : Create a new valueSet.
     *
     * @param valueSet the valueSet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new valueSet, or with status {@code 400 (Bad Request)} if the valueSet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/value-sets")
    public Mono<ResponseEntity<ValueSet>> createValueSet(@Valid @RequestBody ValueSet valueSet) throws URISyntaxException {
        log.debug("REST request to save ValueSet : {}", valueSet);
        if (valueSet.getId() != null) {
            throw new BadRequestAlertException("A new valueSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return valueSetRepository
            .save(valueSet)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/value-sets/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /value-sets/:id} : Updates an existing valueSet.
     *
     * @param id the id of the valueSet to save.
     * @param valueSet the valueSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated valueSet,
     * or with status {@code 400 (Bad Request)} if the valueSet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the valueSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/value-sets/{id}")
    public Mono<ResponseEntity<ValueSet>> updateValueSet(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ValueSet valueSet
    ) throws URISyntaxException {
        log.debug("REST request to update ValueSet : {}, {}", id, valueSet);
        if (valueSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, valueSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return valueSetRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return valueSetRepository
                    .save(valueSet)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /value-sets/:id} : Partial updates given fields of an existing valueSet, field will ignore if it is null
     *
     * @param id the id of the valueSet to save.
     * @param valueSet the valueSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated valueSet,
     * or with status {@code 400 (Bad Request)} if the valueSet is not valid,
     * or with status {@code 404 (Not Found)} if the valueSet is not found,
     * or with status {@code 500 (Internal Server Error)} if the valueSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/value-sets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ValueSet>> partialUpdateValueSet(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ValueSet valueSet
    ) throws URISyntaxException {
        log.debug("REST request to partial update ValueSet partially : {}, {}", id, valueSet);
        if (valueSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, valueSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return valueSetRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ValueSet> result = valueSetRepository
                    .findById(valueSet.getId())
                    .map(existingValueSet -> {
                        if (valueSet.getName() != null) {
                            existingValueSet.setName(valueSet.getName());
                        }

                        return existingValueSet;
                    })
                    .flatMap(valueSetRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /value-sets} : get all the valueSets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of valueSets in body.
     */
    @GetMapping("/value-sets")
    public Mono<List<ValueSet>> getAllValueSets() {
        log.debug("REST request to get all ValueSets");
        return valueSetRepository.findAll().collectList();
    }

    /**
     * {@code GET  /value-sets} : get all the valueSets as a stream.
     * @return the {@link Flux} of valueSets.
     */
    @GetMapping(value = "/value-sets", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ValueSet> getAllValueSetsAsStream() {
        log.debug("REST request to get all ValueSets as a stream");
        return valueSetRepository.findAll();
    }

    /**
     * {@code GET  /value-sets/:id} : get the "id" valueSet.
     *
     * @param id the id of the valueSet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the valueSet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/value-sets/{id}")
    public Mono<ResponseEntity<ValueSet>> getValueSet(@PathVariable String id) {
        log.debug("REST request to get ValueSet : {}", id);
        Mono<ValueSet> valueSet = valueSetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(valueSet);
    }

    /**
     * {@code DELETE  /value-sets/:id} : delete the "id" valueSet.
     *
     * @param id the id of the valueSet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/value-sets/{id}")
    public Mono<ResponseEntity<Void>> deleteValueSet(@PathVariable String id) {
        log.debug("REST request to delete ValueSet : {}", id);
        return valueSetRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
