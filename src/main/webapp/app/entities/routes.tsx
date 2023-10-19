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
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('laboratory', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="/patient/*" element={<Patient />} />
        <Route path="/identifier-type/*" element={<IdentifierType />} />
        <Route path="/diagnostic-report/*" element={<DiagnosticReport />} />
        <Route path="/diagnostic-report-format/*" element={<DiagnosticReportFormat />} />
        <Route path="/service-request/*" element={<ServiceRequest />} />
        <Route path="/value-set/*" element={<ValueSet />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
