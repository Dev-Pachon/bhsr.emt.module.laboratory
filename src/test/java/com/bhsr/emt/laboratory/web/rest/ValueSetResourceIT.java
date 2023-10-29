package com.bhsr.emt.laboratory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.bhsr.emt.laboratory.IntegrationTest;
import com.bhsr.emt.laboratory.domain.ValueSet;
import com.bhsr.emt.laboratory.repository.ValueSetRepository;
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
 * Integration tests for the {@link ValueSetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ValueSetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/value-sets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ValueSetRepository valueSetRepository;

    @Autowired
    private WebTestClient webTestClient;

    private ValueSet valueSet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValueSet createEntity() {
        ValueSet valueSet = new ValueSet();
        valueSet.setName(DEFAULT_NAME);
        return valueSet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValueSet createUpdatedEntity() {
        ValueSet valueSet = new ValueSet();
        valueSet.setName(UPDATED_NAME);
        return valueSet;
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        valueSetRepository.deleteAll().block();
        valueSet = createEntity();
    }

    @Test
    void createValueSet() throws Exception {
        int databaseSizeBeforeCreate = valueSetRepository.findAll().collectList().block().size();
        // Create the ValueSet
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(valueSet))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll().collectList().block();
        assertThat(valueSetList).hasSize(databaseSizeBeforeCreate + 1);
        ValueSet testValueSet = valueSetList.get(valueSetList.size() - 1);
        assertThat(testValueSet.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createValueSetWithExistingId() throws Exception {
        // Create the ValueSet with an existing ID
        valueSet.setId("existing_id");

        int databaseSizeBeforeCreate = valueSetRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(valueSet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll().collectList().block();
        assertThat(valueSetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = valueSetRepository.findAll().collectList().block().size();
        // set the field null
        valueSet.setName(null);

        // Create the ValueSet, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(valueSet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ValueSet> valueSetList = valueSetRepository.findAll().collectList().block();
        assertThat(valueSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllValueSetsAsStream() {
        // Initialize the database
        valueSet.setId(UUID.randomUUID().toString());
        valueSetRepository.save(valueSet).block();

        List<ValueSet> valueSetList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ValueSet.class)
            .getResponseBody()
            .filter(valueSet::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(valueSetList).isNotNull();
        assertThat(valueSetList).hasSize(1);
        ValueSet testValueSet = valueSetList.get(0);
        assertThat(testValueSet.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllValueSets() {
        // Initialize the database
        valueSet.setId(UUID.randomUUID().toString());
        valueSetRepository.save(valueSet).block();

        // Get all the valueSetList
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
            .value(hasItem(valueSet.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getValueSet() {
        // Initialize the database
        valueSet.setId(UUID.randomUUID().toString());
        valueSetRepository.save(valueSet).block();

        // Get the valueSet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, valueSet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(valueSet.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingValueSet() {
        // Get the valueSet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingValueSet() throws Exception {
        // Initialize the database
        valueSet.setId(UUID.randomUUID().toString());
        valueSetRepository.save(valueSet).block();

        int databaseSizeBeforeUpdate = valueSetRepository.findAll().collectList().block().size();

        // Update the valueSet
        ValueSet updatedValueSet = valueSetRepository.findById(valueSet.getId()).block();
        updatedValueSet.setName(UPDATED_NAME);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedValueSet.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedValueSet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll().collectList().block();
        assertThat(valueSetList).hasSize(databaseSizeBeforeUpdate);
        ValueSet testValueSet = valueSetList.get(valueSetList.size() - 1);
        assertThat(testValueSet.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingValueSet() throws Exception {
        int databaseSizeBeforeUpdate = valueSetRepository.findAll().collectList().block().size();
        valueSet.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, valueSet.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(valueSet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll().collectList().block();
        assertThat(valueSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchValueSet() throws Exception {
        int databaseSizeBeforeUpdate = valueSetRepository.findAll().collectList().block().size();
        valueSet.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(valueSet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll().collectList().block();
        assertThat(valueSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamValueSet() throws Exception {
        int databaseSizeBeforeUpdate = valueSetRepository.findAll().collectList().block().size();
        valueSet.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(valueSet))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll().collectList().block();
        assertThat(valueSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateValueSetWithPatch() throws Exception {
        // Initialize the database
        valueSet.setId(UUID.randomUUID().toString());
        valueSetRepository.save(valueSet).block();

        int databaseSizeBeforeUpdate = valueSetRepository.findAll().collectList().block().size();

        // Update the valueSet using partial update
        ValueSet partialUpdatedValueSet = new ValueSet();
        partialUpdatedValueSet.setId(valueSet.getId());

        partialUpdatedValueSet.setName(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedValueSet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedValueSet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll().collectList().block();
        assertThat(valueSetList).hasSize(databaseSizeBeforeUpdate);
        ValueSet testValueSet = valueSetList.get(valueSetList.size() - 1);
        assertThat(testValueSet.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void fullUpdateValueSetWithPatch() throws Exception {
        // Initialize the database
        valueSet.setId(UUID.randomUUID().toString());
        valueSetRepository.save(valueSet).block();

        int databaseSizeBeforeUpdate = valueSetRepository.findAll().collectList().block().size();

        // Update the valueSet using partial update
        ValueSet partialUpdatedValueSet = new ValueSet();
        partialUpdatedValueSet.setId(valueSet.getId());

        partialUpdatedValueSet.setName(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedValueSet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedValueSet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll().collectList().block();
        assertThat(valueSetList).hasSize(databaseSizeBeforeUpdate);
        ValueSet testValueSet = valueSetList.get(valueSetList.size() - 1);
        assertThat(testValueSet.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingValueSet() throws Exception {
        int databaseSizeBeforeUpdate = valueSetRepository.findAll().collectList().block().size();
        valueSet.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, valueSet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(valueSet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll().collectList().block();
        assertThat(valueSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchValueSet() throws Exception {
        int databaseSizeBeforeUpdate = valueSetRepository.findAll().collectList().block().size();
        valueSet.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(valueSet))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll().collectList().block();
        assertThat(valueSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamValueSet() throws Exception {
        int databaseSizeBeforeUpdate = valueSetRepository.findAll().collectList().block().size();
        valueSet.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(valueSet))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll().collectList().block();
        assertThat(valueSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteValueSet() {
        // Initialize the database
        valueSet.setId(UUID.randomUUID().toString());
        valueSetRepository.save(valueSet).block();

        int databaseSizeBeforeDelete = valueSetRepository.findAll().collectList().block().size();

        // Delete the valueSet
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, valueSet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ValueSet> valueSetList = valueSetRepository.findAll().collectList().block();
        assertThat(valueSetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
