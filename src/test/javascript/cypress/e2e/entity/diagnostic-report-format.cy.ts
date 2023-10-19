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

describe('DiagnosticReportFormat e2e test', () => {
  const diagnosticReportFormatPageUrl = '/laboratory/diagnostic-report-format';
  const diagnosticReportFormatPageUrlPattern = new RegExp('/laboratory/diagnostic-report-format(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const diagnosticReportFormatSample = {
    id: '6a1683e7-ecdd-4535-963d-2d6f43e8bc56',
    createdAt: '2023-10-19',
    createdBy: 'Cheese',
    updatedAt: '2023-10-19',
    updatedBy: 'utilisation',
  };

  let diagnosticReportFormat;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/laboratory/api/diagnostic-report-formats+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/laboratory/api/diagnostic-report-formats').as('postEntityRequest');
    cy.intercept('DELETE', '/services/laboratory/api/diagnostic-report-formats/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (diagnosticReportFormat) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/laboratory/api/diagnostic-report-formats/${diagnosticReportFormat.id}`,
      }).then(() => {
        diagnosticReportFormat = undefined;
      });
    }
  });

  it('DiagnosticReportFormats menu should load DiagnosticReportFormats page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('laboratory/diagnostic-report-format');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DiagnosticReportFormat').should('exist');
    cy.url().should('match', diagnosticReportFormatPageUrlPattern);
  });

  describe('DiagnosticReportFormat page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(diagnosticReportFormatPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DiagnosticReportFormat page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/laboratory/diagnostic-report-format/new$'));
        cy.getEntityCreateUpdateHeading('DiagnosticReportFormat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', diagnosticReportFormatPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/laboratory/api/diagnostic-report-formats',
          body: diagnosticReportFormatSample,
        }).then(({ body }) => {
          diagnosticReportFormat = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/laboratory/api/diagnostic-report-formats+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [diagnosticReportFormat],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(diagnosticReportFormatPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DiagnosticReportFormat page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('diagnosticReportFormat');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', diagnosticReportFormatPageUrlPattern);
      });

      it('edit button click should load edit DiagnosticReportFormat page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DiagnosticReportFormat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', diagnosticReportFormatPageUrlPattern);
      });

      it('edit button click should load edit DiagnosticReportFormat page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DiagnosticReportFormat');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', diagnosticReportFormatPageUrlPattern);
      });

      it('last delete button click should delete instance of DiagnosticReportFormat', () => {
        cy.intercept('GET', '/services/laboratory/api/diagnostic-report-formats/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('diagnosticReportFormat').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', diagnosticReportFormatPageUrlPattern);

        diagnosticReportFormat = undefined;
      });
    });
  });

  describe('new DiagnosticReportFormat page', () => {
    beforeEach(() => {
      cy.visit(`${diagnosticReportFormatPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DiagnosticReportFormat');
    });

    it('should create an instance of DiagnosticReportFormat', () => {
      cy.get(`[data-cy="id"]`).type('b9256cbe-e42e-4e25-9644-150076457290').should('have.value', 'b9256cbe-e42e-4e25-9644-150076457290');

      cy.get(`[data-cy="createdAt"]`).type('2023-10-19').blur().should('have.value', '2023-10-19');

      cy.get(`[data-cy="createdBy"]`).type('Armenian').should('have.value', 'Armenian');

      cy.get(`[data-cy="updatedAt"]`).type('2023-10-19').blur().should('have.value', '2023-10-19');

      cy.get(`[data-cy="updatedBy"]`).type('Synergized copying Bridge').should('have.value', 'Synergized copying Bridge');

      cy.get(`[data-cy="deletedAt"]`).type('2023-10-19').blur().should('have.value', '2023-10-19');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        diagnosticReportFormat = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', diagnosticReportFormatPageUrlPattern);
    });
  });
});
