import React from 'react';
import { Navigate, Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { combineReducers, ReducersMapObject } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';
import IdentifierType from './laboratory/identifier-type';
import DiagnosticReportFormat from './laboratory/diagnostic-report-format';
import ServiceRequest from './laboratory/service-request';
import ValueSet from './laboratory/value-set';
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
          <Route path="/service-request/*" element={<ServiceRequest />} />
        </ErrorBoundaryRoutes>
      </PrivateRoute>

      <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN]}>
        <ErrorBoundaryRoutes>
          <Route path="/identifier-type/*" element={<IdentifierType />} />
          <Route path="/diagnostic-report-format/*" element={<DiagnosticReportFormat />} />
          <Route path="/value-set/*" element={<ValueSet />} />
        </ErrorBoundaryRoutes>
      </PrivateRoute>

      <PrivateRoute>
        <ErrorBoundaryRoutes>
          <Route>
            <Route path="/" element={<Navigate to="/404" />} />
          </Route>
        </ErrorBoundaryRoutes>
      </PrivateRoute>
    </div>
  );
};
