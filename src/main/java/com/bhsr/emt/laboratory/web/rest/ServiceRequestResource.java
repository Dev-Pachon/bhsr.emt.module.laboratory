package com.bhsr.emt.laboratory.web.rest;

import com.bhsr.emt.laboratory.domain.Patient;
import com.bhsr.emt.laboratory.domain.ServiceRequest;
import com.bhsr.emt.laboratory.domain.User;
import com.bhsr.emt.laboratory.repository.ServiceRequestRepository;
import com.bhsr.emt.laboratory.service.UserService;
import com.bhsr.emt.laboratory.service.dto.AdminUserDTO;
import com.bhsr.emt.laboratory.service.dto.ServiceRequest.ServiceRequestRequestDTO;
import com.bhsr.emt.laboratory.service.dto.ServiceRequest.ServiceRequestResponseDTO;
import com.bhsr.emt.laboratory.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
 * REST controller for managing {@link com.bhsr.emt.laboratory.domain.ServiceRequest}.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceRequestResource {

    private static final Integer TESTING_SERVICE_ID = 1;
    private final Logger log = LoggerFactory.getLogger(ServiceRequestResource.class);

    private static final String ENTITY_NAME = "laboratoryServiceRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final ServiceRequestRepository serviceRequestRepository;

    /**
     * {@code POST  /service-requests} : Create a new serviceRequest.
     *
     * @param serviceRequest the serviceRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceRequest, or with status {@code 400 (Bad Request)} if the serviceRequest has already an ID.
     */
    @PostMapping("/service-requests")
    public Mono<ResponseEntity<ServiceRequestResponseDTO>> createServiceRequest(
        @Valid @RequestBody ServiceRequestRequestDTO serviceRequest,
        Principal principal
    ) {
        log.debug("REST request to save ServiceRequest : {}", serviceRequest);

        //Call the microservice to get the subject/patient and verify it exists

        if (principal instanceof AbstractAuthenticationToken) {
            return userService
                .getUserFromAuthentication((AbstractAuthenticationToken) principal)
                .switchIfEmpty(Mono.just(AdminUserDTO.builder().id("0000001").build()))
                .flatMap(user -> {
                    ServiceRequest serviceRequestToSave = ServiceRequest
                        .builder()
                        .status(serviceRequest.getStatus())
                        .category(serviceRequest.getCategory())
                        .priority(serviceRequest.getPriority())
                        .diagnosticReportsIds(serviceRequest.getDiagnosticReportsIds())
                        .doNotPerform(false)
                        .patientId(serviceRequest.getPatientId())
                        .serviceId(TESTING_SERVICE_ID)
                        .createdAt(LocalDate.now())
                        .createdBy(User.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).build())
                        .updatedAt(LocalDate.now())
                        .updatedBy(User.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).build())
                        .build();
                    return serviceRequestRepository
                        .save(serviceRequestToSave)
                        .handle((result, sink) -> {
                            try {
                                sink.next(
                                    ResponseEntity
                                        .created(new URI("/api/service-requests/" + result.getId()))
                                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                                        .body(
                                            ServiceRequestResponseDTO
                                                .builder()
                                                .id(result.getId())
                                                .status(result.getStatus())
                                                .category(result.getCategory())
                                                .priority(result.getPriority())
                                                .diagnosticReportsIds(result.getDiagnosticReportsIds())
                                                .doNotPerform(result.getDoNotPerform())
                                                //Create a correct subject
                                                .subject(Patient.builder().id(result.getPatientId()).build())
                                                .serviceId(result.getServiceId())
                                                .createdAt(result.getCreatedAt())
                                                .createdBy(result.getCreatedBy())
                                                .updatedAt(result.getUpdatedAt())
                                                .updatedBy(result.getUpdatedBy())
                                                .build()
                                        )
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
     * {@code PATCH  /service-requests/:id} : Partial updates given fields of an existing serviceRequest, field will ignore if it is null
     *
     * @param id             the id of the serviceRequest to save.
     * @param serviceRequest the serviceRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceRequest,
     * or with status {@code 400 (Bad Request)} if the serviceRequest is not valid,
     * or with status {@code 404 (Not Found)} if the serviceRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ServiceRequest>> partialUpdateServiceRequest(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ServiceRequest serviceRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceRequest partially : {}, {}", id, serviceRequest);
        if (serviceRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return serviceRequestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ServiceRequest> result = serviceRequestRepository
                    .findById(serviceRequest.getId())
                    .map(existingServiceRequest -> {
                        if (serviceRequest.getStatus() != null) {
                            existingServiceRequest.setStatus(serviceRequest.getStatus());
                        }
                        if (serviceRequest.getCategory() != null) {
                            existingServiceRequest.setCategory(serviceRequest.getCategory());
                        }
                        if (serviceRequest.getPriority() != null) {
                            existingServiceRequest.setPriority(serviceRequest.getPriority());
                        }
                        if (serviceRequest.getDiagnosticReportsIds() != null) {
                            existingServiceRequest.setDiagnosticReportsIds(serviceRequest.getDiagnosticReportsIds());
                        }
                        if (serviceRequest.getDoNotPerform() != null) {
                            existingServiceRequest.setDoNotPerform(serviceRequest.getDoNotPerform());
                        }
                        if (serviceRequest.getServiceId() != null) {
                            existingServiceRequest.setServiceId(serviceRequest.getServiceId());
                        }
                        if (serviceRequest.getCreatedAt() != null) {
                            existingServiceRequest.setCreatedAt(serviceRequest.getCreatedAt());
                        }
                        if (serviceRequest.getCreatedBy() != null) {
                            existingServiceRequest.setCreatedBy(serviceRequest.getCreatedBy());
                        }
                        if (serviceRequest.getUpdatedAt() != null) {
                            existingServiceRequest.setUpdatedAt(serviceRequest.getUpdatedAt());
                        }
                        if (serviceRequest.getUpdatedBy() != null) {
                            existingServiceRequest.setUpdatedBy(serviceRequest.getUpdatedBy());
                        }
                        if (serviceRequest.getDeletedAt() != null) {
                            existingServiceRequest.setDeletedAt(serviceRequest.getDeletedAt());
                        }

                        return existingServiceRequest;
                    })
                    .flatMap(serviceRequestRepository::save);

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
     * {@code GET  /service-requests} : get all the serviceRequests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceRequests in body.
     */
    @GetMapping("/service-requests")
    public Mono<List<ServiceRequestResponseDTO>> getAllServiceRequests() {
        log.debug("REST request to get all ServiceRequests");
        return serviceRequestRepository
            .findAll()
            .collectList()
            .map(serviceRequests ->
                serviceRequests
                    .stream()
                    .map(serviceRequest ->
                        ServiceRequestResponseDTO
                            .builder()
                            .id(serviceRequest.getId())
                            .status(serviceRequest.getStatus())
                            .category(serviceRequest.getCategory())
                            .priority(serviceRequest.getPriority())
                            .diagnosticReportsIds(serviceRequest.getDiagnosticReportsIds())
                            .doNotPerform(serviceRequest.getDoNotPerform())
                            .subject(Patient.builder().id(serviceRequest.getPatientId()).build())
                            .serviceId(serviceRequest.getServiceId())
                            .createdAt(serviceRequest.getCreatedAt())
                            .createdBy(serviceRequest.getCreatedBy())
                            .updatedAt(serviceRequest.getUpdatedAt())
                            .updatedBy(serviceRequest.getUpdatedBy())
                            .build()
                    )
                    .collect(Collectors.toList())
            );
    }

    /**
     * {@code GET  /service-requests} : get all the serviceRequests as a stream.
     *
     * @return the {@link Flux} of serviceRequests.
     */
    @GetMapping(value = "/service-requests", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ServiceRequest> getAllServiceRequestsAsStream() {
        log.debug("REST request to get all ServiceRequests as a stream");
        return serviceRequestRepository.findAll();
    }

    /**
     * {@code GET  /service-requests/:id} : get the "id" serviceRequest.
     *
     * @param id the id of the serviceRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-requests/{id}")
    public Mono<ResponseEntity<ServiceRequestResponseDTO>> getServiceRequest(@PathVariable String id) {
        log.debug("REST request to get ServiceRequest : {}", id);
        Mono<ServiceRequest> serviceRequest = serviceRequestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(
            serviceRequest.flatMap(result ->
                Mono.just(
                    ServiceRequestResponseDTO
                        .builder()
                        .id(result.getId())
                        .status(result.getStatus())
                        .category(result.getCategory())
                        .priority(result.getPriority())
                        .diagnosticReportsIds(result.getDiagnosticReportsIds())
                        .doNotPerform(result.getDoNotPerform())
                        .subject(Patient.builder().id(result.getPatientId()).build())
                        .serviceId(result.getServiceId())
                        .createdAt(result.getCreatedAt())
                        .createdBy(result.getCreatedBy())
                        .updatedAt(result.getUpdatedAt())
                        .updatedBy(result.getUpdatedBy())
                        .build()
                )
            )
        );
    }
}
