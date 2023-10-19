import dayjs from 'dayjs';
import { ServiceRequestStatus } from 'app/shared/model/enumerations/service-request-status.model';

export interface IServiceRequest {
  id?: string;
  status?: ServiceRequestStatus;
  category?: string;
  priority?: string;
  code?: string;
  doNotPerform?: boolean | null;
  serviceId?: number;
  createdAt?: string;
  createdBy?: string;
  updatedAt?: string;
  updatedBy?: string;
  deletedAt?: string | null;
}

export const defaultValue: Readonly<IServiceRequest> = {
  doNotPerform: false,
};
