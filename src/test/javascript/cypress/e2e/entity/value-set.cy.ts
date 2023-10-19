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

describe('ValueSet e2e test', () => {
  const valueSetPageUrl = '/laboratory/value-set';
  const valueSetPageUrlPattern = new RegExp('/laboratory/value-set(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const valueSetSample = { id: 'd44d028e-2c9b-4bc4-ab19-1a293966e5ff', name: 'card Brunei Devolved' };

  let valueSet;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/laboratory/api/value-sets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/laboratory/api/value-sets').as('postEntityRequest');
    cy.intercept('DELETE', '/services/laboratory/api/value-sets/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (valueSet) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/laboratory/api/value-sets/${valueSet.id}`,
      }).then(() => {
        valueSet = undefined;
      });
    }
  });

  it('ValueSets menu should load ValueSets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('laboratory/value-set');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ValueSet').should('exist');
    cy.url().should('match', valueSetPageUrlPattern);
  });

  describe('ValueSet page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(valueSetPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ValueSet page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/laboratory/value-set/new$'));
        cy.getEntityCreateUpdateHeading('ValueSet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', valueSetPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/laboratory/api/value-sets',
          body: valueSetSample,
        }).then(({ body }) => {
          valueSet = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/laboratory/api/value-sets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [valueSet],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(valueSetPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ValueSet page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('valueSet');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', valueSetPageUrlPattern);
      });

      it('edit button click should load edit ValueSet page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ValueSet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', valueSetPageUrlPattern);
      });

      it('edit button click should load edit ValueSet page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ValueSet');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', valueSetPageUrlPattern);
      });

      it('last delete button click should delete instance of ValueSet', () => {
        cy.intercept('GET', '/services/laboratory/api/value-sets/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('valueSet').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', valueSetPageUrlPattern);

        valueSet = undefined;
      });
    });
  });

  describe('new ValueSet page', () => {
    beforeEach(() => {
      cy.visit(`${valueSetPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ValueSet');
    });

    it('should create an instance of ValueSet', () => {
      cy.get(`[data-cy="id"]`).type('6c0e0104-c8ed-4f36-bee3-2261e53bac36').should('have.value', '6c0e0104-c8ed-4f36-bee3-2261e53bac36');

      cy.get(`[data-cy="name"]`).type('bypass').should('have.value', 'bypass');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        valueSet = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', valueSetPageUrlPattern);
    });
  });
});
