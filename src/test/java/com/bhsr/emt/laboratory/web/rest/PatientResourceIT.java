package com.bhsr.emt.laboratory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.bhsr.emt.laboratory.IntegrationTest;
import com.bhsr.emt.laboratory.domain.Patient;
import com.bhsr.emt.laboratory.domain.enumeration.AdministrativeGender;
import com.bhsr.emt.laboratory.repository.PatientRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link PatientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PatientResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final AdministrativeGender DEFAULT_GENDER = AdministrativeGender.MALE;
    private static final AdministrativeGender UPDATED_GENDER = AdministrativeGender.FEMALE;

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/patients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Patient patient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity() {
        Patient patient = new Patient();
        patient.setActive(DEFAULT_ACTIVE);
        patient.setGender(DEFAULT_GENDER);
        patient.setBirthDate(DEFAULT_BIRTH_DATE);
        return patient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity() {
        Patient patient = new Patient();
        patient.setActive(DEFAULT_ACTIVE);
        patient.setGender(DEFAULT_GENDER);
        patient.setBirthDate(DEFAULT_BIRTH_DATE);
        return patient;
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        patientRepository.deleteAll().block();
        patient = createEntity();
    }

    @Test
    void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().collectList().block().size();
        // Create the Patient
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(patient))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testPatient.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPatient.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
    }

    @Test
    void createPatientWithExistingId() throws Exception {
        // Create the Patient with an existing ID
        patient.setId("existing_id");

        int databaseSizeBeforeCreate = patientRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(patient))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().collectList().block().size();
        // set the field null
        patient.setActive(null);

        // Create the Patient, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(patient))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().collectList().block().size();
        // set the field null
        patient.setGender(null);

        // Create the Patient, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(patient))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().collectList().block().size();
        // set the field null
        patient.setBirthDate(null);

        // Create the Patient, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(patient))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPatientsAsStream() {
        // Initialize the database
        patient.setId(UUID.randomUUID().toString());
        patientRepository.save(patient).block();

        List<Patient> patientList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Patient.class)
            .getResponseBody()
            .filter(patient::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(patientList).isNotNull();
        assertThat(patientList).hasSize(1);
        Patient testPatient = patientList.get(0);
        assertThat(testPatient.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testPatient.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPatient.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
    }

    @Test
    void getAllPatients() {
        // Initialize the database
        patient.setId(UUID.randomUUID().toString());
        patientRepository.save(patient).block();

        // Get all the patientList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(patient.getId()))
            .jsonPath("$.[*].active")
            .value(hasItem(DEFAULT_ACTIVE.booleanValue()))
            .jsonPath("$.[*].gender")
            .value(hasItem(DEFAULT_GENDER.toString()))
            .jsonPath("$.[*].birthDate")
            .value(hasItem(DEFAULT_BIRTH_DATE.toString()));
    }

    @Test
    void getPatient() {
        // Initialize the database
        patient.setId(UUID.randomUUID().toString());
        patientRepository.save(patient).block();

        // Get the patient
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, patient.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(patient.getId()))
            .jsonPath("$.active")
            .value(is(DEFAULT_ACTIVE.booleanValue()))
            .jsonPath("$.gender")
            .value(is(DEFAULT_GENDER.toString()))
            .jsonPath("$.birthDate")
            .value(is(DEFAULT_BIRTH_DATE.toString()));
    }

    @Test
    void getNonExistingPatient() {
        // Get the patient
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPatient() throws Exception {
        // Initialize the database
        patient.setId(UUID.randomUUID().toString());
        patientRepository.save(patient).block();

        int databaseSizeBeforeUpdate = patientRepository.findAll().collectList().block().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).block();
        updatedPatient.setActive(DEFAULT_ACTIVE);
        updatedPatient.setGender(DEFAULT_GENDER);
        updatedPatient.setBirthDate(DEFAULT_BIRTH_DATE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPatient.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPatient))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPatient.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPatient.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    void putNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().collectList().block().size();
        patient.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, patient.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(patient))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().collectList().block().size();
        patient.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(patient))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().collectList().block().size();
        patient.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(patient))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        patient.setId(UUID.randomUUID().toString());
        patientRepository.save(patient).block();

        int databaseSizeBeforeUpdate = patientRepository.findAll().collectList().block().size();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient.setActive(DEFAULT_ACTIVE);
        partialUpdatedPatient.setGender(DEFAULT_GENDER);
        partialUpdatedPatient.setBirthDate(DEFAULT_BIRTH_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPatient))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPatient.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPatient.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    void fullUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        patient.setId(UUID.randomUUID().toString());
        patientRepository.save(patient).block();

        int databaseSizeBeforeUpdate = patientRepository.findAll().collectList().block().size();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient.setActive(DEFAULT_ACTIVE);
        partialUpdatedPatient.setGender(DEFAULT_GENDER);
        partialUpdatedPatient.setBirthDate(DEFAULT_BIRTH_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPatient))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPatient.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPatient.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    void patchNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().collectList().block().size();
        patient.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, patient.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(patient))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().collectList().block().size();
        patient.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(patient))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().collectList().block().size();
        patient.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(patient))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePatient() {
        // Initialize the database
        patient.setId(UUID.randomUUID().toString());
        patientRepository.save(patient).block();

        int databaseSizeBeforeDelete = patientRepository.findAll().collectList().block().size();

        // Delete the patient
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, patient.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Patient> patientList = patientRepository.findAll().collectList().block();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
