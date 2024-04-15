package com.bhsr.emt.laboratory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.bhsr.emt.laboratory.IntegrationTest;
import com.bhsr.emt.laboratory.domain.ServiceRequest;
import com.bhsr.emt.laboratory.domain.enumeration.ServiceRequestStatus;
import com.bhsr.emt.laboratory.repository.ServiceRequestRepository;
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
 * Integration tests for the {@link ServiceRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ServiceRequestResourceIT {

    private static final ServiceRequestStatus DEFAULT_STATUS = ServiceRequestStatus.DRAFT;
    private static final ServiceRequestStatus UPDATED_STATUS = ServiceRequestStatus.ACTIVE;

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_PRIORITY = "AAAAAAAAAA";
    private static final String UPDATED_PRIORITY = "BBBBBBBBBB";

    private static final UUID DEFAULT_CODE = UUID.randomUUID();
    private static final UUID UPDATED_CODE = UUID.randomUUID();

    private static final Boolean DEFAULT_DO_NOT_PERFORM = false;
    private static final Boolean UPDATED_DO_NOT_PERFORM = true;

    private static final Integer DEFAULT_SERVICE_ID = 1;
    private static final Integer UPDATED_SERVICE_ID = 2;

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

    private static final String ENTITY_API_URL = "/api/service-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private WebTestClient webTestClient;

    private ServiceRequest serviceRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceRequest createEntity() {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setServiceId(DEFAULT_SERVICE_ID);
        serviceRequest.setCreatedAt(DEFAULT_CREATED_AT);
        serviceRequest.setCreatedBy(DEFAULT_CREATED_BY);
        serviceRequest.setUpdatedAt(DEFAULT_UPDATED_AT);
        serviceRequest.setUpdatedBy(DEFAULT_UPDATED_BY);
        serviceRequest.setDeletedAt(DEFAULT_DELETED_AT);
        serviceRequest.setStatus(DEFAULT_STATUS);
        serviceRequest.setCategory(DEFAULT_CATEGORY);
        serviceRequest.setPriority(DEFAULT_PRIORITY);
        serviceRequest.setCode(DEFAULT_CODE);
        serviceRequest.setDoNotPerform(DEFAULT_DO_NOT_PERFORM);
        return serviceRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceRequest createUpdatedEntity() {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setServiceId(DEFAULT_SERVICE_ID);
        serviceRequest.setCreatedAt(DEFAULT_CREATED_AT);
        serviceRequest.setCreatedBy(DEFAULT_CREATED_BY);
        serviceRequest.setUpdatedAt(DEFAULT_UPDATED_AT);
        serviceRequest.setUpdatedBy(DEFAULT_UPDATED_BY);
        serviceRequest.setDeletedAt(DEFAULT_DELETED_AT);
        serviceRequest.setStatus(DEFAULT_STATUS);
        serviceRequest.setCategory(DEFAULT_CATEGORY);
        serviceRequest.setPriority(DEFAULT_PRIORITY);
        serviceRequest.setCode(DEFAULT_CODE);
        serviceRequest.setDoNotPerform(DEFAULT_DO_NOT_PERFORM);
        return serviceRequest;
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        serviceRequestRepository.deleteAll().block();
        serviceRequest = createEntity();
    }

    @Test
    void createServiceRequest() throws Exception {
        int databaseSizeBeforeCreate = serviceRequestRepository.findAll().collectList().block().size();
        // Create the ServiceRequest
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceRequest testServiceRequest = serviceRequestList.get(serviceRequestList.size() - 1);
        assertThat(testServiceRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testServiceRequest.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testServiceRequest.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testServiceRequest.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testServiceRequest.getDoNotPerform()).isEqualTo(DEFAULT_DO_NOT_PERFORM);
        assertThat(testServiceRequest.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
        assertThat(testServiceRequest.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testServiceRequest.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testServiceRequest.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testServiceRequest.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testServiceRequest.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    void createServiceRequestWithExistingId() throws Exception {
        // Create the ServiceRequest with an existing ID
        serviceRequest.setId("existing_id");

        int databaseSizeBeforeCreate = serviceRequestRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRequestRepository.findAll().collectList().block().size();
        // set the field null
        serviceRequest.setStatus(null);

        // Create the ServiceRequest, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRequestRepository.findAll().collectList().block().size();
        // set the field null
        serviceRequest.setCategory(null);

        // Create the ServiceRequest, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRequestRepository.findAll().collectList().block().size();
        // set the field null
        serviceRequest.setPriority(null);

        // Create the ServiceRequest, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRequestRepository.findAll().collectList().block().size();
        // set the field null
        serviceRequest.setCode(null);

        // Create the ServiceRequest, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkServiceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRequestRepository.findAll().collectList().block().size();
        // set the field null
        serviceRequest.setServiceId(null);

        // Create the ServiceRequest, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRequestRepository.findAll().collectList().block().size();
        // set the field null
        serviceRequest.setCreatedAt(null);

        // Create the ServiceRequest, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRequestRepository.findAll().collectList().block().size();
        // set the field null
        serviceRequest.setCreatedBy(null);

        // Create the ServiceRequest, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRequestRepository.findAll().collectList().block().size();
        // set the field null
        serviceRequest.setUpdatedAt(null);

        // Create the ServiceRequest, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUpdatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRequestRepository.findAll().collectList().block().size();
        // set the field null
        serviceRequest.setUpdatedBy(null);

        // Create the ServiceRequest, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllServiceRequestsAsStream() {
        // Initialize the database
        serviceRequest.setId(UUID.randomUUID().toString());
        serviceRequestRepository.save(serviceRequest).block();

        List<ServiceRequest> serviceRequestList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ServiceRequest.class)
            .getResponseBody()
            .filter(serviceRequest::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(serviceRequestList).isNotNull();
        assertThat(serviceRequestList).hasSize(1);
        ServiceRequest testServiceRequest = serviceRequestList.get(0);
        assertThat(testServiceRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testServiceRequest.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testServiceRequest.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testServiceRequest.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testServiceRequest.getDoNotPerform()).isEqualTo(DEFAULT_DO_NOT_PERFORM);
        assertThat(testServiceRequest.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
        assertThat(testServiceRequest.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testServiceRequest.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testServiceRequest.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testServiceRequest.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testServiceRequest.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    void getAllServiceRequests() {
        // Initialize the database
        serviceRequest.setId(UUID.randomUUID().toString());
        serviceRequestRepository.save(serviceRequest).block();

        // Get all the serviceRequestList
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
            .value(hasItem(serviceRequest.getId()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].category")
            .value(hasItem(DEFAULT_CATEGORY))
            .jsonPath("$.[*].priority")
            .value(hasItem(DEFAULT_PRIORITY))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE.toString()))
            .jsonPath("$.[*].doNotPerform")
            .value(hasItem(DEFAULT_DO_NOT_PERFORM.booleanValue()))
            .jsonPath("$.[*].serviceId")
            .value(hasItem(DEFAULT_SERVICE_ID))
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
    void getServiceRequest() {
        // Initialize the database
        serviceRequest.setId(UUID.randomUUID().toString());
        serviceRequestRepository.save(serviceRequest).block();

        // Get the serviceRequest
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, serviceRequest.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(serviceRequest.getId()))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.category")
            .value(is(DEFAULT_CATEGORY))
            .jsonPath("$.priority")
            .value(is(DEFAULT_PRIORITY))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE.toString()))
            .jsonPath("$.doNotPerform")
            .value(is(DEFAULT_DO_NOT_PERFORM.booleanValue()))
            .jsonPath("$.serviceId")
            .value(is(DEFAULT_SERVICE_ID))
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
    void getNonExistingServiceRequest() {
        // Get the serviceRequest
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingServiceRequest() throws Exception {
        // Initialize the database
        serviceRequest.setId(UUID.randomUUID().toString());
        serviceRequestRepository.save(serviceRequest).block();

        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().collectList().block().size();

        // Update the serviceRequest
        ServiceRequest updatedServiceRequest = serviceRequestRepository.findById(serviceRequest.getId()).block();
        updatedServiceRequest.setServiceId(DEFAULT_SERVICE_ID);
        updatedServiceRequest.setCreatedAt(DEFAULT_CREATED_AT);
        updatedServiceRequest.setCreatedBy(DEFAULT_CREATED_BY);
        updatedServiceRequest.setUpdatedAt(DEFAULT_UPDATED_AT);
        updatedServiceRequest.setUpdatedBy(DEFAULT_UPDATED_BY);
        updatedServiceRequest.setDeletedAt(DEFAULT_DELETED_AT);
        updatedServiceRequest.setStatus(DEFAULT_STATUS);
        updatedServiceRequest.setCategory(DEFAULT_CATEGORY);
        updatedServiceRequest.setPriority(DEFAULT_PRIORITY);
        updatedServiceRequest.setCode(DEFAULT_CODE);
        updatedServiceRequest.setDoNotPerform(DEFAULT_DO_NOT_PERFORM);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedServiceRequest.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedServiceRequest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
        ServiceRequest testServiceRequest = serviceRequestList.get(serviceRequestList.size() - 1);
        assertThat(testServiceRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testServiceRequest.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testServiceRequest.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testServiceRequest.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testServiceRequest.getDoNotPerform()).isEqualTo(UPDATED_DO_NOT_PERFORM);
        assertThat(testServiceRequest.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testServiceRequest.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testServiceRequest.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testServiceRequest.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testServiceRequest.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testServiceRequest.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    void putNonExistingServiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().collectList().block().size();
        serviceRequest.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, serviceRequest.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchServiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().collectList().block().size();
        serviceRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamServiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().collectList().block().size();
        serviceRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateServiceRequestWithPatch() throws Exception {
        // Initialize the database
        serviceRequest.setId(UUID.randomUUID().toString());
        serviceRequestRepository.save(serviceRequest).block();

        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().collectList().block().size();

        // Update the serviceRequest using partial update
        ServiceRequest partialUpdatedServiceRequest = new ServiceRequest();
        partialUpdatedServiceRequest.setId(serviceRequest.getId());
        partialUpdatedServiceRequest.setServiceId(UPDATED_SERVICE_ID);
        partialUpdatedServiceRequest.setCreatedAt(UPDATED_CREATED_AT);
        partialUpdatedServiceRequest.setCreatedBy(UPDATED_CREATED_BY);
        partialUpdatedServiceRequest.setUpdatedAt(UPDATED_UPDATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedServiceRequest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceRequest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
        ServiceRequest testServiceRequest = serviceRequestList.get(serviceRequestList.size() - 1);
        assertThat(testServiceRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testServiceRequest.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testServiceRequest.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testServiceRequest.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testServiceRequest.getDoNotPerform()).isEqualTo(DEFAULT_DO_NOT_PERFORM);
        assertThat(testServiceRequest.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testServiceRequest.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testServiceRequest.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testServiceRequest.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testServiceRequest.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testServiceRequest.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    void fullUpdateServiceRequestWithPatch() throws Exception {
        // Initialize the database
        serviceRequest.setId(UUID.randomUUID().toString());
        serviceRequestRepository.save(serviceRequest).block();

        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().collectList().block().size();

        // Update the serviceRequest using partial update
        ServiceRequest partialUpdatedServiceRequest = new ServiceRequest();
        partialUpdatedServiceRequest.setId(serviceRequest.getId());
        partialUpdatedServiceRequest.setUpdatedBy(UPDATED_UPDATED_BY);
        partialUpdatedServiceRequest.setDeletedAt(UPDATED_DELETED_AT);
        partialUpdatedServiceRequest.setStatus(UPDATED_STATUS);
        partialUpdatedServiceRequest.setCategory(UPDATED_CATEGORY);
        partialUpdatedServiceRequest.setPriority(UPDATED_PRIORITY);
        partialUpdatedServiceRequest.setCode(UPDATED_CODE);
        partialUpdatedServiceRequest.setDoNotPerform(UPDATED_DO_NOT_PERFORM);
        partialUpdatedServiceRequest.setServiceId(UPDATED_SERVICE_ID);
        partialUpdatedServiceRequest.setCreatedAt(UPDATED_CREATED_AT);
        partialUpdatedServiceRequest.setCreatedBy(UPDATED_CREATED_BY);
        partialUpdatedServiceRequest.setUpdatedAt(UPDATED_UPDATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedServiceRequest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceRequest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
        ServiceRequest testServiceRequest = serviceRequestList.get(serviceRequestList.size() - 1);
        assertThat(testServiceRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testServiceRequest.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testServiceRequest.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testServiceRequest.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testServiceRequest.getDoNotPerform()).isEqualTo(UPDATED_DO_NOT_PERFORM);
        assertThat(testServiceRequest.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testServiceRequest.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testServiceRequest.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testServiceRequest.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testServiceRequest.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testServiceRequest.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    void patchNonExistingServiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().collectList().block().size();
        serviceRequest.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, serviceRequest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchServiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().collectList().block().size();
        serviceRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamServiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().collectList().block().size();
        serviceRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceRequest))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteServiceRequest() {
        // Initialize the database
        serviceRequest.setId(UUID.randomUUID().toString());
        serviceRequestRepository.save(serviceRequest).block();

        int databaseSizeBeforeDelete = serviceRequestRepository.findAll().collectList().block().size();

        // Delete the serviceRequest
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, serviceRequest.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll().collectList().block();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
