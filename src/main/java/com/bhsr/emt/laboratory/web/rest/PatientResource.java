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
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
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
import reactor.core.publisher.Mono;
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
    private final WebClient webClient;
    private final PatientMapper patientMapper;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${spring.security.oauth2.client.provider.oidc.issuer-uri}")
    private String issuerUri;

    @Value("${webclient.module.ehr.uri}")
    private String ehrUri;

    @Value("${webclient.security.oauth2.client.registration.oidc.client-id}")
    private String clientId;

    @Value("${webclient.security.oauth2.client.registration.oidc.client-secret}")
    private String clientSecret;

    @Value("${webclient.security.oauth2.client.registration.oidc.username}")
    private String clientUsername;

    @Value("${webclient.security.oauth2.client.registration.oidc.password}")
    private String clientPassword;

    @Value("${webclient.security.oauth2.client.registration.oidc.grant-type}")
    private String clientGrantType;

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
        formData.add("username", clientUsername);
        formData.add("password", clientPassword);
        formData.add("grant_type", clientGrantType);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        return webClient
            .post()
            .uri(issuerUri + "/protocol/openid-connect/token")
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
                            .uri(ehrUri + "/Pacient/" + id)
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
                    )
                    .doOnNext(p -> webClient.get());
            });
    }

    public Mono<List<Patient>> getPatientsByField(String name, String value) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", clientUsername);
        formData.add("password", clientPassword);
        formData.add("grant_type", clientGrantType);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        return webClient
            .post()
            .uri(issuerUri + "/protocol/openid-connect/token")
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
                            .uri(ehrUri + "/Pacient/GetByField")
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
        formData.add("username", clientUsername);
        formData.add("password", clientPassword);
        formData.add("grant_type", clientGrantType);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        return webClient
            .post()
            .uri(issuerUri + "/protocol/openid-connect/token")
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
                            .uri(ehrUri + "/Pacient")
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
}
