package com.bhsr.emt.laboratory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.bhsr.emt.laboratory.IntegrationTest;
import com.bhsr.emt.laboratory.domain.IdentifierType;
import com.bhsr.emt.laboratory.repository.IdentifierTypeRepository;
import java.time.Duration;
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
 * Integration tests for the {@link IdentifierTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class IdentifierTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/identifier-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private IdentifierTypeRepository identifierTypeRepository;

    @Autowired
    private WebTestClient webTestClient;

    private IdentifierType identifierType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdentifierType createEntity() {
        IdentifierType identifierType = new IdentifierType();
        identifierType.setName(DEFAULT_NAME);
        return identifierType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdentifierType createUpdatedEntity() {
        IdentifierType identifierType = new IdentifierType();
        identifierType.setName(UPDATED_NAME);
        return identifierType;
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        identifierTypeRepository.deleteAll().block();
        identifierType = createEntity();
    }

    @Test
    void createIdentifierType() throws Exception {
        int databaseSizeBeforeCreate = identifierTypeRepository.findAll().collectList().block().size();
        // Create the IdentifierType
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identifierType))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IdentifierType in the database
        List<IdentifierType> identifierTypeList = identifierTypeRepository.findAll().collectList().block();
        assertThat(identifierTypeList).hasSize(databaseSizeBeforeCreate + 1);
        IdentifierType testIdentifierType = identifierTypeList.get(identifierTypeList.size() - 1);
        assertThat(testIdentifierType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createIdentifierTypeWithExistingId() throws Exception {
        // Create the IdentifierType with an existing ID
        identifierType.setId(UUID.randomUUID());

        int databaseSizeBeforeCreate = identifierTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identifierType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentifierType in the database
        List<IdentifierType> identifierTypeList = identifierTypeRepository.findAll().collectList().block();
        assertThat(identifierTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = identifierTypeRepository.findAll().collectList().block().size();
        // set the field null
        identifierType.setName(null);

        // Create the IdentifierType, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identifierType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IdentifierType> identifierTypeList = identifierTypeRepository.findAll().collectList().block();
        assertThat(identifierTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllIdentifierTypesAsStream() {
        // Initialize the database
        identifierType.setId(UUID.randomUUID());
        identifierTypeRepository.save(identifierType).block();

        List<IdentifierType> identifierTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(IdentifierType.class)
            .getResponseBody()
            .filter(identifierType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(identifierTypeList).isNotNull();
        assertThat(identifierTypeList).hasSize(1);
        IdentifierType testIdentifierType = identifierTypeList.get(0);
        assertThat(testIdentifierType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllIdentifierTypes() {
        // Initialize the database
        identifierType.setId(UUID.randomUUID());
        identifierTypeRepository.save(identifierType).block();

        // Get all the identifierTypeList
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
            .value(hasItem(identifierType.getId().toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getIdentifierType() {
        // Initialize the database
        identifierType.setId(UUID.randomUUID());
        identifierTypeRepository.save(identifierType).block();

        // Get the identifierType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, identifierType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(identifierType.getId().toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingIdentifierType() {
        // Get the identifierType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingIdentifierType() throws Exception {
        // Initialize the database
        identifierType.setId(UUID.randomUUID());
        identifierTypeRepository.save(identifierType).block();

        int databaseSizeBeforeUpdate = identifierTypeRepository.findAll().collectList().block().size();

        // Update the identifierType
        IdentifierType updatedIdentifierType = identifierTypeRepository.findById(identifierType.getId()).block();
        updatedIdentifierType.setName(UPDATED_NAME);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedIdentifierType.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedIdentifierType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IdentifierType in the database
        List<IdentifierType> identifierTypeList = identifierTypeRepository.findAll().collectList().block();
        assertThat(identifierTypeList).hasSize(databaseSizeBeforeUpdate);
        IdentifierType testIdentifierType = identifierTypeList.get(identifierTypeList.size() - 1);
        assertThat(testIdentifierType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingIdentifierType() throws Exception {
        int databaseSizeBeforeUpdate = identifierTypeRepository.findAll().collectList().block().size();
        identifierType.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, identifierType.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identifierType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentifierType in the database
        List<IdentifierType> identifierTypeList = identifierTypeRepository.findAll().collectList().block();
        assertThat(identifierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIdentifierType() throws Exception {
        int databaseSizeBeforeUpdate = identifierTypeRepository.findAll().collectList().block().size();
        identifierType.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identifierType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentifierType in the database
        List<IdentifierType> identifierTypeList = identifierTypeRepository.findAll().collectList().block();
        assertThat(identifierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIdentifierType() throws Exception {
        int databaseSizeBeforeUpdate = identifierTypeRepository.findAll().collectList().block().size();
        identifierType.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identifierType))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IdentifierType in the database
        List<IdentifierType> identifierTypeList = identifierTypeRepository.findAll().collectList().block();
        assertThat(identifierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIdentifierTypeWithPatch() throws Exception {
        // Initialize the database
        identifierType.setId(UUID.randomUUID());
        identifierTypeRepository.save(identifierType).block();

        int databaseSizeBeforeUpdate = identifierTypeRepository.findAll().collectList().block().size();

        // Update the identifierType using partial update
        IdentifierType partialUpdatedIdentifierType = new IdentifierType();
        partialUpdatedIdentifierType.setId(identifierType.getId());

        partialUpdatedIdentifierType.setName(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIdentifierType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIdentifierType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IdentifierType in the database
        List<IdentifierType> identifierTypeList = identifierTypeRepository.findAll().collectList().block();
        assertThat(identifierTypeList).hasSize(databaseSizeBeforeUpdate);
        IdentifierType testIdentifierType = identifierTypeList.get(identifierTypeList.size() - 1);
        assertThat(testIdentifierType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void fullUpdateIdentifierTypeWithPatch() throws Exception {
        // Initialize the database
        identifierType.setId(UUID.randomUUID());
        identifierTypeRepository.save(identifierType).block();

        int databaseSizeBeforeUpdate = identifierTypeRepository.findAll().collectList().block().size();

        // Update the identifierType using partial update
        IdentifierType partialUpdatedIdentifierType = new IdentifierType();
        partialUpdatedIdentifierType.setId(identifierType.getId());

        partialUpdatedIdentifierType.setName(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIdentifierType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIdentifierType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IdentifierType in the database
        List<IdentifierType> identifierTypeList = identifierTypeRepository.findAll().collectList().block();
        assertThat(identifierTypeList).hasSize(databaseSizeBeforeUpdate);
        IdentifierType testIdentifierType = identifierTypeList.get(identifierTypeList.size() - 1);
        assertThat(testIdentifierType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingIdentifierType() throws Exception {
        int databaseSizeBeforeUpdate = identifierTypeRepository.findAll().collectList().block().size();
        identifierType.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, identifierType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(identifierType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentifierType in the database
        List<IdentifierType> identifierTypeList = identifierTypeRepository.findAll().collectList().block();
        assertThat(identifierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIdentifierType() throws Exception {
        int databaseSizeBeforeUpdate = identifierTypeRepository.findAll().collectList().block().size();
        identifierType.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(identifierType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentifierType in the database
        List<IdentifierType> identifierTypeList = identifierTypeRepository.findAll().collectList().block();
        assertThat(identifierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIdentifierType() throws Exception {
        int databaseSizeBeforeUpdate = identifierTypeRepository.findAll().collectList().block().size();
        identifierType.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(identifierType))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IdentifierType in the database
        List<IdentifierType> identifierTypeList = identifierTypeRepository.findAll().collectList().block();
        assertThat(identifierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIdentifierType() {
        // Initialize the database
        identifierType.setId(UUID.randomUUID());
        identifierTypeRepository.save(identifierType).block();

        int databaseSizeBeforeDelete = identifierTypeRepository.findAll().collectList().block().size();

        // Delete the identifierType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, identifierType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IdentifierType> identifierTypeList = identifierTypeRepository.findAll().collectList().block();
        assertThat(identifierTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
