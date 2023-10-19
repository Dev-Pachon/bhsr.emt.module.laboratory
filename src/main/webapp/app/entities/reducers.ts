import patient from 'app/entities/laboratory/patient/patient.reducer';
import identifierType from 'app/entities/laboratory/identifier-type/identifier-type.reducer';
import diagnosticReport from 'app/entities/laboratory/diagnostic-report/diagnostic-report.reducer';
import diagnosticReportFormat from 'app/entities/laboratory/diagnostic-report-format/diagnostic-report-format.reducer';
import serviceRequest from 'app/entities/laboratory/service-request/service-request.reducer';
import valueSet from 'app/entities/laboratory/value-set/value-set.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  patient,
  identifierType,
  diagnosticReport,
  diagnosticReportFormat,
  serviceRequest,
  valueSet,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
