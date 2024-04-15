package com.bhsr.emt.laboratory.web.rest;

import com.bhsr.emt.laboratory.domain.DiagnosticReport;
import com.bhsr.emt.laboratory.domain.User;
import com.bhsr.emt.laboratory.domain.enumeration.DiagnosticReportStatus;
import com.bhsr.emt.laboratory.repository.DiagnosticReportRepository;
import com.bhsr.emt.laboratory.repository.ServiceRequestRepository;
import com.bhsr.emt.laboratory.service.UserService;
import com.bhsr.emt.laboratory.service.dto.DiagnosticReport.DiagnosticReportResponseDTO;
import com.bhsr.emt.laboratory.service.mapper.DiagnosticReportMapper;
import com.bhsr.emt.laboratory.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
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
@RequiredArgsConstructor
public class DiagnosticReportResource {

    private static final String ENTITY_NAME = "laboratoryDiagnosticReport";
    private final Logger log = LoggerFactory.getLogger(DiagnosticReportResource.class);
    private final DiagnosticReportRepository diagnosticReportRepository;
    private final DiagnosticReportMapper diagnosticReportMapper;
    private final PatientResource patientResource;
    private final UserService userService;
    private final ServiceRequestRepository serviceRequestRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

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
     * @param id               the id of the diagnosticReport to save.
     * @param diagnosticReport the diagnosticReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diagnosticReport,
     * or with status {@code 400 (Bad Request)} if the diagnosticReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the diagnosticReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/diagnostic-reports/{id}")
    public Mono<ResponseEntity<DiagnosticReportResponseDTO>> updateDiagnosticReport(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody DiagnosticReportResponseDTO diagnosticReport,
        Principal principal
    ) throws URISyntaxException {
        log.debug("REST request to update DiagnosticReport : {}, {}", id, diagnosticReport);
        if (diagnosticReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diagnosticReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (principal instanceof AbstractAuthenticationToken) {
            return userService
                .getUserFromAuthentication((AbstractAuthenticationToken) principal)
                .flatMap(user ->
                    diagnosticReportRepository
                        .existsById(id)
                        .flatMap(exists -> {
                            if (!exists) {
                                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                            }

                            if (diagnosticReport.getStatus() == DiagnosticReportStatus.REGISTERED) {
                                diagnosticReport.setStatus(DiagnosticReportStatus.FINAL);
                            }

                            diagnosticReport.setUpdatedAt(LocalDate.now());
                            diagnosticReport.setUpdatedBy(
                                User.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).build()
                            );

                            return diagnosticReportRepository
                                .save(diagnosticReportMapper.DiagnosticReportResponseDTOToDiagnosticReport(diagnosticReport))
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                                .map(result ->
                                    ResponseEntity
                                        .ok()
                                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                                        .body(diagnosticReportMapper.DiagnosticReporToResponseDTO(result))
                                );
                        })
                );
        } else {
            throw new BadRequestAlertException("Invalid authToken", ENTITY_NAME, "unauthenticated");
        }
    }

    /**
     * {@code PATCH  /diagnostic-reports/:id} : Partial updates given fields of an existing diagnosticReport, field will ignore if it is null
     *
     * @param id               the id of the diagnosticReport to save.
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
        @NotNull @RequestBody DiagnosticReport diagnosticReport,
        Principal principal
    ) throws URISyntaxException {
        log.debug("REST request to partial update DiagnosticReport partially : {}, {}", id, diagnosticReport);
        if (diagnosticReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diagnosticReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (principal instanceof AbstractAuthenticationToken) {
            return userService
                .getUserFromAuthentication((AbstractAuthenticationToken) principal)
                .flatMap(user ->
                    diagnosticReportRepository
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
                                    diagnosticReport.setUpdatedAt(LocalDate.now());
                                    diagnosticReport.setUpdatedBy(
                                        User.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).build()
                                    );

                                    if (diagnosticReport.getDeletedAt() != null) {
                                        existingDiagnosticReport.setDeletedAt(diagnosticReport.getDeletedAt());
                                    }

                                    // UPDATE SERVICE REQUEST STATUS
                                    //                                             diagnosticReportRepository.findAll(
                                    //                                                                           Example.of(DiagnosticReport.builder().basedOn(
                                    //                                                                               ServiceRequest.builder().id(
                                    //                                                                                   diagnosticReport.getBasedOn().getId()).build()).build()))
                                    //                                                                       .filter(
                                    //                                                                           relatedDiagnostic -> relatedDiagnostic.getStatus() != DiagnosticReportStatus.PARTIAL || relatedDiagnostic.getStatus() != DiagnosticReportStatus.REGISTERED || relatedDiagnostic.getStatus() != DiagnosticReportStatus.PRELIMINARY
                                    //                                                                       ).count().doOnNext(
                                    //                                                                           count -> {
                                    //                                                                               if (count == 0) {
                                    //                                                                                   serviceRequestRepository.findById(
                                    //                                                                                                               diagnosticReport.getBasedOn().getId())
                                    //                                                                                                           .map(
                                    //                                                                                                               serviceRequest -> {
                                    //                                                                                                                   serviceRequest.setStatus(
                                    //                                                                                                                       ServiceRequestStatus.COMPLETED
                                    //                                                                                                                   );
                                    //                                                                                                                   return serviceRequest;
                                    //                                                                                                               }
                                    //                                                                                                           ).flatMap(
                                    //                                                                                                               serviceRequestRepository::save
                                    //                                                                                                           );
                                    //                                                                               }
                                    //                                                                           }
                                    //                                                                       );

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
                        })
                );
        } else {
            throw new BadRequestAlertException("Invalid authToken", ENTITY_NAME, "unauthenticated");
        }
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
     *
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
    public Mono<ResponseEntity<DiagnosticReportResponseDTO>> getDiagnosticReport(@PathVariable String id) {
        log.debug("REST request to get DiagnosticReport : {}", id);
        Mono<DiagnosticReportResponseDTO> diagnosticReport = diagnosticReportRepository
            .findById(id)
            .flatMap(element ->
                patientResource
                    .getPatientById(element.getSubject())
                    .flatMap(patient -> {
                        DiagnosticReportResponseDTO diagnosticReportResponse = diagnosticReportMapper.DiagnosticReporToResponseDTO(element);
                        diagnosticReportResponse.setSubject(patient);
                        return Mono.just(diagnosticReportResponse);
                    })
            );

        return ResponseUtil.wrapOrNotFound(diagnosticReport);
    }
}
