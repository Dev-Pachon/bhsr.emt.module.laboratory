package com.bhsr.emt.laboratory.web.rest;

import com.bhsr.emt.laboratory.domain.ServiceRequest;
import com.bhsr.emt.laboratory.repository.ServiceRequestRepository;
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
 * REST controller for managing {@link com.bhsr.emt.laboratory.domain.ServiceRequest}.
 */
@RestController
@RequestMapping("/api")
public class ServiceRequestResource {

    private final Logger log = LoggerFactory.getLogger(ServiceRequestResource.class);

    private static final String ENTITY_NAME = "laboratoryServiceRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceRequestRepository serviceRequestRepository;

    public ServiceRequestResource(ServiceRequestRepository serviceRequestRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
    }

    /**
     * {@code POST  /service-requests} : Create a new serviceRequest.
     *
     * @param serviceRequest the serviceRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceRequest, or with status {@code 400 (Bad Request)} if the serviceRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-requests")
    public Mono<ResponseEntity<ServiceRequest>> createServiceRequest(@Valid @RequestBody ServiceRequest serviceRequest)
        throws URISyntaxException {
        log.debug("REST request to save ServiceRequest : {}", serviceRequest);
        if (serviceRequest.getId() != null) {
            throw new BadRequestAlertException("A new serviceRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return serviceRequestRepository
            .save(serviceRequest)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/service-requests/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /service-requests/:id} : Updates an existing serviceRequest.
     *
     * @param id the id of the serviceRequest to save.
     * @param serviceRequest the serviceRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceRequest,
     * or with status {@code 400 (Bad Request)} if the serviceRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-requests/{id}")
    public Mono<ResponseEntity<ServiceRequest>> updateServiceRequest(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ServiceRequest serviceRequest
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceRequest : {}, {}", id, serviceRequest);
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

                return serviceRequestRepository
                    .save(serviceRequest)
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
     * {@code PATCH  /service-requests/:id} : Partial updates given fields of an existing serviceRequest, field will ignore if it is null
     *
     * @param id the id of the serviceRequest to save.
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
                        if (serviceRequest.getCode() != null) {
                            existingServiceRequest.setCode(serviceRequest.getCode());
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
    public Mono<List<ServiceRequest>> getAllServiceRequests() {
        log.debug("REST request to get all ServiceRequests");
        return serviceRequestRepository.findAll().collectList();
    }

    /**
     * {@code GET  /service-requests} : get all the serviceRequests as a stream.
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
    public Mono<ResponseEntity<ServiceRequest>> getServiceRequest(@PathVariable String id) {
        log.debug("REST request to get ServiceRequest : {}", id);
        Mono<ServiceRequest> serviceRequest = serviceRequestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(serviceRequest);
    }

    /**
     * {@code DELETE  /service-requests/:id} : delete the "id" serviceRequest.
     *
     * @param id the id of the serviceRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-requests/{id}")
    public Mono<ResponseEntity<Void>> deleteServiceRequest(@PathVariable String id) {
        log.debug("REST request to delete ServiceRequest : {}", id);
        return serviceRequestRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
