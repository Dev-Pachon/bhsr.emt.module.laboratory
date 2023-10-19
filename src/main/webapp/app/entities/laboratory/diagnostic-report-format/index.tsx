import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DiagnosticReportFormat from './diagnostic-report-format';
import DiagnosticReportFormatDetail from './diagnostic-report-format-detail';
import DiagnosticReportFormatUpdate from './diagnostic-report-format-update';
import DiagnosticReportFormatDeleteDialog from './diagnostic-report-format-delete-dialog';

const DiagnosticReportFormatRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DiagnosticReportFormat />} />
    <Route path="new" element={<DiagnosticReportFormatUpdate />} />
    <Route path=":id">
      <Route index element={<DiagnosticReportFormatDetail />} />
      <Route path="edit" element={<DiagnosticReportFormatUpdate />} />
      <Route path="delete" element={<DiagnosticReportFormatDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DiagnosticReportFormatRoutes;
