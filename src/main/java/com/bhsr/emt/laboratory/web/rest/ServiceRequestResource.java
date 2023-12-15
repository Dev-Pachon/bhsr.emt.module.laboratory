package com.bhsr.emt.laboratory.web.rest;

import com.bhsr.emt.laboratory.domain.DiagnosticReport;
import com.bhsr.emt.laboratory.domain.ServiceRequest;
import com.bhsr.emt.laboratory.domain.User;
import com.bhsr.emt.laboratory.domain.enumeration.DiagnosticReportStatus;
import com.bhsr.emt.laboratory.repository.DiagnosticReportFormatRepository;
import com.bhsr.emt.laboratory.repository.DiagnosticReportRepositoryAlternative;
import com.bhsr.emt.laboratory.repository.ServiceRequestRepository;
import com.bhsr.emt.laboratory.security.oauth2.OAuthIdpTokenResponseDTO;
import com.bhsr.emt.laboratory.service.UserService;
import com.bhsr.emt.laboratory.service.dto.DiagnosticReport.DiagnosticReportResponseLightDTO;
import com.bhsr.emt.laboratory.service.dto.Patient.PatientServiceResponse;
import com.bhsr.emt.laboratory.service.dto.ServiceRequest.ServiceRequestRequestDTO;
import com.bhsr.emt.laboratory.service.dto.ServiceRequest.ServiceRequestResponseDTO;
import com.bhsr.emt.laboratory.service.dto.ServiceRequest.ServiceRequestResponseLightDTO;
import com.bhsr.emt.laboratory.service.mapper.DiagnosticReportFormatMapper;
import com.bhsr.emt.laboratory.service.mapper.PatientMapper;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
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
    private static final String ENTITY_NAME = "laboratoryServiceRequest";
    private final Logger log = LoggerFactory.getLogger(ServiceRequestResource.class);
    private final WebClient webClient;
    private final UserService userService;
    private final ServiceRequestRepository serviceRequestRepository;
    private final DiagnosticReportFormatRepository diagnosticReportFormatRepository;
    private final PatientResource patientResource;
    private final DiagnosticReportRepositoryAlternative diagnosticReportRepository;
    private final DiagnosticReportFormatMapper diagnosticReportFormatMapper;
    private final PatientMapper patientMapper;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * {@code POST  /service-requests} : Create a new serviceRequest.
     *
     * @param serviceRequest the serviceRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceRequest, or with status {@code 400 (Bad Request)} if the serviceRequest has already an ID.
     */
    @PostMapping("/service-requests")
    public Mono<ResponseEntity<ServiceRequestResponseLightDTO>> createServiceRequest(
        @Valid @RequestBody ServiceRequestRequestDTO serviceRequest,
        Principal principal
    ) {
        if (!(principal instanceof AbstractAuthenticationToken)) {
            return Mono.error(new BadRequestAlertException("Invalid authToken", ENTITY_NAME, "unauthenticated"));
        }

        log.debug("REST request to save ServiceRequest : {}", serviceRequest);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", "admin");
        formData.add("password", "admin");
        formData.add("grant_type", "password");
        formData.add("client_id", "web_app");
        formData.add("client_secret", "web_app");

        return webClient
            .post()
            .uri("http://localhost:9080/auth/realms/EMT/protocol/openid-connect/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(formData))
            .exchangeToMono(clientResponse -> {
                if (clientResponse.statusCode().isError()) {
                    return Mono.error(
                        new BadRequestAlertException("Something went wrong for request token", ENTITY_NAME, "badclientrequest")
                    );
                }
                return clientResponse
                    .bodyToFlux(OAuthIdpTokenResponseDTO.class)
                    .collectList()
                    .flatMap(responseTokenAsList ->
                        webClient
                            .get()
                            .uri("http://localhost:5161/api/Pacient/" + serviceRequest.getSubject())
                            .header("Authorization", "Bearer " + responseTokenAsList.get(0).getAccessToken())
                            .retrieve()
                            .onStatus(
                                HttpStatus::isError,
                                clientResponse2 ->
                                    Mono.error(
                                        new BadRequestAlertException(
                                            "Something went wrong while getting the patient",
                                            ENTITY_NAME,
                                            "badclientrequest"
                                        )
                                    )
                            )
                            .bodyToFlux(PatientServiceResponse.class)
                            .collectList()
                            .flatMap(patientServiceResponse -> {
                                if (patientServiceResponse == null) {
                                    return Mono.error(new BadRequestAlertException("Invalid patient", ENTITY_NAME, "patientinvalid"));
                                }

                                return userService
                                    .getUserFromAuthentication((AbstractAuthenticationToken) principal)
                                    .flatMap(user -> {
                                        ServiceRequest serviceRequestToSave = ServiceRequest
                                            .builder()
                                            .status(serviceRequest.getStatus())
                                            .category(serviceRequest.getCategory())
                                            .priority(serviceRequest.getPriority())
                                            .diagnosticReportsIds(
                                                serviceRequest
                                                    .getDiagnosticReportsFormats()
                                                    .stream()
                                                    .map(format -> {
                                                        DiagnosticReport diagnosticReport = DiagnosticReport
                                                            .builder()
                                                            .status(DiagnosticReportStatus.REGISTERED)
                                                            .createdAt(LocalDate.now())
                                                            .createdBy(
                                                                User
                                                                    .builder()
                                                                    .id(user.getId())
                                                                    .firstName(user.getFirstName())
                                                                    .lastName(user.getLastName())
                                                                    .build()
                                                            )
                                                            .updatedAt(LocalDate.now())
                                                            .updatedBy(
                                                                User
                                                                    .builder()
                                                                    .id(user.getId())
                                                                    .firstName(user.getFirstName())
                                                                    .lastName(user.getLastName())
                                                                    .build()
                                                            )
                                                            .format(
                                                                diagnosticReportFormatMapper.DiagnosticReportFormatRequestDTOToDiagnosticReportFormat(
                                                                    format
                                                                )
                                                            )
                                                            .subject(serviceRequest.getSubject())
                                                            .build();
                                                        DiagnosticReport saved = diagnosticReportRepository.save(diagnosticReport);
                                                        return saved.getId();
                                                    })
                                                    .collect(Collectors.toSet())
                                            )
                                            .doNotPerform(false)
                                            .subject(serviceRequest.getSubject())
                                            .serviceId(TESTING_SERVICE_ID)
                                            .createdAt(LocalDate.now())
                                            .createdBy(
                                                User
                                                    .builder()
                                                    .id(user.getId())
                                                    .firstName(user.getFirstName())
                                                    .lastName(user.getLastName())
                                                    .build()
                                            )
                                            .updatedAt(LocalDate.now())
                                            .updatedBy(
                                                User
                                                    .builder()
                                                    .id(user.getId())
                                                    .firstName(user.getFirstName())
                                                    .lastName(user.getLastName())
                                                    .build()
                                            )
                                            .build();
                                        return serviceRequestRepository
                                            .save(serviceRequestToSave)
                                            .handle((result, sink) -> {
                                                try {
                                                    sink.next(
                                                        ResponseEntity
                                                            .created(new URI("/api/service-requests/" + result.getId()))
                                                            .headers(
                                                                HeaderUtil.createEntityCreationAlert(
                                                                    applicationName,
                                                                    true,
                                                                    ENTITY_NAME,
                                                                    result.getId()
                                                                )
                                                            )
                                                            .body(
                                                                ServiceRequestResponseLightDTO
                                                                    .builder()
                                                                    .id(result.getId())
                                                                    .status(result.getStatus())
                                                                    .category(result.getCategory())
                                                                    .priority(result.getPriority())
                                                                    .diagnosticReportsFormats(result.getDiagnosticReportsIds())
                                                                    .doNotPerform(result.getDoNotPerform())
                                                                    //Create a correct subject
                                                                    .subject(
                                                                        patientMapper.PatientServiceToPatient(patientServiceResponse.get(0))
                                                                    )
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
                            })
                    );
            });
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
     */
    @PatchMapping(value = "/service-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ServiceRequest>> partialUpdateServiceRequest(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ServiceRequest serviceRequest
    ) {
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
    public Mono<List<ServiceRequestResponseLightDTO>> getAllServiceRequests() {
        log.debug("REST request to get all ServiceRequests");

        return serviceRequestRepository
            .findAll()
            .flatMap(serviceRequest ->
                patientResource
                    .getPatientById(serviceRequest.getSubject())
                    .flatMap(patient ->
                        Mono.just(
                            ServiceRequestResponseLightDTO
                                .builder()
                                .id(serviceRequest.getId())
                                .status(serviceRequest.getStatus())
                                .category(serviceRequest.getCategory())
                                .priority(serviceRequest.getPriority())
                                .doNotPerform(serviceRequest.getDoNotPerform())
                                .subject(patient)
                                .diagnosticReportsFormats(serviceRequest.getDiagnosticReportsIds())
                                .serviceId(serviceRequest.getServiceId())
                                .createdAt(serviceRequest.getCreatedAt())
                                .createdBy(serviceRequest.getCreatedBy())
                                .updatedAt(serviceRequest.getUpdatedAt())
                                .updatedBy(serviceRequest.getUpdatedBy())
                                .build()
                        )
                    )
            )
            .collectList();
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
                patientResource
                    .getPatientById(result.getSubject())
                    .flatMap(patient ->
                        Mono.just(
                            ServiceRequestResponseDTO
                                .builder()
                                .id(result.getId())
                                .status(result.getStatus())
                                .category(result.getCategory())
                                .priority(result.getPriority())
                                .diagnosticReports(
                                    result
                                        .getDiagnosticReportsIds()
                                        .stream()
                                        .map(reportId -> {
                                            DiagnosticReport diagnosticReport = diagnosticReportRepository
                                                .findById(reportId)
                                                .orElse(DiagnosticReport.builder().build());
                                            return DiagnosticReportResponseLightDTO
                                                .builder()
                                                .id(diagnosticReport.getId())
                                                .status(diagnosticReport.getStatus())
                                                .createdAt(diagnosticReport.getCreatedAt())
                                                .createdBy(diagnosticReport.getCreatedBy())
                                                .updatedAt(diagnosticReport.getUpdatedAt())
                                                .updatedBy(diagnosticReport.getUpdatedBy())
                                                .format(diagnosticReport.getFormat().getName())
                                                .build();
                                        })
                                        .collect(Collectors.toSet())
                                )
                                .doNotPerform(result.getDoNotPerform())
                                .subject(patient)
                                .serviceId(result.getServiceId())
                                .createdAt(result.getCreatedAt())
                                .createdBy(result.getCreatedBy())
                                .updatedAt(result.getUpdatedAt())
                                .updatedBy(result.getUpdatedBy())
                                .build()
                        )
                    )
            )
        );
    }
}
