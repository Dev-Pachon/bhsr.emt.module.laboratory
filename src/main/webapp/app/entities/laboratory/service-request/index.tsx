import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ServiceRequest from './service-request';
import ServiceRequestDetail from './service-request-detail';
import { IPatient } from 'app/shared/model/laboratory/patient.model';
import { AdministrativeGender } from 'app/shared/model/enumerations/administrative-gender.model';
import DiagnosticReport from 'app/entities/laboratory/diagnostic-report';
import PatientListToRequestService from 'app/entities/laboratory/service-request/patient-list-to-request-service';
import PrivateRoute from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

const ServiceRequestRoutes = () => {
  const samplePatient: IPatient = {
    id: '6573486f174697337fd76662',
    name: {
      text: 'John Doe',
      given: 'John',
      family: 'Doe',
    },
    active: true,
    gender: AdministrativeGender.MALE,
    birthDate: '1990-01-01',
    address: {
      line: '123 Main St',
      city: 'Anytown',
      state: 'NY',
      country: 'US',
      text: '123 Main St, Anytown, NY, US',
      district: 'Anydistrict',
    },
    contact: {
      address: {
        line: '465 Main St',
        city: 'Anytown',
        state: 'SF',
        country: 'US',
        text: '465 Main St, Anytown, SF, US',
        district: 'Anydistrict',
      },
      gender: AdministrativeGender.FEMALE,
      name: {
        text: 'Jane Doe',
        given: 'Jane',
        family: 'Doe',
      },
      id: '2',
    },
  };
  return (
    <ErrorBoundaryRoutes>
      <Route index element={<ServiceRequest />} />
      {/*<Route path="new" element={<ServiceRequestUpdate patient={samplePatient} />} />*/}

      <Route
        path="new"
        element={
          <PrivateRoute hasAnyAuthorities={[AUTHORITIES.MED]}>
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
