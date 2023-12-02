import dayjs from 'dayjs';
import { ServiceRequestStatus } from 'app/shared/model/enumerations/service-request-status.model';
import { IUser } from 'app/shared/model/user.model';
import { IDiagnosticReportFormat } from 'app/shared/model/laboratory/diagnostic-report-format.model';
import { IPatient } from 'app/shared/model/laboratory/patient.model';
import { IDiagnosticReport } from 'app/shared/model/laboratory/diagnostic-report.model';

export interface IServiceRequest {
  id?: string;
  status?: ServiceRequestStatus;
  category?: string;
  priority?: string;
  diagnosticReportsFormats?: string[];
  doNotPerform?: boolean | null;
  serviceId?: number;
  subject?: string;
  createdAt?: string;
  createdBy?: IUser;
  updatedAt?: string;
  updatedBy?: IUser;
  deletedAt?: string | null;
}

export interface IServiceRequestResponse {
  id?: string;
  status?: ServiceRequestStatus;
  category?: string;
  priority?: string;
  diagnosticReports?: IDiagnosticReport[];
  doNotPerform?: boolean | null;
  serviceId?: number;
  subject?: IPatient;
  createdAt?: string;
  createdBy?: IUser;
  updatedAt?: string;
  updatedBy?: IUser;
  deletedAt?: string | null;
}

export const defaultValueResponse: Readonly<IServiceRequestResponse> = {
  doNotPerform: false,
  diagnosticReports: [],
};

export const defaultValue: Readonly<IServiceRequest> = {
  doNotPerform: false,
  diagnosticReportsFormats: [],
};
