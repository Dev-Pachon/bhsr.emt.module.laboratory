package com.bhsr.emt.laboratory.web.rest;

import com.bhsr.emt.laboratory.domain.DiagnosticReportFormat;
import com.bhsr.emt.laboratory.domain.FieldFormat;
import com.bhsr.emt.laboratory.domain.User;
import com.bhsr.emt.laboratory.repository.DiagnosticReportFormatRepository;
import com.bhsr.emt.laboratory.service.UserService;
import com.bhsr.emt.laboratory.service.dto.AdminUserDTO;
import com.bhsr.emt.laboratory.service.dto.DiagnosticReportFormat.DiagnosticReportFormatRequestDTO;
import com.bhsr.emt.laboratory.service.dto.DiagnosticReportFormat.DiagnosticReportFormatResponseDTO;
import com.bhsr.emt.laboratory.service.dto.FieldFormat.FieldFormatResponseDTO;
import com.bhsr.emt.laboratory.service.mapper.DiagnosticReportFormatMapper;
import com.bhsr.emt.laboratory.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
 * REST controller for managing {@link com.bhsr.emt.laboratory.domain.DiagnosticReportFormat}.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DiagnosticReportFormatResource {

    private static final String ENTITY_NAME = "laboratoryDiagnosticReportFormat";
    private final Logger log = LoggerFactory.getLogger(DiagnosticReportFormatResource.class);
    private final UserService userService;
    private final DiagnosticReportFormatRepository diagnosticReportFormatRepository;
    private final DiagnosticReportFormatMapper diagnosticReportFormatMapper;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * {@code POST  /diagnostic-report-formats} : Create a new diagnosticReportFormat.
     *
     * @param diagnosticReportFormat the diagnosticReportFormat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new diagnosticReportFormat, or with status {@code 400 (Bad Request)} if the diagnosticReportFormat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/diagnostic-report-formats")
    public Mono<ResponseEntity<DiagnosticReportFormatResponseDTO>> createDiagnosticReportFormat(
        @Valid @RequestBody DiagnosticReportFormatRequestDTO diagnosticReportFormat,
        Principal principal
    ) throws URISyntaxException {
        log.debug("REST request to save DiagnosticReportFormat : {}", diagnosticReportFormat);

        if (principal instanceof AbstractAuthenticationToken) {
            return userService
                .getUserFromAuthentication((AbstractAuthenticationToken) principal)
                .flatMap(user -> {
                    DiagnosticReportFormat diagnosticReportFormatToSave = DiagnosticReportFormat
                        .builder()
                        .name(diagnosticReportFormat.getName())
                        .fieldFormats(
                            diagnosticReportFormat
                                .getFieldFormats()
                                .stream()
                                .map(fieldFormat ->
                                    FieldFormat
                                        .builder()
                                        .name(fieldFormat.getName())
                                        .dataType(fieldFormat.getDataType())
                                        .isRequired(fieldFormat.getIsRequired())
                                        .isSearchable(fieldFormat.getIsSearchable())
                                        .defaultValue(fieldFormat.getDefaultValue())
                                        .referenceValue(fieldFormat.getReferenceValue())
                                        .valueSet(fieldFormat.getValueSet())
                                        .order(fieldFormat.getOrder())
                                        .build()
                                )
                                .collect(Collectors.toSet())
                        )
                        .createdAt(LocalDate.now())
                        .createdBy(User.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).build())
                        .updatedAt(LocalDate.now())
                        .updatedBy(User.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).build())
                        .build();
                    log.debug("REST Formatted object to save : {}", diagnosticReportFormatToSave);
                    return diagnosticReportFormatRepository
                        .save(diagnosticReportFormatToSave)
                        .handle((result, sink) -> {
                            try {
                                DiagnosticReportFormatResponseDTO response = DiagnosticReportFormatResponseDTO
                                    .builder()
                                    .id(result.getId())
                                    .name(result.getName())
                                    .fieldFormats(
                                        result
                                            .getFieldFormats()
                                            .stream()
                                            .map(fieldFormat ->
                                                FieldFormatResponseDTO
                                                    .builder()
                                                    .name(fieldFormat.getName())
                                                    .dataType(fieldFormat.getDataType())
                                                    .isRequired(fieldFormat.getIsRequired())
                                                    .isSearchable(fieldFormat.getIsSearchable())
                                                    .defaultValue(fieldFormat.getDefaultValue())
                                                    .referenceValue(fieldFormat.getReferenceValue())
                                                    .valueSet(fieldFormat.getValueSet())
                                                    .order(fieldFormat.getOrder())
                                                    .build()
                                            )
                                            .collect(Collectors.toSet())
                                    )
                                    .build();
                                sink.next(
                                    ResponseEntity
                                        .created(new URI("/api/diagnostic-report-formats/" + response.getId()))
                                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                                        .body(response)
                                );
                            } catch (URISyntaxException e) {
                                sink.error(new RuntimeException(e));
                            }
                        });
                });
        } else {
            throw new BadRequestAlertException("Invalid authToken", ENTITY_NAME, "unauthenticated");
        }
    }

    /**
     * {@code PUT  /diagnostic-report-formats/:id} : Updates an existing diagnosticReportFormat.
     *
     * @param id                               the id of the diagnosticReportFormat to save.
     * @param diagnosticReportFormatRequestDTO the diagnosticReportFormat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diagnosticReportFormat,
     * or with status {@code 400 (Bad Request)} if the diagnosticReportFormat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the diagnosticReportFormat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/diagnostic-report-formats/{id}")
    public Mono<ResponseEntity<DiagnosticReportFormatResponseDTO>> updateDiagnosticReportFormat(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody DiagnosticReportFormatRequestDTO diagnosticReportFormatRequestDTO,
        Principal principal
    ) throws URISyntaxException {
        log.debug("REST request to update DiagnosticReportFormat : {}, {}", id, diagnosticReportFormatRequestDTO);
        if (diagnosticReportFormatRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diagnosticReportFormatRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (principal instanceof AbstractAuthenticationToken) {
            return userService
                .getUserFromAuthentication((AbstractAuthenticationToken) principal)
                .switchIfEmpty(Mono.just(AdminUserDTO.builder().id("0000001").build()))
                .flatMap(user ->
                    diagnosticReportFormatRepository
                        .existsById(id)
                        .flatMap(exists -> {
                            if (!exists) {
                                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                            }

                            DiagnosticReportFormat diagnosticReportFormat = DiagnosticReportFormat
                                .builder()
                                .id(diagnosticReportFormatRequestDTO.getId())
                                .name(diagnosticReportFormatRequestDTO.getName())
                                .fieldFormats(
                                    diagnosticReportFormatRequestDTO
                                        .getFieldFormats()
                                        .stream()
                                        .map(fieldFormat ->
                                            FieldFormat
                                                .builder()
                                                .name(fieldFormat.getName())
                                                .dataType(fieldFormat.getDataType())
                                                .isRequired(fieldFormat.getIsRequired())
                                                .isSearchable(fieldFormat.getIsSearchable())
                                                .defaultValue(fieldFormat.getDefaultValue())
                                                .referenceValue(fieldFormat.getReferenceValue())
                                                .valueSet(fieldFormat.getValueSet())
                                                .order(fieldFormat.getOrder())
                                                .build()
                                        )
                                        .collect(Collectors.toSet())
                                )
                                .updatedAt(LocalDate.now())
                                .updatedBy(
                                    User.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).build()
                                )
                                .createdAt(diagnosticReportFormatRequestDTO.getCreatedAt())
                                .createdBy(diagnosticReportFormatRequestDTO.getCreatedBy())
                                .build();

                            return diagnosticReportFormatRepository
                                .save(diagnosticReportFormat)
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                                .map(result -> {
                                    DiagnosticReportFormatResponseDTO response = DiagnosticReportFormatResponseDTO
                                        .builder()
                                        .id(result.getId())
                                        .name(result.getName())
                                        .fieldFormats(
                                            result
                                                .getFieldFormats()
                                                .stream()
                                                .map(fieldFormat ->
                                                    FieldFormatResponseDTO
                                                        .builder()
                                                        .name(fieldFormat.getName())
                                                        .dataType(fieldFormat.getDataType())
                                                        .isRequired(fieldFormat.getIsRequired())
                                                        .isSearchable(fieldFormat.getIsSearchable())
                                                        .defaultValue(fieldFormat.getDefaultValue())
                                                        .referenceValue(fieldFormat.getReferenceValue())
                                                        .valueSet(fieldFormat.getValueSet())
                                                        .order(fieldFormat.getOrder())
                                                        .build()
                                                )
                                                .collect(Collectors.toSet())
                                        )
                                        .createdAt(result.getCreatedAt())
                                        .updatedAt(result.getUpdatedAt())
                                        .createdBy(result.getCreatedBy())
                                        .updatedBy(result.getUpdatedBy())
                                        .build();

                                    return ResponseEntity
                                        .ok()
                                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, response.getId()))
                                        .body(response);
                                });
                        })
                );
        } else {
            throw new BadRequestAlertException("Invalid authToken", ENTITY_NAME, "unauthenticated");
        }
    }

    /**
     * {@code PATCH  /diagnostic-report-formats/:id} : Partial updates given fields of an existing diagnosticReportFormat, field will ignore if it is null
     *
     * @param id                     the id of the diagnosticReportFormat to save.
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
        @NotNull @RequestBody DiagnosticReportFormat diagnosticReportFormat,
        Principal principal
    ) throws URISyntaxException {
        log.debug("REST request to partial update DiagnosticReportFormat partially : {}, {}", id, diagnosticReportFormat);
        if (diagnosticReportFormat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diagnosticReportFormat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (principal instanceof AbstractAuthenticationToken) {
            return userService
                .getUserFromAuthentication((AbstractAuthenticationToken) principal)
                .flatMap(user ->
                    diagnosticReportFormatRepository
                        .existsById(id)
                        .flatMap(exists -> {
                            if (!exists) {
                                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                            }

                            Mono<DiagnosticReportFormat> result = diagnosticReportFormatRepository
                                .findById(diagnosticReportFormat.getId())
                                .map(existingDiagnosticReportFormat -> {
                                    diagnosticReportFormat.setUpdatedAt(LocalDate.now());
                                    diagnosticReportFormat.setUpdatedBy(
                                        User.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).build()
                                    );
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
                        })
                );
        } else {
            throw new BadRequestAlertException("Invalid authToken", ENTITY_NAME, "unauthenticated");
        }
    }

    /**
     * {@code GET  /diagnostic-report-formats} : get all the diagnosticReportFormats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of diagnosticReportFormats in body.
     */
    @GetMapping("/diagnostic-report-formats")
    public Mono<List<DiagnosticReportFormatResponseDTO>> getAllDiagnosticReportFormats() {
        log.debug("REST request to get all DiagnosticReportFormats");
        return diagnosticReportFormatRepository
            .findAll()
            .map(diagnosticReportFormatMapper::DiagnosticReportFormatToResponseDTO)
            .collectList();
    }

    /**
     * {@code GET  /diagnostic-report-formats} : get all the diagnosticReportFormats as a stream.
     *
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
