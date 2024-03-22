package com.bhsr.emt.laboratory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.bhsr.emt.laboratory.IntegrationTest;
import com.bhsr.emt.laboratory.domain.DiagnosticReportFormat;
import com.bhsr.emt.laboratory.repository.DiagnosticReportFormatRepository;
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
 * Integration tests for the {@link DiagnosticReportFormatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DiagnosticReportFormatResourceIT {

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DELETED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DELETED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/diagnostic-report-formats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DiagnosticReportFormatRepository diagnosticReportFormatRepository;

    @Autowired
    private WebTestClient webTestClient;

    private DiagnosticReportFormat diagnosticReportFormat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiagnosticReportFormat createEntity() {
        DiagnosticReportFormat diagnosticReportFormat = new DiagnosticReportFormat();
        diagnosticReportFormat.setCreatedAt(DEFAULT_CREATED_AT);
        diagnosticReportFormat.setCreatedBy(DEFAULT_CREATED_BY);
        diagnosticReportFormat.setUpdatedAt(DEFAULT_UPDATED_AT);
        diagnosticReportFormat.setUpdatedBy(DEFAULT_UPDATED_BY);
        diagnosticReportFormat.setDeletedAt(DEFAULT_DELETED_AT);
        return diagnosticReportFormat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiagnosticReportFormat createUpdatedEntity() {
        DiagnosticReportFormat diagnosticReportFormat = new DiagnosticReportFormat();
        diagnosticReportFormat.setCreatedAt(UPDATED_CREATED_AT);
        diagnosticReportFormat.setCreatedBy(UPDATED_CREATED_BY);
        diagnosticReportFormat.setUpdatedAt(UPDATED_UPDATED_AT);
        diagnosticReportFormat.setUpdatedBy(UPDATED_UPDATED_BY);
        diagnosticReportFormat.setDeletedAt(UPDATED_DELETED_AT);
        return diagnosticReportFormat;
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        diagnosticReportFormatRepository.deleteAll().block();
        diagnosticReportFormat = createEntity();
    }

    @Test
    void createDiagnosticReportFormat() throws Exception {
        int databaseSizeBeforeCreate = diagnosticReportFormatRepository.findAll().collectList().block().size();
        // Create the DiagnosticReportFormat
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReportFormat))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DiagnosticReportFormat in the database
        List<DiagnosticReportFormat> diagnosticReportFormatList = diagnosticReportFormatRepository.findAll().collectList().block();
        assertThat(diagnosticReportFormatList).hasSize(databaseSizeBeforeCreate + 1);
        DiagnosticReportFormat testDiagnosticReportFormat = diagnosticReportFormatList.get(diagnosticReportFormatList.size() - 1);
        assertThat(testDiagnosticReportFormat.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDiagnosticReportFormat.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDiagnosticReportFormat.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testDiagnosticReportFormat.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testDiagnosticReportFormat.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    void createDiagnosticReportFormatWithExistingId() throws Exception {
        // Create the DiagnosticReportFormat with an existing ID
        diagnosticReportFormat.setId("existing_id");

        int databaseSizeBeforeCreate = diagnosticReportFormatRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReportFormat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DiagnosticReportFormat in the database
        List<DiagnosticReportFormat> diagnosticReportFormatList = diagnosticReportFormatRepository.findAll().collectList().block();
        assertThat(diagnosticReportFormatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = diagnosticReportFormatRepository.findAll().collectList().block().size();
        // set the field null
        diagnosticReportFormat.setCreatedAt(null);

        // Create the DiagnosticReportFormat, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReportFormat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DiagnosticReportFormat> diagnosticReportFormatList = diagnosticReportFormatRepository.findAll().collectList().block();
        assertThat(diagnosticReportFormatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = diagnosticReportFormatRepository.findAll().collectList().block().size();
        // set the field null
        diagnosticReportFormat.setCreatedBy(null);

        // Create the DiagnosticReportFormat, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReportFormat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DiagnosticReportFormat> diagnosticReportFormatList = diagnosticReportFormatRepository.findAll().collectList().block();
        assertThat(diagnosticReportFormatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = diagnosticReportFormatRepository.findAll().collectList().block().size();
        // set the field null
        diagnosticReportFormat.setUpdatedAt(null);

        // Create the DiagnosticReportFormat, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReportFormat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DiagnosticReportFormat> diagnosticReportFormatList = diagnosticReportFormatRepository.findAll().collectList().block();
        assertThat(diagnosticReportFormatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUpdatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = diagnosticReportFormatRepository.findAll().collectList().block().size();
        // set the field null
        diagnosticReportFormat.setUpdatedBy(null);

        // Create the DiagnosticReportFormat, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReportFormat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DiagnosticReportFormat> diagnosticReportFormatList = diagnosticReportFormatRepository.findAll().collectList().block();
        assertThat(diagnosticReportFormatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDiagnosticReportFormatsAsStream() {
        // Initialize the database
        diagnosticReportFormat.setId(UUID.randomUUID().toString());
        diagnosticReportFormatRepository.save(diagnosticReportFormat).block();

        List<DiagnosticReportFormat> diagnosticReportFormatList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DiagnosticReportFormat.class)
            .getResponseBody()
            .filter(diagnosticReportFormat::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(diagnosticReportFormatList).isNotNull();
        assertThat(diagnosticReportFormatList).hasSize(1);
        DiagnosticReportFormat testDiagnosticReportFormat = diagnosticReportFormatList.get(0);
        assertThat(testDiagnosticReportFormat.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDiagnosticReportFormat.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDiagnosticReportFormat.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testDiagnosticReportFormat.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testDiagnosticReportFormat.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    void getAllDiagnosticReportFormats() {
        // Initialize the database
        diagnosticReportFormat.setId(UUID.randomUUID().toString());
        diagnosticReportFormatRepository.save(diagnosticReportFormat).block();

        // Get all the diagnosticReportFormatList
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
            .value(hasItem(diagnosticReportFormat.getId()))
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
    void getDiagnosticReportFormat() {
        // Initialize the database
        diagnosticReportFormat.setId(UUID.randomUUID().toString());
        diagnosticReportFormatRepository.save(diagnosticReportFormat).block();

        // Get the diagnosticReportFormat
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, diagnosticReportFormat.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(diagnosticReportFormat.getId()))
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
    void getNonExistingDiagnosticReportFormat() {
        // Get the diagnosticReportFormat
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNonExistingDiagnosticReportFormat() throws Exception {
        int databaseSizeBeforeUpdate = diagnosticReportFormatRepository.findAll().collectList().block().size();
        diagnosticReportFormat.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, diagnosticReportFormat.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReportFormat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DiagnosticReportFormat in the database
        List<DiagnosticReportFormat> diagnosticReportFormatList = diagnosticReportFormatRepository.findAll().collectList().block();
        assertThat(diagnosticReportFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDiagnosticReportFormat() throws Exception {
        int databaseSizeBeforeUpdate = diagnosticReportFormatRepository.findAll().collectList().block().size();
        diagnosticReportFormat.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReportFormat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DiagnosticReportFormat in the database
        List<DiagnosticReportFormat> diagnosticReportFormatList = diagnosticReportFormatRepository.findAll().collectList().block();
        assertThat(diagnosticReportFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDiagnosticReportFormat() throws Exception {
        int databaseSizeBeforeUpdate = diagnosticReportFormatRepository.findAll().collectList().block().size();
        diagnosticReportFormat.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReportFormat))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DiagnosticReportFormat in the database
        List<DiagnosticReportFormat> diagnosticReportFormatList = diagnosticReportFormatRepository.findAll().collectList().block();
        assertThat(diagnosticReportFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchNonExistingDiagnosticReportFormat() throws Exception {
        int databaseSizeBeforeUpdate = diagnosticReportFormatRepository.findAll().collectList().block().size();
        diagnosticReportFormat.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, diagnosticReportFormat.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReportFormat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DiagnosticReportFormat in the database
        List<DiagnosticReportFormat> diagnosticReportFormatList = diagnosticReportFormatRepository.findAll().collectList().block();
        assertThat(diagnosticReportFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDiagnosticReportFormat() throws Exception {
        int databaseSizeBeforeUpdate = diagnosticReportFormatRepository.findAll().collectList().block().size();
        diagnosticReportFormat.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReportFormat))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DiagnosticReportFormat in the database
        List<DiagnosticReportFormat> diagnosticReportFormatList = diagnosticReportFormatRepository.findAll().collectList().block();
        assertThat(diagnosticReportFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDiagnosticReportFormat() throws Exception {
        int databaseSizeBeforeUpdate = diagnosticReportFormatRepository.findAll().collectList().block().size();
        diagnosticReportFormat.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(diagnosticReportFormat))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DiagnosticReportFormat in the database
        List<DiagnosticReportFormat> diagnosticReportFormatList = diagnosticReportFormatRepository.findAll().collectList().block();
        assertThat(diagnosticReportFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDiagnosticReportFormat() {
        // Initialize the database
        diagnosticReportFormat.setId(UUID.randomUUID().toString());
        diagnosticReportFormatRepository.save(diagnosticReportFormat).block();

        int databaseSizeBeforeDelete = diagnosticReportFormatRepository.findAll().collectList().block().size();

        // Delete the diagnosticReportFormat
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, diagnosticReportFormat.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DiagnosticReportFormat> diagnosticReportFormatList = diagnosticReportFormatRepository.findAll().collectList().block();
        assertThat(diagnosticReportFormatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
