import dayjs from 'dayjs';
import { IPatient } from 'app/shared/model/laboratory/patient.model';
import { IDiagnosticReportFormat } from 'app/shared/model/laboratory/diagnostic-report-format.model';
import { IServiceRequest } from 'app/shared/model/laboratory/service-request.model';
import { DiagnosticReportStatus } from 'app/shared/model/enumerations/diagnostic-report-status.model';
import { IUser } from 'app/shared/model/user.model';
import { ICustomField } from 'app/shared/model/laboratory/custom-field.model';

export interface IDiagnosticReport {
  id?: string;
  status?: DiagnosticReportStatus;
  createdAt?: string;
  createdBy?: IUser;
  updatedAt?: string;
  updatedBy?: IUser;
  deletedAt?: string | null;
  fields?: ICustomField[] | null;
  subject?: IPatient | null;
  format?: IDiagnosticReportFormat | null;
  basedOn?: IServiceRequest | null;
}

export interface IDiagnosticReportLight {
  id?: string;
  status?: DiagnosticReportStatus;
  createdAt?: string;
  createdBy?: IUser;
  updatedAt?: string;
  updatedBy?: IUser;
  deletedAt?: string | null;
  fields?: ICustomField[] | null;
  subject?: IPatient | null;
  format?: string | null;
  basedOn?: IServiceRequest | null;
}

export const defaultValue: Readonly<IDiagnosticReport> = {};
