package com.bhsr.emt.laboratory.web.rest;

import com.bhsr.emt.laboratory.domain.HumanName;
import com.bhsr.emt.laboratory.domain.Identifier;
import com.bhsr.emt.laboratory.domain.IdentifierType;
import com.bhsr.emt.laboratory.domain.Patient;
import com.bhsr.emt.laboratory.domain.enumeration.AdministrativeGender;
import com.bhsr.emt.laboratory.repository.PatientRepository;
import com.bhsr.emt.laboratory.security.oauth2.OAuthIdpTokenResponseDTO;
import com.bhsr.emt.laboratory.service.dto.Patient.PatientField;
import com.bhsr.emt.laboratory.service.dto.Patient.PatientServiceResponse;
import com.bhsr.emt.laboratory.service.mapper.PatientMapper;
import com.bhsr.emt.laboratory.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
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
 * REST controller for managing {@link com.bhsr.emt.laboratory.domain.Patient}.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PatientResource {

    private static final String ENTITY_NAME = "laboratoryPatient";
    private final Logger log = LoggerFactory.getLogger(PatientResource.class);
    private final PatientRepository patientRepository;
    private final WebClient webClient;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientMapper patientMapper;

    /**
     * {@code POST  /patients} : Create a new patient.
     *
     * @param patient the patient to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patient, or with status {@code 400 (Bad Request)} if the patient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patients")
    public Mono<ResponseEntity<Patient>> createPatient(@Valid @RequestBody Patient patient) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", patient);
        if (patient.getId() != null) {
            throw new BadRequestAlertException("A new patient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return patientRepository
            .save(patient)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/patients/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /patients/:id} : Updates an existing patient.
     *
     * @param id      the id of the patient to save.
     * @param patient the patient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patient,
     * or with status {@code 400 (Bad Request)} if the patient is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patients/{id}")
    public Mono<ResponseEntity<Patient>> updatePatient(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Patient patient
    ) throws URISyntaxException {
        log.debug("REST request to update Patient : {}, {}", id, patient);
        if (patient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return patientRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return patientRepository
                    .save(patient)
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
     * {@code PATCH  /patients/:id} : Partial updates given fields of an existing patient, field will ignore if it is null
     *
     * @param id      the id of the patient to save.
     * @param patient the patient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patient,
     * or with status {@code 400 (Bad Request)} if the patient is not valid,
     * or with status {@code 404 (Not Found)} if the patient is not found,
     * or with status {@code 500 (Internal Server Error)} if the patient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/patients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Patient>> partialUpdatePatient(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Patient patient
    ) throws URISyntaxException {
        log.debug("REST request to partial update Patient partially : {}, {}", id, patient);
        if (patient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return patientRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Patient> result = patientRepository
                    .findById(patient.getId())
                    .map(existingPatient -> {
                        if (patient.getActive() != null) {
                            existingPatient.setActive(patient.getActive());
                        }
                        if (patient.getGender() != null) {
                            existingPatient.setGender(patient.getGender());
                        }
                        if (patient.getBirthDate() != null) {
                            existingPatient.setBirthDate(patient.getBirthDate());
                        }

                        return existingPatient;
                    })
                    .flatMap(patientRepository::save);

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
     * {@code GET  /patients} : get all the patients.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patients in body.
     */
    @GetMapping("/patients")
    public Mono<List<Patient>> getAllPatients(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String value,
        Principal principal
    ) {
        log.debug("REST request to get all Patients");

        if (name != null && value != null) {
            return getPatientsByField(name, value);
        } else {
            return getPatients();
        }
    }

    /**
     * {@code GET  /patients} : get all the patients as a stream.
     *
     * @return the {@link Flux} of patients.
     */
    @GetMapping(value = "/patients", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Patient> getAllPatientsAsStream() {
        log.debug("REST request to get all Patients as a stream");
        return patientRepository.findAll();
    }

    /**
     * {@code GET  /patients/:id} : get the "id" patient.
     *
     * @param id the id of the patient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patient, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patients/{id}")
    public Mono<ResponseEntity<Patient>> getPatient(@PathVariable String id, Principal principal) {
        log.debug("REST request to get Patient : {}", id);

        if (!(principal instanceof AbstractAuthenticationToken)) {
            return Mono.error(new BadRequestAlertException("Invalid authToken", ENTITY_NAME, "unauthenticated"));
        }
        if (id == null) {
            return Mono.error(new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull"));
        }

        return ResponseUtil.wrapOrNotFound(getPatientById(id));
    }

    public Mono<Patient> getPatientById(String id) {
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
                            .uri("http://localhost:5161/api/Pacient/" + id)
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
                                if (patientServiceResponse == null || patientServiceResponse.isEmpty()) {
                                    return Mono.error(new BadRequestAlertException("Not found patient", ENTITY_NAME, "patientidinvalid"));
                                }

                                return Mono.just(patientMapper.PatientServiceToPatient(patientServiceResponse.get(0)));
                            })
                    );
            });
    }

    public Mono<List<Patient>> getPatientsByField(String name, String value) {
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
                            .method(HttpMethod.GET)
                            .uri("http://localhost:5161/api/Pacient/GetByField")
                            .header("Authorization", "Bearer " + responseTokenAsList.get(0).getAccessToken())
                            .bodyValue(PatientField.builder().Name(name).Value(value).build())
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
                            .flatMap(patientServiceResponse -> {
                                if (patientServiceResponse == null) {
                                    return Mono.error(new BadRequestAlertException("Not found patient", ENTITY_NAME, "patientidinvalid"));
                                }

                                Patient.PatientBuilder patientResponse = Patient.builder();
                                HumanName.HumanNameBuilder humanName = HumanName.builder();
                                Identifier.IdentifierBuilder identifier = Identifier.builder();
                                AtomicReference<String> Name = new AtomicReference<>("");
                                AtomicReference<String> lastName = new AtomicReference<>("");
                                AtomicReference<String> secondLastName = new AtomicReference<>("");
                                AtomicReference<String> middleName = new AtomicReference<>("");

                                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

                                patientServiceResponse
                                    .getFieldsList()
                                    .forEach(field -> {
                                        switch (field.getName()) {
                                            case "idType":
                                                identifier.type(
                                                    IdentifierType.builder().id(UUID.randomUUID()).name(field.getValue()).build()
                                                );
                                                break;
                                            case "idNumber":
                                                identifier.value(field.getValue());
                                                break;
                                            case "birthDate":
                                                patientResponse.birthDate(LocalDate.parse(field.getValue(), inputFormatter));
                                                break;
                                            case "gender":
                                                patientResponse.gender(AdministrativeGender.valueOf(field.getValue()));
                                                break;
                                            case "Name":
                                                Name.set(field.getValue());
                                                break;
                                            case "lastName":
                                                lastName.set(field.getValue());
                                                break;
                                            case "middleName":
                                                middleName.set(field.getValue());
                                                break;
                                            case "secondLastName":
                                                secondLastName.set(field.getValue());
                                                break;
                                        }
                                    });

                                patientResponse.name(
                                    humanName
                                        .given(String.join(" ", Name.get(), middleName.get()))
                                        .family(String.join(" ", lastName.get(), secondLastName.get()))
                                        .text(String.join(" ", Name.get(), middleName.get(), lastName.get(), secondLastName.get()))
                                        .build()
                                );

                                patientResponse.identifier(identifier.build());
                                patientResponse.active(true);
                                patientResponse.id(patientServiceResponse.getId());

                                return Mono.just(patientResponse.build());
                            })
                            .collectList()
                    );
            });
    }

    public Mono<List<Patient>> getPatients() {
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
                            .uri("http://localhost:5161/api/Pacient")
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
                            .flatMap(patientServiceResponse -> {
                                if (patientServiceResponse == null) {
                                    return Mono.error(new BadRequestAlertException("Not found patient", ENTITY_NAME, "patientidinvalid"));
                                }
                                Patient.PatientBuilder patientResponse = Patient.builder();
                                HumanName.HumanNameBuilder humanName = HumanName.builder();
                                Identifier.IdentifierBuilder identifier = Identifier.builder();
                                AtomicReference<String> Name = new AtomicReference<>("");
                                AtomicReference<String> lastName = new AtomicReference<>("");
                                AtomicReference<String> secondLastName = new AtomicReference<>("");
                                AtomicReference<String> middleName = new AtomicReference<>("");

                                patientServiceResponse
                                    .getFieldsList()
                                    .forEach(field -> {
                                        switch (field.getName()) {
                                            case "idType":
                                                identifier.type(
                                                    IdentifierType.builder().id(UUID.randomUUID()).name(field.getValue()).build()
                                                );
                                                break;
                                            case "idNumber":
                                                identifier.value(field.getValue());
                                                break;
                                            case "birthDate":
                                                patientResponse.birthDate(LocalDate.parse(field.getValue()));
                                                break;
                                            case "gender":
                                                patientResponse.gender(AdministrativeGender.valueOf(field.getValue()));
                                                break;
                                            case "Name":
                                                Name.set(field.getValue());
                                                break;
                                            case "lastName":
                                                lastName.set(field.getValue());
                                                break;
                                            case "middleName":
                                                middleName.set(field.getValue());
                                                break;
                                            case "secondLastName":
                                                secondLastName.set(field.getValue());
                                                break;
                                        }
                                    });

                                patientResponse.name(
                                    humanName
                                        .given(String.join(" ", Name.get(), middleName.get()))
                                        .family(String.join(" ", lastName.get(), secondLastName.get()))
                                        .text(String.join(" ", Name.get(), middleName.get(), lastName.get(), secondLastName.get()))
                                        .build()
                                );

                                patientResponse.identifier(identifier.build());

                                patientResponse.active(true);

                                patientResponse.id(patientServiceResponse.getId());

                                return Mono.just(patientResponse.build());
                            })
                            .collectList()
                    );
            });
    }

    /**
     * {@code DELETE  /patients/:id} : delete the "id" patient.
     *
     * @param id the id of the patient to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patients/{id}")
    public Mono<ResponseEntity<Void>> deletePatient(@PathVariable String id) {
        log.debug("REST request to delete Patient : {}", id);
        return patientRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
