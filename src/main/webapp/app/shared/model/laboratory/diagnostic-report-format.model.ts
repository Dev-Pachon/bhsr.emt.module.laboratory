import dayjs from 'dayjs';

export interface IDiagnosticReportFormat {
  id?: string;
  createdAt?: string;
  createdBy?: string;
  updatedAt?: string;
  updatedBy?: string;
  deletedAt?: string | null;
}

export const defaultValue: Readonly<IDiagnosticReportFormat> = {};
