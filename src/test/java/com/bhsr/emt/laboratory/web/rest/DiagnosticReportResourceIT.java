package com.bhsr.emt.laboratory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.bhsr.emt.laboratory.IntegrationTest;
import com.bhsr.emt.laboratory.domain.DiagnosticReport;
import com.bhsr.emt.laboratory.domain.User;
import com.bhsr.emt.laboratory.domain.enumeration.DiagnosticReportStatus;
import com.bhsr.emt.laboratory.repository.DiagnosticReportRepository;
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
 * Integration tests for the {@link DiagnosticReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DiagnosticReportResourceIT {

    private static final DiagnosticReportStatus DEFAULT_STATUS = DiagnosticReportStatus.REGISTERED;
    private static final DiagnosticReportStatus UPDATED_STATUS = DiagnosticReportStatus.PARTIAL;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final User DEFAULT_CREATED_BY = User.builder().firstName("AAAAAAAAAA").build();
    private static final User UPDATED_CREATED_BY = User.builder().firstName("BBBBBBBBBB").build();

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final User DEFAULT_UPDATED_BY = User.builder().firstName("AAAAAAAAAAA").build();
    private static final User UPDATED_UPDATED_BY = User.builder().firstName("BBBBBBBBBB").build();

    private static final LocalDate DEFAULT_DELETED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DELETED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/diagnostic-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DiagnosticReportRepository diagnosticReportRepository;

    @Autowired
    private WebTestClient webTestClient;

    private DiagnosticReport diagnosticReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiagnosticReport createEntity() {
        DiagnosticReport diagnosticReport = new DiagnosticReport();
        diagnosticReport.setCreatedAt(DEFAULT_CREATED_AT);
        diagnosticReport.setCreatedBy(DEFAULT_CREATED_BY);
        diagnosticReport.setUpdatedAt(DEFAULT_UPDATED_AT);
        diagnosticReport.setUpdatedBy(DEFAULT_UPDATED_BY);
        diagnosticReport.setDeletedAt(DEFAULT_DELETED_AT);
        return diagnosticReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiagnosticReport createUpdatedEntity() {
        DiagnosticReport diagnosticReport = new DiagnosticReport();
        diagnosticReport.setCreatedAt(UPDATED_CREATED_AT);
        diagnosticReport.setCreatedBy(UPDATED_CREATED_BY);
        diagnosticReport.setUpdatedAt(UPDATED_UPDATED_AT);
        diagnosticReport.setUpdatedBy(UPDATED_UPDATED_BY);
        diagnosticReport.setDeletedAt(UPDATED_DELETED_AT);
        return diagnosticReport;
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        diagnosticReportRepository.deleteAll().block();
        diagnosticReport = createEntity();
    }

    @Test
    void createDiagnosticReport() throws Exception {
        int databaseSizeBeforeCreate = diagnosticReportRepository.findAll().collectList().block().size();
        // Create the DiagnosticReport
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReport))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DiagnosticReport in the database
        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeCreate + 1);
        DiagnosticReport testDiagnosticReport = diagnosticReportList.get(diagnosticReportList.size() - 1);
        assertThat(testDiagnosticReport.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDiagnosticReport.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDiagnosticReport.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDiagnosticReport.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testDiagnosticReport.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testDiagnosticReport.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    void createDiagnosticReportWithExistingId() throws Exception {
        // Create the DiagnosticReport with an existing ID
        diagnosticReport.setId("existing_id");

        int databaseSizeBeforeCreate = diagnosticReportRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DiagnosticReport in the database
        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = diagnosticReportRepository.findAll().collectList().block().size();
        // set the field null
        diagnosticReport.setStatus(null);

        // Create the DiagnosticReport, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = diagnosticReportRepository.findAll().collectList().block().size();
        // set the field null
        diagnosticReport.setCreatedAt(null);

        // Create the DiagnosticReport, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = diagnosticReportRepository.findAll().collectList().block().size();
        // set the field null
        diagnosticReport.setCreatedBy(null);

        // Create the DiagnosticReport, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = diagnosticReportRepository.findAll().collectList().block().size();
        // set the field null
        diagnosticReport.setUpdatedAt(null);

        // Create the DiagnosticReport, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUpdatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = diagnosticReportRepository.findAll().collectList().block().size();
        // set the field null
        diagnosticReport.setUpdatedBy(null);

        // Create the DiagnosticReport, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDiagnosticReportsAsStream() {
        // Initialize the database
        diagnosticReport.setId(UUID.randomUUID().toString());
        diagnosticReportRepository.save(diagnosticReport).block();

        List<DiagnosticReport> diagnosticReportList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DiagnosticReport.class)
            .getResponseBody()
            .filter(diagnosticReport::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(diagnosticReportList).isNotNull();
        assertThat(diagnosticReportList).hasSize(1);
        DiagnosticReport testDiagnosticReport = diagnosticReportList.get(0);
        assertThat(testDiagnosticReport.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDiagnosticReport.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDiagnosticReport.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDiagnosticReport.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testDiagnosticReport.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testDiagnosticReport.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    void getAllDiagnosticReports() {
        // Initialize the database
        diagnosticReport.setId(UUID.randomUUID().toString());
        diagnosticReportRepository.save(diagnosticReport).block();

        // Get all the diagnosticReportList
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
            .value(hasItem(diagnosticReport.getId()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.[*].updatedBy")
            .value(hasItem(DEFAULT_UPDATED_BY))
            .jsonPath("$.[*].deletedAt")
            .value(hasItem(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getDiagnosticReport() {
        // Initialize the database
        diagnosticReport.setId(UUID.randomUUID().toString());
        diagnosticReportRepository.save(diagnosticReport).block();

        // Get the diagnosticReport
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, diagnosticReport.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(diagnosticReport.getId()))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()))
            .jsonPath("$.updatedBy")
            .value(is(DEFAULT_UPDATED_BY))
            .jsonPath("$.deletedAt")
            .value(is(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    void getNonExistingDiagnosticReport() {
        // Get the diagnosticReport
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDiagnosticReport() throws Exception {
        // Initialize the database
        diagnosticReport.setId(UUID.randomUUID().toString());
        diagnosticReportRepository.save(diagnosticReport).block();

        int databaseSizeBeforeUpdate = diagnosticReportRepository.findAll().collectList().block().size();

        // Update the diagnosticReport
        DiagnosticReport updatedDiagnosticReport = diagnosticReportRepository.findById(diagnosticReport.getId()).block();
        updatedDiagnosticReport.setCreatedAt(UPDATED_CREATED_AT);
        updatedDiagnosticReport.setCreatedBy(UPDATED_CREATED_BY);
        updatedDiagnosticReport.setUpdatedAt(UPDATED_UPDATED_AT);
        updatedDiagnosticReport.setUpdatedBy(UPDATED_UPDATED_BY);
        updatedDiagnosticReport.setDeletedAt(UPDATED_DELETED_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDiagnosticReport.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedDiagnosticReport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DiagnosticReport in the database
        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeUpdate);
        DiagnosticReport testDiagnosticReport = diagnosticReportList.get(diagnosticReportList.size() - 1);
        assertThat(testDiagnosticReport.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDiagnosticReport.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDiagnosticReport.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDiagnosticReport.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testDiagnosticReport.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testDiagnosticReport.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    void putNonExistingDiagnosticReport() throws Exception {
        int databaseSizeBeforeUpdate = diagnosticReportRepository.findAll().collectList().block().size();
        diagnosticReport.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, diagnosticReport.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DiagnosticReport in the database
        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDiagnosticReport() throws Exception {
        int databaseSizeBeforeUpdate = diagnosticReportRepository.findAll().collectList().block().size();
        diagnosticReport.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DiagnosticReport in the database
        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDiagnosticReport() throws Exception {
        int databaseSizeBeforeUpdate = diagnosticReportRepository.findAll().collectList().block().size();
        diagnosticReport.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReport))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DiagnosticReport in the database
        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDiagnosticReportWithPatch() throws Exception {
        // Initialize the database
        diagnosticReport.setId(UUID.randomUUID().toString());
        diagnosticReportRepository.save(diagnosticReport).block();

        int databaseSizeBeforeUpdate = diagnosticReportRepository.findAll().collectList().block().size();

        // Update the diagnosticReport using partial update
        DiagnosticReport partialUpdatedDiagnosticReport = new DiagnosticReport();
        partialUpdatedDiagnosticReport.setId(diagnosticReport.getId());

        partialUpdatedDiagnosticReport.setCreatedAt(UPDATED_CREATED_AT);
        partialUpdatedDiagnosticReport.setCreatedBy(UPDATED_CREATED_BY);
        partialUpdatedDiagnosticReport.setUpdatedAt(UPDATED_UPDATED_AT);
        partialUpdatedDiagnosticReport.setUpdatedBy(UPDATED_UPDATED_BY);
        partialUpdatedDiagnosticReport.setDeletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDiagnosticReport.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDiagnosticReport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DiagnosticReport in the database
        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeUpdate);
        DiagnosticReport testDiagnosticReport = diagnosticReportList.get(diagnosticReportList.size() - 1);
        assertThat(testDiagnosticReport.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDiagnosticReport.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDiagnosticReport.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDiagnosticReport.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testDiagnosticReport.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testDiagnosticReport.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    void fullUpdateDiagnosticReportWithPatch() throws Exception {
        // Initialize the database
        diagnosticReport.setId(UUID.randomUUID().toString());
        diagnosticReportRepository.save(diagnosticReport).block();

        int databaseSizeBeforeUpdate = diagnosticReportRepository.findAll().collectList().block().size();

        // Update the diagnosticReport using partial update
        DiagnosticReport partialUpdatedDiagnosticReport = new DiagnosticReport();
        partialUpdatedDiagnosticReport.setId(diagnosticReport.getId());

        partialUpdatedDiagnosticReport.setCreatedAt(UPDATED_CREATED_AT);
        partialUpdatedDiagnosticReport.setCreatedBy(UPDATED_CREATED_BY);
        partialUpdatedDiagnosticReport.setUpdatedAt(UPDATED_UPDATED_AT);
        partialUpdatedDiagnosticReport.setUpdatedBy(UPDATED_UPDATED_BY);
        partialUpdatedDiagnosticReport.setDeletedAt(UPDATED_DELETED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDiagnosticReport.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDiagnosticReport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DiagnosticReport in the database
        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeUpdate);
        DiagnosticReport testDiagnosticReport = diagnosticReportList.get(diagnosticReportList.size() - 1);
        assertThat(testDiagnosticReport.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDiagnosticReport.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDiagnosticReport.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDiagnosticReport.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testDiagnosticReport.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testDiagnosticReport.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    void patchNonExistingDiagnosticReport() throws Exception {
        int databaseSizeBeforeUpdate = diagnosticReportRepository.findAll().collectList().block().size();
        diagnosticReport.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, diagnosticReport.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DiagnosticReport in the database
        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDiagnosticReport() throws Exception {
        int databaseSizeBeforeUpdate = diagnosticReportRepository.findAll().collectList().block().size();
        diagnosticReport.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReport))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DiagnosticReport in the database
        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDiagnosticReport() throws Exception {
        int databaseSizeBeforeUpdate = diagnosticReportRepository.findAll().collectList().block().size();
        diagnosticReport.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReport))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DiagnosticReport in the database
        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDiagnosticReport() {
        // Initialize the database
        diagnosticReport.setId(UUID.randomUUID().toString());
        diagnosticReportRepository.save(diagnosticReport).block();

        int databaseSizeBeforeDelete = diagnosticReportRepository.findAll().collectList().block().size();

        // Delete the diagnosticReport
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, diagnosticReport.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DiagnosticReport> diagnosticReportList = diagnosticReportRepository.findAll().collectList().block();
        assertThat(diagnosticReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
