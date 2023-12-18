import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ServiceRequest from './service-request';
import ServiceRequestDetail from './service-request-detail';
import DiagnosticReport from 'app/entities/laboratory/diagnostic-report';
import PatientListToRequestService from 'app/entities/laboratory/service-request/patient-list-to-request-service';
import PrivateRoute from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

const ServiceRequestRoutes = () => {
  return (
    <ErrorBoundaryRoutes>
      <Route index element={<ServiceRequest />} />

      <Route
        path="new"
        element={
          <PrivateRoute hasAnyAuthorities={[AUTHORITIES.MEDICAL_USER]}>
            <PatientListToRequestService />
          </PrivateRoute>
        }
      />

      <Route path=":id">
        <Route index element={<ServiceRequestDetail />} />
        <Route path="diagnostic-report/*" element={<DiagnosticReport />} />
      </Route>
    </ErrorBoundaryRoutes>
  );
};

export default ServiceRequestRoutes;
