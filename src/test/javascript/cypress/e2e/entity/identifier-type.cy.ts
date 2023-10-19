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

describe('IdentifierType e2e test', () => {
  const identifierTypePageUrl = '/laboratory/identifier-type';
  const identifierTypePageUrlPattern = new RegExp('/laboratory/identifier-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const identifierTypeSample = { name: 'Dynamic' };

  let identifierType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/laboratory/api/identifier-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/laboratory/api/identifier-types').as('postEntityRequest');
    cy.intercept('DELETE', '/services/laboratory/api/identifier-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (identifierType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/laboratory/api/identifier-types/${identifierType.id}`,
      }).then(() => {
        identifierType = undefined;
      });
    }
  });

  it('IdentifierTypes menu should load IdentifierTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('laboratory/identifier-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IdentifierType').should('exist');
    cy.url().should('match', identifierTypePageUrlPattern);
  });

  describe('IdentifierType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(identifierTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IdentifierType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/laboratory/identifier-type/new$'));
        cy.getEntityCreateUpdateHeading('IdentifierType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identifierTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/laboratory/api/identifier-types',
          body: identifierTypeSample,
        }).then(({ body }) => {
          identifierType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/laboratory/api/identifier-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [identifierType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(identifierTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IdentifierType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('identifierType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identifierTypePageUrlPattern);
      });

      it('edit button click should load edit IdentifierType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IdentifierType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identifierTypePageUrlPattern);
      });

      it('edit button click should load edit IdentifierType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IdentifierType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identifierTypePageUrlPattern);
      });

      it('last delete button click should delete instance of IdentifierType', () => {
        cy.intercept('GET', '/services/laboratory/api/identifier-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('identifierType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', identifierTypePageUrlPattern);

        identifierType = undefined;
      });
    });
  });

  describe('new IdentifierType page', () => {
    beforeEach(() => {
      cy.visit(`${identifierTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IdentifierType');
    });

    it('should create an instance of IdentifierType', () => {
      cy.get(`[data-cy="name"]`).type('withdrawal Bedfordshire Legacy').should('have.value', 'withdrawal Bedfordshire Legacy');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        identifierType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', identifierTypePageUrlPattern);
    });
  });
});
