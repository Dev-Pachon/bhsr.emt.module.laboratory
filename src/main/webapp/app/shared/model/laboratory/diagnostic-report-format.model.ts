import dayjs from 'dayjs';
import { IFieldFormat } from 'app/shared/model/laboratory/field-format.model';
import { IUser } from 'app/shared/model/user.model';

export interface IDiagnosticReportFormat {
  id?: string;
  name?: string;
  fieldFormats?: IFieldFormat[];
  createdAt?: string;
  createdBy?: IUser;
  updatedAt?: string;
  updatedBy?: IUser;
  deletedAt?: string | null;
}

export const defaultValue: Readonly<IDiagnosticReportFormat> = {
  fieldFormats: [],
};
