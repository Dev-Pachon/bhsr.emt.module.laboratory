import dayjs from 'dayjs';
import { ServiceRequestStatus } from 'app/shared/model/enumerations/service-request-status.model';
import { IUser } from 'app/shared/model/user.model';

export interface IServiceRequest {
  id?: string;
  status?: ServiceRequestStatus;
  category?: string;
  priority?: string;
  diagnosticReportsIds?: string[];
  doNotPerform?: boolean | null;
  serviceId?: number;
  patientId?: string;
  createdAt?: string;
  createdBy?: IUser;
  updatedAt?: string;
  updatedBy?: IUser;
  deletedAt?: string | null;
}

export const defaultValue: Readonly<IServiceRequest> = {
  doNotPerform: false,
  diagnosticReportsIds: [],
};
