import dayjs from 'dayjs';
import { IPatient } from 'app/shared/model/laboratory/patient.model';
import { IDiagnosticReportFormat } from 'app/shared/model/laboratory/diagnostic-report-format.model';
import { IServiceRequest } from 'app/shared/model/laboratory/service-request.model';
import { DiagnosticReportStatus } from 'app/shared/model/enumerations/diagnostic-report-status.model';

export interface IDiagnosticReport {
  id?: string;
  status?: DiagnosticReportStatus;
  createdAt?: string;
  createdBy?: string;
  updatedAt?: string;
  updatedBy?: string;
  deletedAt?: string | null;
  subject?: IPatient | null;
  format?: IDiagnosticReportFormat | null;
  basedOn?: IServiceRequest | null;
}

export const defaultValue: Readonly<IDiagnosticReport> = {};
