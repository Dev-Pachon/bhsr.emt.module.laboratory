import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import DiagnosticReportDetail from './diagnostic-report-detail';
import DiagnosticReportUpdate from './diagnostic-report-update';
import PrivateRoute from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

const DiagnosticReportRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path=":diagnosticReportId">
      <Route index element={<DiagnosticReportDetail />} />
      <Route
        path="edit"
        element={
          <PrivateRoute hasAnyAuthorities={[AUTHORITIES.LAB]}>
            <DiagnosticReportUpdate />
          </PrivateRoute>
        }
      />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DiagnosticReportRoutes;
