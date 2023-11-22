import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';

import Patient from './laboratory/patient';
import IdentifierType from './laboratory/identifier-type';
import DiagnosticReport from './laboratory/diagnostic-report';
import DiagnosticReportFormat from './laboratory/diagnostic-report-format';
import ServiceRequest from './laboratory/service-request';
import ValueSet from './laboratory/value-set';
import { LaboratoryHome } from 'app/entities/laboratory';
import PrivateRoute from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('laboratory', combineReducers(entitiesReducers as ReducersMapObject));

  return (
    <div>
      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.MED, AUTHORITIES.LAB]}>
        <ErrorBoundaryRoutes>
          <Route path="/">
            <Route path="/service-request/*" element={<ServiceRequest />} />
          </Route>
        </ErrorBoundaryRoutes>
      </PrivateRoute>

      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN]}>
        <ErrorBoundaryRoutes>
          <Route path="/" element={<LaboratoryHome />}>
            <Route path="/identifier-type/*" element={<IdentifierType />} />
            <Route path="/diagnostic-report-format/*" element={<DiagnosticReportFormat />} />
            <Route path="/value-set/*" element={<ValueSet />} />
          </Route>
        </ErrorBoundaryRoutes>
      </PrivateRoute>
    </div>
  );
};
