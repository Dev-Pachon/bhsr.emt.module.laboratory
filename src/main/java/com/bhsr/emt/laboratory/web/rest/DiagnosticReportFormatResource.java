package com.bhsr.emt.laboratory.web.rest;

import com.bhsr.emt.laboratory.domain.DiagnosticReportFormat;
import com.bhsr.emt.laboratory.repository.DiagnosticReportFormatRepository;
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
 * REST controller for managing {@link com.bhsr.emt.laboratory.domain.DiagnosticReportFormat}.
 */
@RestController
@RequestMapping("/api")
public class DiagnosticReportFormatResource {

    private final Logger log = LoggerFactory.getLogger(DiagnosticReportFormatResource.class);

    private static final String ENTITY_NAME = "laboratoryDiagnosticReportFormat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DiagnosticReportFormatRepository diagnosticReportFormatRepository;

    public DiagnosticReportFormatResource(DiagnosticReportFormatRepository diagnosticReportFormatRepository) {
        this.diagnosticReportFormatRepository = diagnosticReportFormatRepository;
    }

    /**
     * {@code POST  /diagnostic-report-formats} : Create a new diagnosticReportFormat.
     *
     * @param diagnosticReportFormat the diagnosticReportFormat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new diagnosticReportFormat, or with status {@code 400 (Bad Request)} if the diagnosticReportFormat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/diagnostic-report-formats")
    public Mono<ResponseEntity<DiagnosticReportFormat>> createDiagnosticReportFormat(
        @Valid @RequestBody DiagnosticReportFormat diagnosticReportFormat
    ) throws URISyntaxException {
        log.debug("REST request to save DiagnosticReportFormat : {}", diagnosticReportFormat);
        if (diagnosticReportFormat.getId() != null) {
            throw new BadRequestAlertException("A new diagnosticReportFormat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return diagnosticReportFormatRepository
            .save(diagnosticReportFormat)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/diagnostic-report-formats/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /diagnostic-report-formats/:id} : Updates an existing diagnosticReportFormat.
     *
     * @param id the id of the diagnosticReportFormat to save.
     * @param diagnosticReportFormat the diagnosticReportFormat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diagnosticReportFormat,
     * or with status {@code 400 (Bad Request)} if the diagnosticReportFormat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the diagnosticReportFormat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/diagnostic-report-formats/{id}")
    public Mono<ResponseEntity<DiagnosticReportFormat>> updateDiagnosticReportFormat(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody DiagnosticReportFormat diagnosticReportFormat
    ) throws URISyntaxException {
        log.debug("REST request to update DiagnosticReportFormat : {}, {}", id, diagnosticReportFormat);
        if (diagnosticReportFormat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diagnosticReportFormat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return diagnosticReportFormatRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return diagnosticReportFormatRepository
                    .save(diagnosticReportFormat.setIsPersisted())
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
     * {@code PATCH  /diagnostic-report-formats/:id} : Partial updates given fields of an existing diagnosticReportFormat, field will ignore if it is null
     *
     * @param id the id of the diagnosticReportFormat to save.
     * @param diagnosticReportFormat the diagnosticReportFormat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diagnosticReportFormat,
     * or with status {@code 400 (Bad Request)} if the diagnosticReportFormat is not valid,
     * or with status {@code 404 (Not Found)} if the diagnosticReportFormat is not found,
     * or with status {@code 500 (Internal Server Error)} if the diagnosticReportFormat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/diagnostic-report-formats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DiagnosticReportFormat>> partialUpdateDiagnosticReportFormat(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody DiagnosticReportFormat diagnosticReportFormat
    ) throws URISyntaxException {
        log.debug("REST request to partial update DiagnosticReportFormat partially : {}, {}", id, diagnosticReportFormat);
        if (diagnosticReportFormat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diagnosticReportFormat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return diagnosticReportFormatRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DiagnosticReportFormat> result = diagnosticReportFormatRepository
                    .findById(diagnosticReportFormat.getId())
                    .map(existingDiagnosticReportFormat -> {
                        if (diagnosticReportFormat.getCreatedAt() != null) {
                            existingDiagnosticReportFormat.setCreatedAt(diagnosticReportFormat.getCreatedAt());
                        }
                        if (diagnosticReportFormat.getCreatedBy() != null) {
                            existingDiagnosticReportFormat.setCreatedBy(diagnosticReportFormat.getCreatedBy());
                        }
                        if (diagnosticReportFormat.getUpdatedAt() != null) {
                            existingDiagnosticReportFormat.setUpdatedAt(diagnosticReportFormat.getUpdatedAt());
                        }
                        if (diagnosticReportFormat.getUpdatedBy() != null) {
                            existingDiagnosticReportFormat.setUpdatedBy(diagnosticReportFormat.getUpdatedBy());
                        }
                        if (diagnosticReportFormat.getDeletedAt() != null) {
                            existingDiagnosticReportFormat.setDeletedAt(diagnosticReportFormat.getDeletedAt());
                        }

                        return existingDiagnosticReportFormat;
                    })
                    .flatMap(diagnosticReportFormatRepository::save);

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
     * {@code GET  /diagnostic-report-formats} : get all the diagnosticReportFormats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of diagnosticReportFormats in body.
     */
    @GetMapping("/diagnostic-report-formats")
    public Mono<List<DiagnosticReportFormat>> getAllDiagnosticReportFormats() {
        log.debug("REST request to get all DiagnosticReportFormats");
        return diagnosticReportFormatRepository.findAll().collectList();
    }

    /**
     * {@code GET  /diagnostic-report-formats} : get all the diagnosticReportFormats as a stream.
     * @return the {@link Flux} of diagnosticReportFormats.
     */
    @GetMapping(value = "/diagnostic-report-formats", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<DiagnosticReportFormat> getAllDiagnosticReportFormatsAsStream() {
        log.debug("REST request to get all DiagnosticReportFormats as a stream");
        return diagnosticReportFormatRepository.findAll();
    }

    /**
     * {@code GET  /diagnostic-report-formats/:id} : get the "id" diagnosticReportFormat.
     *
     * @param id the id of the diagnosticReportFormat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the diagnosticReportFormat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/diagnostic-report-formats/{id}")
    public Mono<ResponseEntity<DiagnosticReportFormat>> getDiagnosticReportFormat(@PathVariable String id) {
        log.debug("REST request to get DiagnosticReportFormat : {}", id);
        Mono<DiagnosticReportFormat> diagnosticReportFormat = diagnosticReportFormatRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(diagnosticReportFormat);
    }

    /**
     * {@code DELETE  /diagnostic-report-formats/:id} : delete the "id" diagnosticReportFormat.
     *
     * @param id the id of the diagnosticReportFormat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/diagnostic-report-formats/{id}")
    public Mono<ResponseEntity<Void>> deleteDiagnosticReportFormat(@PathVariable String id) {
        log.debug("REST request to delete DiagnosticReportFormat : {}", id);
        return diagnosticReportFormatRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
