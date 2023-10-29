package com.bhsr.emt.laboratory.web.rest;

import com.bhsr.emt.laboratory.domain.DiagnosticReport;
import com.bhsr.emt.laboratory.repository.DiagnosticReportRepository;
import com.bhsr.emt.laboratory.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
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
 * REST controller for managing {@link com.bhsr.emt.laboratory.domain.DiagnosticReport}.
 */
@RestController
@RequestMapping("/api")
public class DiagnosticReportResource {

    private final Logger log = LoggerFactory.getLogger(DiagnosticReportResource.class);

    private static final String ENTITY_NAME = "laboratoryDiagnosticReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DiagnosticReportRepository diagnosticReportRepository;

    public DiagnosticReportResource(DiagnosticReportRepository diagnosticReportRepository) {
        this.diagnosticReportRepository = diagnosticReportRepository;
    }

    /**
     * {@code POST  /diagnostic-reports} : Create a new diagnosticReport.
     *
     * @param diagnosticReport the diagnosticReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new diagnosticReport, or with status {@code 400 (Bad Request)} if the diagnosticReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/diagnostic-reports")
    public Mono<ResponseEntity<DiagnosticReport>> createDiagnosticReport(@Valid @RequestBody DiagnosticReport diagnosticReport)
        throws URISyntaxException {
        log.debug("REST request to save DiagnosticReport : {}", diagnosticReport);
        if (diagnosticReport.getId() != null) {
            throw new BadRequestAlertException("A new diagnosticReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return diagnosticReportRepository
            .save(diagnosticReport)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/diagnostic-reports/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /diagnostic-reports/:id} : Updates an existing diagnosticReport.
     *
     * @param id the id of the diagnosticReport to save.
     * @param diagnosticReport the diagnosticReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diagnosticReport,
     * or with status {@code 400 (Bad Request)} if the diagnosticReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the diagnosticReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/diagnostic-reports/{id}")
    public Mono<ResponseEntity<DiagnosticReport>> updateDiagnosticReport(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody DiagnosticReport diagnosticReport
    ) throws URISyntaxException {
        log.debug("REST request to update DiagnosticReport : {}, {}", id, diagnosticReport);
        if (diagnosticReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diagnosticReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return diagnosticReportRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return diagnosticReportRepository
                    .save(diagnosticReport)
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
     * {@code PATCH  /diagnostic-reports/:id} : Partial updates given fields of an existing diagnosticReport, field will ignore if it is null
     *
     * @param id the id of the diagnosticReport to save.
     * @param diagnosticReport the diagnosticReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diagnosticReport,
     * or with status {@code 400 (Bad Request)} if the diagnosticReport is not valid,
     * or with status {@code 404 (Not Found)} if the diagnosticReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the diagnosticReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/diagnostic-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DiagnosticReport>> partialUpdateDiagnosticReport(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody DiagnosticReport diagnosticReport
    ) throws URISyntaxException {
        log.debug("REST request to partial update DiagnosticReport partially : {}, {}", id, diagnosticReport);
        if (diagnosticReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diagnosticReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return diagnosticReportRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DiagnosticReport> result = diagnosticReportRepository
                    .findById(diagnosticReport.getId())
                    .map(existingDiagnosticReport -> {
                        if (diagnosticReport.getStatus() != null) {
                            existingDiagnosticReport.setStatus(diagnosticReport.getStatus());
                        }
                        if (diagnosticReport.getCreatedAt() != null) {
                            existingDiagnosticReport.setCreatedAt(diagnosticReport.getCreatedAt());
                        }
                        if (diagnosticReport.getCreatedBy() != null) {
                            existingDiagnosticReport.setCreatedBy(diagnosticReport.getCreatedBy());
                        }
                        if (diagnosticReport.getUpdatedAt() != null) {
                            existingDiagnosticReport.setUpdatedAt(diagnosticReport.getUpdatedAt());
                        }
                        if (diagnosticReport.getUpdatedBy() != null) {
                            existingDiagnosticReport.setUpdatedBy(diagnosticReport.getUpdatedBy());
                        }
                        if (diagnosticReport.getDeletedAt() != null) {
                            existingDiagnosticReport.setDeletedAt(diagnosticReport.getDeletedAt());
                        }

                        return existingDiagnosticReport;
                    })
                    .flatMap(diagnosticReportRepository::save);

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
     * {@code GET  /diagnostic-reports} : get all the diagnosticReports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of diagnosticReports in body.
     */
    @GetMapping("/diagnostic-reports")
    public Mono<List<DiagnosticReport>> getAllDiagnosticReports() {
        log.debug("REST request to get all DiagnosticReports");
        return diagnosticReportRepository.findAll().collectList();
    }

    /**
     * {@code GET  /diagnostic-reports} : get all the diagnosticReports as a stream.
     * @return the {@link Flux} of diagnosticReports.
     */
    @GetMapping(value = "/diagnostic-reports", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<DiagnosticReport> getAllDiagnosticReportsAsStream() {
        log.debug("REST request to get all DiagnosticReports as a stream");
        return diagnosticReportRepository.findAll();
    }

    /**
     * {@code GET  /diagnostic-reports/:id} : get the "id" diagnosticReport.
     *
     * @param id the id of the diagnosticReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the diagnosticReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/diagnostic-reports/{id}")
    public Mono<ResponseEntity<DiagnosticReport>> getDiagnosticReport(@PathVariable String id) {
        log.debug("REST request to get DiagnosticReport : {}", id);
        Mono<DiagnosticReport> diagnosticReport = diagnosticReportRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(diagnosticReport);
    }

    /**
     * {@code DELETE  /diagnostic-reports/:id} : delete the "id" diagnosticReport.
     *
     * @param id the id of the diagnosticReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/diagnostic-reports/{id}")
    public Mono<ResponseEntity<Void>> deleteDiagnosticReport(@PathVariable String id) {
        log.debug("REST request to delete DiagnosticReport : {}", id);
        return diagnosticReportRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
