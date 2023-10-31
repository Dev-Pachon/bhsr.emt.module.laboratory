import dayjs from 'dayjs';
import { IFieldFormat } from 'app/shared/model/laboratory/field-format.model';

export interface IDiagnosticReportFormat {
  id?: string;
  name?: string;
  fieldFormats?: IFieldFormat[];
  createdAt?: string;
  createdBy?: string;
  updatedAt?: string;
  updatedBy?: string;
  deletedAt?: string | null;
}

export const defaultValue: Readonly<IDiagnosticReportFormat> = {
  fieldFormats: [],
};
