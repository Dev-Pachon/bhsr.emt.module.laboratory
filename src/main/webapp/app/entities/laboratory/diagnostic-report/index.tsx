import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DiagnosticReport from './diagnostic-report';
import DiagnosticReportDetail from './diagnostic-report-detail';
import DiagnosticReportUpdate from './diagnostic-report-update';
import DiagnosticReportDeleteDialog from './diagnostic-report-delete-dialog';

const DiagnosticReportRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DiagnosticReport />} />
    <Route path="new" element={<DiagnosticReportUpdate />} />
    <Route path=":id">
      <Route index element={<DiagnosticReportDetail />} />
      <Route path="edit" element={<DiagnosticReportUpdate />} />
      <Route path="delete" element={<DiagnosticReportDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DiagnosticReportRoutes;
