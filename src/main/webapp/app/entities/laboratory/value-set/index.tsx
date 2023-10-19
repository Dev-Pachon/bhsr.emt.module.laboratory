import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ValueSet from './value-set';
import ValueSetDetail from './value-set-detail';
import ValueSetUpdate from './value-set-update';
import ValueSetDeleteDialog from './value-set-delete-dialog';

const ValueSetRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ValueSet />} />
    <Route path="new" element={<ValueSetUpdate />} />
    <Route path=":id">
      <Route index element={<ValueSetDetail />} />
      <Route path="edit" element={<ValueSetUpdate />} />
      <Route path="delete" element={<ValueSetDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ValueSetRoutes;
