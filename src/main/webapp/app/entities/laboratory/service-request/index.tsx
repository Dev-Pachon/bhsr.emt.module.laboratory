import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ServiceRequest from './service-request';
import ServiceRequestDetail from './service-request-detail';
import ServiceRequestUpdate from './service-request-update';
import { IPatient } from 'app/shared/model/laboratory/patient.model';
import { AdministrativeGender } from 'app/shared/model/enumerations/administrative-gender.model';

const ServiceRequestRoutes = () => {
  const samplePatient: IPatient = {
    id: '1',
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
      <Route path="new" element={<ServiceRequestUpdate patient={samplePatient} />} />
      <Route path=":id">
        <Route index element={<ServiceRequestDetail />} />
      </Route>
    </ErrorBoundaryRoutes>
  );
};

export default ServiceRequestRoutes;
