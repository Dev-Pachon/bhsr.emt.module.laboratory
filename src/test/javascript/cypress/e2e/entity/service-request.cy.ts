import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('ServiceRequest e2e test', () => {
  const serviceRequestPageUrl = '/laboratory/service-request';
  const serviceRequestPageUrlPattern = new RegExp('/laboratory/service-request(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const serviceRequestSample = {
    id: 'a11243eb-a20e-4742-862a-25f89bc1ad4e',
    status: 'ENTERED_IN_ERROR',
    category: 'Books indexing Views',
    priority: 'transmitter payment users',
    code: '3a31bcd3-2d78-487d-b705-ecc111dc1689',
    serviceId: 99399,
    createdAt: '2023-10-19',
    createdBy: 'sensor transition haptic',
    updatedAt: '2023-10-19',
    updatedBy: 'port yellow',
  };

  let serviceRequest;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/laboratory/api/service-requests+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/laboratory/api/service-requests').as('postEntityRequest');
    cy.intercept('DELETE', '/services/laboratory/api/service-requests/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (serviceRequest) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/laboratory/api/service-requests/${serviceRequest.id}`,
      }).then(() => {
        serviceRequest = undefined;
      });
    }
  });

  it('ServiceRequests menu should load ServiceRequests page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('laboratory/service-request');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ServiceRequest').should('exist');
    cy.url().should('match', serviceRequestPageUrlPattern);
  });

  describe('ServiceRequest page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(serviceRequestPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ServiceRequest page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/laboratory/service-request/new$'));
        cy.getEntityCreateUpdateHeading('ServiceRequest');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceRequestPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/laboratory/api/service-requests',
          body: serviceRequestSample,
        }).then(({ body }) => {
          serviceRequest = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/laboratory/api/service-requests+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [serviceRequest],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(serviceRequestPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ServiceRequest page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('serviceRequest');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceRequestPageUrlPattern);
      });

      it('edit button click should load edit ServiceRequest page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ServiceRequest');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceRequestPageUrlPattern);
      });

      it('edit button click should load edit ServiceRequest page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ServiceRequest');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceRequestPageUrlPattern);
      });

      it('last delete button click should delete instance of ServiceRequest', () => {
        cy.intercept('GET', '/services/laboratory/api/service-requests/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('serviceRequest').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceRequestPageUrlPattern);

        serviceRequest = undefined;
      });
    });
  });

  describe('new ServiceRequest page', () => {
    beforeEach(() => {
      cy.visit(`${serviceRequestPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ServiceRequest');
    });

    it('should create an instance of ServiceRequest', () => {
      cy.get(`[data-cy="id"]`).type('3a070025-dd2e-4d5d-b9e0-87741cf57fda').should('have.value', '3a070025-dd2e-4d5d-b9e0-87741cf57fda');

      cy.get(`[data-cy="status"]`).select('REVOKED');

      cy.get(`[data-cy="category"]`).type('compressing Dollar payment').should('have.value', 'compressing Dollar payment');

      cy.get(`[data-cy="priority"]`).type('content-based').should('have.value', 'content-based');

      cy.get(`[data-cy="code"]`)
        .type('ef5fc2ce-2ac8-4e84-89bf-9f3884e54110')
        .invoke('val')
        .should('match', new RegExp('ef5fc2ce-2ac8-4e84-89bf-9f3884e54110'));

      cy.get(`[data-cy="doNotPerform"]`).should('not.be.checked');
      cy.get(`[data-cy="doNotPerform"]`).click().should('be.checked');

      cy.get(`[data-cy="serviceId"]`).type('44903').should('have.value', '44903');

      cy.get(`[data-cy="createdAt"]`).type('2023-10-19').blur().should('have.value', '2023-10-19');

      cy.get(`[data-cy="createdBy"]`).type('Kids Nuevo Sleek').should('have.value', 'Kids Nuevo Sleek');

      cy.get(`[data-cy="updatedAt"]`).type('2023-10-19').blur().should('have.value', '2023-10-19');

      cy.get(`[data-cy="updatedBy"]`).type('Dynamic cultivate Money').should('have.value', 'Dynamic cultivate Money');

      cy.get(`[data-cy="deletedAt"]`).type('2023-10-19').blur().should('have.value', '2023-10-19');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        serviceRequest = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', serviceRequestPageUrlPattern);
    });
  });
});
