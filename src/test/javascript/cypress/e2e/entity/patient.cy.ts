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

describe('Patient e2e test', () => {
  const patientPageUrl = '/laboratory/patient';
  const patientPageUrlPattern = new RegExp('/laboratory/patient(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const patientSample = { id: 'ac782442-4d2d-4b74-811c-c8e6d4c21214', active: true, gender: 'OTHER', birthDate: '2023-10-19' };

  let patient;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/laboratory/api/patients+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/laboratory/api/patients').as('postEntityRequest');
    cy.intercept('DELETE', '/services/laboratory/api/patients/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (patient) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/laboratory/api/patients/${patient.id}`,
      }).then(() => {
        patient = undefined;
      });
    }
  });

  it('Patients menu should load Patients page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('laboratory/patient');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Patient').should('exist');
    cy.url().should('match', patientPageUrlPattern);
  });

  describe('Patient page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(patientPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Patient page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/laboratory/patient/new$'));
        cy.getEntityCreateUpdateHeading('Patient');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', patientPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/laboratory/api/patients',
          body: patientSample,
        }).then(({ body }) => {
          patient = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/laboratory/api/patients+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [patient],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(patientPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Patient page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('patient');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', patientPageUrlPattern);
      });

      it('edit button click should load edit Patient page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Patient');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', patientPageUrlPattern);
      });

      it('edit button click should load edit Patient page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Patient');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', patientPageUrlPattern);
      });

      it('last delete button click should delete instance of Patient', () => {
        cy.intercept('GET', '/services/laboratory/api/patients/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('patient').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', patientPageUrlPattern);

        patient = undefined;
      });
    });
  });

  describe('new Patient page', () => {
    beforeEach(() => {
      cy.visit(`${patientPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Patient');
    });

    it('should create an instance of Patient', () => {
      cy.get(`[data-cy="id"]`).type('31a3eaf9-7017-4b86-a9d5-3b35a354379f').should('have.value', '31a3eaf9-7017-4b86-a9d5-3b35a354379f');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click().should('be.checked');

      cy.get(`[data-cy="gender"]`).select('UNKNOWN');

      cy.get(`[data-cy="birthDate"]`).type('2023-10-19').blur().should('have.value', '2023-10-19');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        patient = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', patientPageUrlPattern);
    });
  });
});
