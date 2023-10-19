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

describe('DiagnosticReport e2e test', () => {
  const diagnosticReportPageUrl = '/laboratory/diagnostic-report';
  const diagnosticReportPageUrlPattern = new RegExp('/laboratory/diagnostic-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const diagnosticReportSample = {
    id: '9ceac310-b9ae-439f-8724-e5a8b70a45b7',
    status: 'PRELIMINARY',
    createdAt: '2023-10-19',
    createdBy: 'scale District',
    updatedAt: '2023-10-18',
    updatedBy: 'Car logistical',
  };

  let diagnosticReport;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/laboratory/api/diagnostic-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/laboratory/api/diagnostic-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/services/laboratory/api/diagnostic-reports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (diagnosticReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/laboratory/api/diagnostic-reports/${diagnosticReport.id}`,
      }).then(() => {
        diagnosticReport = undefined;
      });
    }
  });

  it('DiagnosticReports menu should load DiagnosticReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('laboratory/diagnostic-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DiagnosticReport').should('exist');
    cy.url().should('match', diagnosticReportPageUrlPattern);
  });

  describe('DiagnosticReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(diagnosticReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DiagnosticReport page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/laboratory/diagnostic-report/new$'));
        cy.getEntityCreateUpdateHeading('DiagnosticReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', diagnosticReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/laboratory/api/diagnostic-reports',
          body: diagnosticReportSample,
        }).then(({ body }) => {
          diagnosticReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/laboratory/api/diagnostic-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [diagnosticReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(diagnosticReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DiagnosticReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('diagnosticReport');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', diagnosticReportPageUrlPattern);
      });

      it('edit button click should load edit DiagnosticReport page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DiagnosticReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', diagnosticReportPageUrlPattern);
      });

      it('edit button click should load edit DiagnosticReport page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DiagnosticReport');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', diagnosticReportPageUrlPattern);
      });

      it('last delete button click should delete instance of DiagnosticReport', () => {
        cy.intercept('GET', '/services/laboratory/api/diagnostic-reports/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('diagnosticReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', diagnosticReportPageUrlPattern);

        diagnosticReport = undefined;
      });
    });
  });

  describe('new DiagnosticReport page', () => {
    beforeEach(() => {
      cy.visit(`${diagnosticReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DiagnosticReport');
    });

    it('should create an instance of DiagnosticReport', () => {
      cy.get(`[data-cy="id"]`).type('b0050f06-8440-43d6-991c-2b033f795908').should('have.value', 'b0050f06-8440-43d6-991c-2b033f795908');

      cy.get(`[data-cy="status"]`).select('PRELIMINARY');

      cy.get(`[data-cy="createdAt"]`).type('2023-10-19').blur().should('have.value', '2023-10-19');

      cy.get(`[data-cy="createdBy"]`).type('Granite').should('have.value', 'Granite');

      cy.get(`[data-cy="updatedAt"]`).type('2023-10-19').blur().should('have.value', '2023-10-19');

      cy.get(`[data-cy="updatedBy"]`).type('solutions XSS').should('have.value', 'solutions XSS');

      cy.get(`[data-cy="deletedAt"]`).type('2023-10-19').blur().should('have.value', '2023-10-19');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        diagnosticReport = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', diagnosticReportPageUrlPattern);
    });
  });
});
